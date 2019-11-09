package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.model.User;
import semato.semato_med.payload.ApiResponse;
import semato.semato_med.payload.ChangePasswordRequest;
import semato.semato_med.payload.UserInfo;
import semato.semato_med.repository.UserRepository;
import semato.semato_med.security.CurrentUser;
import semato.semato_med.security.UserPrincipal;

import java.util.Optional;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('PATIENT')")
    public UserInfo getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return new UserInfo(currentUser.getEmail(), currentUser.getName(), currentUser.getSurname());
    }

    @PostMapping("/user/changePassword")
    @Secured({"ROLE_PATIENT","ROLE_PHYSICIAN"})
    public ResponseEntity<?> changePassword(@CurrentUser UserPrincipal currentUser, @RequestBody ChangePasswordRequest changePasswordRequest){

        if(!passwordEncoder.matches(changePasswordRequest.getOldPassword(), currentUser.getPassword())){
            return new ResponseEntity<>(new ApiResponse(false, "Incorrect old password"), HttpStatus.BAD_REQUEST);
        }

        if(changePasswordRequest.getOldPassword().equals(changePasswordRequest.getNewPassword())){
            return new ResponseEntity<>(new ApiResponse(false, "New password cannot equals old password"), HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = userRepository.findByEmail(currentUser.getEmail());

        if(user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        }

        userRepository.save(user.get());

        return ResponseEntity.ok(new ApiResponse(true, "Password change successfully"));

    }
}
