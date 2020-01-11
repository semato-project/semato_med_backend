package semato.semato_med.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import semato.semato_med.exception.AppException;
import semato.semato_med.model.*;
import semato.semato_med.repository.AdminRepository;
import semato.semato_med.repository.RoleRepository;
import semato.semato_med.repository.UserRepository;

import java.util.Collections;

@Component
@Order(3)
public class AdminLoader /*implements ApplicationRunner*/ {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static final String EMAIL = "superadmin@example.com";
    public static final String PASSWORD = "TajneHaslo!";

    public void run(ApplicationArguments args) {

        if(! userRepository.findByEmail(EMAIL).isPresent()) {

            User user = new User();
            user.setFirstName("Roman");
            user.setLastName("Tajny");
            user.setEmail(EMAIL);
            user.setPhone("007-007-007");
            user.setPassword(passwordEncoder.encode(PASSWORD));
            Role userRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new AppException("User Role not set."));
            user.setRoles(Collections.singleton(userRole));

            Admin admin = new Admin(user);
            adminRepository.save(admin);

        }
    }
}
