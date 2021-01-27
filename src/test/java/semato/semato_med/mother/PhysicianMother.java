package semato.semato_med.mother;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import semato.semato_med.exception.AppException;
import semato.semato_med.model.*;
import semato.semato_med.repository.RoleRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class PhysicianMother {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Physician createWithSpeciality(Speciality speciality, RoleRepository roleRepository) {

        UUID uuid = UUID.randomUUID();

        Role userRole = roleRepository.findByName(RoleName.ROLE_PHYSICIAN).orElseThrow(() -> new AppException("Phisician Role not set."));

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail(uuid.toString() + "@example.com");
        user.setPhone("phoneNumber");
        user.setPassword(passwordEncoder.encode("password"));

        user.setRoles(Collections.singleton(userRole));

        Physician physician = new Physician();
        physician.setMedicalDegrees("medicalDegrees");
        physician.setNote("note");
        physician.setTitle("title");
        physician.setImage_url("imageUrl");

        Set<Speciality> specialitySet = new HashSet<>();
        specialitySet.add(speciality);
        physician.setSpecialitySet(specialitySet);

        physician.setUser(user);
        return physician;
    }
}
