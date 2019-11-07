package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import semato.semato_med.exception.AppException;
import semato.semato_med.model.Patient;
import semato.semato_med.model.Role;
import semato.semato_med.model.RoleName;
import semato.semato_med.model.User;
import semato.semato_med.payload.ApiResponse;
import semato.semato_med.payload.JwtAuthenticationResponse;
import semato.semato_med.payload.LoginRequest;
import semato.semato_med.payload.SingUpRequest;
import semato.semato_med.repository.PatientRepository;
import semato.semato_med.repository.RoleRepository;
import semato.semato_med.repository.UserRepository;
import semato.semato_med.security.JwtTokenProvider;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/singup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SingUpRequest singUpRequest) {
        if (userRepository.existsByEmail(singUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(singUpRequest.getEmail(), singUpRequest.getFirstName(), singUpRequest.getLastName(), singUpRequest.getPassword());

        if (!singUpRequest.getPhone().isEmpty()) {
            user.setPhone(singUpRequest.getPhone());
        }

        Patient patient = new Patient(user, singUpRequest.getPesel(), singUpRequest.getBirthDate());

        if (!singUpRequest.getCity().isEmpty()) {
            patient.setCity(singUpRequest.getCity());
        }

        if (!singUpRequest.getPostalCode().isEmpty()) {
            patient.setPostalCode(singUpRequest.getPostalCode());
        }

        if (!singUpRequest.getStreet().isEmpty()) {
            patient.setStreet(singUpRequest.getStreet());
        }

        if (!singUpRequest.getHouseNumber().isEmpty()) {
            patient.setHouseNumber(singUpRequest.getHouseNumber());
        }


        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_PATIENT).orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        Patient result = patientRepository.save(patient);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUser().getEmail()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
