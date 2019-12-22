package semato.semato_med.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import semato.semato_med.exception.AppException;
import semato.semato_med.model.Patient;
import semato.semato_med.model.Role;
import semato.semato_med.model.RoleName;
import semato.semato_med.model.User;
import semato.semato_med.repository.PatientRepository;
import semato.semato_med.repository.RoleRepository;
import semato.semato_med.repository.UserRepository;

import java.time.LocalDate;
import java.util.Collections;

@Component
@Order(2)
public class PatientLoader implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static final String EMAIL = "mrt.wonsik@gmail.com";
    public static final String PASSWORD = "qwerty";

    @Override
    public void run(ApplicationArguments args) {

        if(! userRepository.findByEmail(EMAIL).isPresent()) {

            User user = new User();
            user.setFirstName("Alek");
            user.setLastName("Lolek");
            user.setEmail(EMAIL);
            user.setPhone("123-123-123");
            user.setPassword(passwordEncoder.encode(PASSWORD));
            Role userRole = roleRepository.findByName(RoleName.ROLE_PATIENT).orElseThrow(() -> new AppException("User Role not set."));
            user.setRoles(Collections.singleton(userRole));

            Patient patient = new Patient();
            patient.setBirthDate(LocalDate.of(1999,1,1));
            patient.setCity("Kraków");
            patient.setHouseNumber("2");
            patient.setPesel("12345678901");
            patient.setPostalCode("33-333");
            patient.setStreet("Misiewicza");
            patient.setUser(user);
            patientRepository.save(patient);

        }
    }
}
