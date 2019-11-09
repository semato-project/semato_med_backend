package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.model.PasswordResetToken;
import semato.semato_med.model.User;
import semato.semato_med.payload.*;
import semato.semato_med.repository.PasswordResetTokenRepository;
import semato.semato_med.repository.UserRepository;
import semato.semato_med.security.CurrentUser;
import semato.semato_med.security.UserPrincipal;
import semato.semato_med.service.EmailSender;
import semato.semato_med.service.PasswordChanger;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PasswordController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PasswordChanger passwordChanger;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    EmailSender emailSender;

    @PostMapping("/user/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String userEmail) {

        Optional<User> user = userRepository.findByEmail(userEmail);
        if (!user.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(false, "Email is not in database"), HttpStatus.NOT_FOUND);
        }

        emailSender.send(passwordChanger.constructResetTokenEmail("localhost", passwordChanger.createPasswordResetToken(user.get()), user.get()));


        return ResponseEntity.ok(new ApiResponse(true, "Email is send"));
    }

    @GetMapping("/user/confirmPassword")
    public ResponseEntity<?> confirmPassword(
            @RequestParam("id") Long id,
            @RequestParam("token") String token) {


        String result = passwordChanger.validatePasswordResetToken(id, token);

        if ( result != null) {
            return new ResponseEntity<>(new ApiResponse(false, result), HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(new ApiResponse(true, "Confirm successfully"));
    }

    //TODO: poprawić resetowanie hasła, dostęp tylko dla osób które dostaną confirm w metodzie wyżej.

    @PostMapping("/user/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {

        Optional<User> user = userRepository.findById(resetPasswordRequest.getId());

        if (!user.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(false, "Invalid token"), HttpStatus.BAD_REQUEST);
        }

        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByTokenAndUser(resetPasswordRequest.getToken(), user.get());

        if (!passwordResetToken.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(false, "Invalid token"), HttpStatus.BAD_REQUEST);
        }

        if (!passwordResetToken.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            return new ResponseEntity<>(new ApiResponse(false, "Token is expired"), HttpStatus.BAD_REQUEST);
        }

        user.get().setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepository.save(user.get());

        passwordResetToken.get().setExpiryDate(LocalDateTime.now());
        passwordResetTokenRepository.save(passwordResetToken.get());

        return ResponseEntity.ok(new ApiResponse(true, "Password reset successfully"));
    }

    @PostMapping("/user/changePassword")
    @Secured({"ROLE_PATIENT", "ROLE_PHYSICIAN"})
    public ResponseEntity<?> changePassword(@CurrentUser UserPrincipal currentUser, @RequestBody ChangePasswordRequest changePasswordRequest) {

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), currentUser.getPassword())) {
            return new ResponseEntity<>(new ApiResponse(false, "Incorrect old password"), HttpStatus.BAD_REQUEST);
        }

        if (changePasswordRequest.getOldPassword().equals(changePasswordRequest.getNewPassword())) {
            return new ResponseEntity<>(new ApiResponse(false, "New password cannot equals old password"), HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = userRepository.findByEmail(currentUser.getEmail());

        if (user.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(false, "User not found"), HttpStatus.BAD_REQUEST);
        }

        user.get().setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user.get());

        return ResponseEntity.ok(new ApiResponse(true, "Password change successfully"));

    }
}
