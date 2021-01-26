package semato.semato_med.mother;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import semato.semato_med.model.Physician;
import semato.semato_med.model.User;
import java.util.UUID;

@Service
public class PhysicianMother {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Physician get() {

        UUID uuid = UUID.randomUUID();

        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail(uuid.toString() + "@example.com");
        user.setPhone("phoneNumber");
        user.setPassword(passwordEncoder.encode("password"));

        Physician physician = new Physician();
        physician.setMedicalDegrees("medicalDegrees");
        physician.setNote("note");
        physician.setTitle("title");
        physician.setImage_url("imageUrl");

        physician.setUser(user);
        return physician;
    }
}
