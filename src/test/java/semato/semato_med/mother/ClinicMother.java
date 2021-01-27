package semato.semato_med.mother;

import org.springframework.stereotype.Service;
import semato.semato_med.model.Clinic;
import java.util.UUID;

@Service
public class ClinicMother {

    public Clinic get() {
        UUID uuid = UUID.randomUUID();
        Clinic clinic = new Clinic();
        clinic.setCity("city");
        clinic.setCountry("country");
        clinic.setEmail(uuid.toString() + "@example.com");
        clinic.setName(uuid.toString());
        clinic.setHouseNumber("1");
        clinic.setStreet("street");
        clinic.setImageUrl("imageUrl");
        clinic.setLatitude(1);
        clinic.setLongitude(1);
        clinic.setPostalCode("postalCode");
        return clinic;
    }
}
