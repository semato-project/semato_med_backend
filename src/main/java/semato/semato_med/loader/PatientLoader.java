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

    public static final String TRISTAN_PAKISTANCZYK_EXAMPLE_EMAIL = "tristanpakistanczyk@example.com";
    public static final String PAULINA_REKIN_EXAMPLE_EMAIL = "paulinarekin@example.com";
    public static final String ALEK_OLEK_EXAMPLE_EMAIL = "aleklolek@example.com";
    public static final String ROMEK_BROMEK_EXAMPLE_EMAIL = "romekbromek@example.com";
    public static final String KASIA_ZPODLASIA_EXAMPLE_EMAIL = "kasiazpodlasia@example.com";
    public static final String WACLAW_KUMATY_EXAMPLE_EMAIL = "waclawkumaty@example.com";
    public static final String TEMP_PASSWORD = "qwerty";


    @Override
    public void run(ApplicationArguments args) {

        if (!userRepository.findByEmail(ALEK_OLEK_EXAMPLE_EMAIL).isPresent()) {

            saveUser("Alek",
                    "Lolek",
                    ALEK_OLEK_EXAMPLE_EMAIL,
                    "123-123-123",
                    TEMP_PASSWORD,
                    LocalDate.of(1999, 1, 1),
                    "Kraków",
                    "2",
                    "12345678901",
                    "33-333",
                    "Misiewizca");

            saveUser("Romek",
                    "Bromek",
                    ROMEK_BROMEK_EXAMPLE_EMAIL,
                    "321-321-321",
                    TEMP_PASSWORD,
                    LocalDate.of(1989, 1, 1),
                    "Kraków",
                    "5",
                    "09123123231",
                    "22-222",
                    "Tuska");

            saveUser("Kasia",
                    "Zpodlasia",
                    KASIA_ZPODLASIA_EXAMPLE_EMAIL,
                    "333-222-111",
                    TEMP_PASSWORD,
                    LocalDate.of(1975, 1, 1),
                    "Kraków",
                    "8",
                    "19231223234",
                    "11-111",
                    "Kaczyńska");

            saveUser("Wacław",
                    "Kumaty",
                    WACLAW_KUMATY_EXAMPLE_EMAIL,
                    "333-222-999",
                    TEMP_PASSWORD,
                    LocalDate.of(1964, 1, 1),
                    "Kraków",
                    "15",
                    "19231222224",
                    "91-231",
                    "Grodzka");

            saveUser("Tristan",
                    "Pakistanczyk",
                    TRISTAN_PAKISTANCZYK_EXAMPLE_EMAIL,
                    "333-999-111",
                    TEMP_PASSWORD,
                    LocalDate.of(1965, 1, 1),
                    "Kraków",
                    "66",
                    "66661223234",
                    "23-323",
                    "Różowa");

            saveUser("Paulina",
                    "Rekin",
                    PAULINA_REKIN_EXAMPLE_EMAIL,
                    "393-999-111",
                    TEMP_PASSWORD,
                    LocalDate.of(1992, 1, 1),
                    "Kraków",
                    "11",
                    "92233232341",
                    "84-343",
                    "Schetyńska");
        }
    }

    private void saveUser(String firstName,
                          String lastName,
                          String email,
                          String phone,
                          String password,
                          LocalDate birthDate,
                          String city,
                          String houseNumber,
                          String pesel,
                          String postalCode,
                          String street) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));
        Role userRole = roleRepository.findByName(RoleName.ROLE_PATIENT).orElseThrow(() -> new AppException("User Role not set."));
        user.setRoles(Collections.singleton(userRole));

        Patient patient = new Patient();
        patient.setBirthDate(birthDate);
        patient.setCity(city);
        patient.setHouseNumber(houseNumber);
        patient.setPesel(pesel);
        patient.setPostalCode(postalCode);
        patient.setStreet(street);
        patient.setUser(user);
        patientRepository.save(patient);
    }
}
