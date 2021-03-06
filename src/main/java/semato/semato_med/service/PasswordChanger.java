package semato.semato_med.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import semato.semato_med.model.PasswordResetToken;
import semato.semato_med.model.User;
import semato.semato_med.repository.PasswordResetTokenRepository;
import semato.semato_med.repository.UserRepository;
import semato.semato_med.security.JwtTokenProvider;
import semato.semato_med.security.UserPrincipal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordChanger {

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    public String createPasswordResetToken(User user) {
        String token = UUID.randomUUID().toString();
        passwordResetTokenRepository.save(new PasswordResetToken(token, user));
        return token;
    }

    public SimpleMailMessage constructResetTokenEmail(String contextPath, String token, User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Password reset");
        email.setText("To reset password click link below: " + System.getProperty("line.separator") + contextPath + "/api/user/confirmPassword?id=" + user.getId() + "&token=" + token);
        email.setTo(user.getEmail());
        email.setFrom("SematoMedClinic");
        return email;
    }

    public String validatePasswordResetToken(long id, String token) {

        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            return "Invalid token";
        }

        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByTokenAndUser(token, user.get());

        if (!passwordResetToken.isPresent()) {
            return "Invalid token";
        }

        if (!passwordResetToken.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            return "Token is expired";
        }

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        UserPrincipal.create(user.get()),
                        null,
                        Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE"))
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return null;
    }


    public void expiryPasswordToken(PasswordResetToken passwordResetToken){
        passwordResetToken.setExpiryDate(LocalDateTime.now());
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public void saveNewPassword(User user, String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public boolean isOldPasswordIsValid(String introducedPassword, String oldPassword){
        return passwordEncoder.matches(introducedPassword, oldPassword);
    }
}
