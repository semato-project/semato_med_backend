package semato.semato_med.mother;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import semato.semato_med.exception.AppException;
import semato.semato_med.model.*;
import semato.semato_med.repository.RoleRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

@Service
public class PatientMother {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Patient get(RoleRepository roleRepository) {

        UUID uuid = UUID.randomUUID();

        Role userRole = roleRepository.findByName(RoleName.ROLE_PATIENT).orElseThrow(() -> new AppException("Patient Role not set."));

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail(uuid.toString() + "@example.com");
        user.setPhone("phoneNumber");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(Collections.singleton(userRole));

        Patient patient = new Patient();
        patient.setBirthDate(LocalDate.now());
        patient.setCity("city");
        patient.setHouseNumber("houseNumber");
        patient.setPesel(uuid.toString());
        patient.setPostalCode("postalCode");
        patient.setStreet("street");
        patient.setUser(user);

        return patient;


    }
}
