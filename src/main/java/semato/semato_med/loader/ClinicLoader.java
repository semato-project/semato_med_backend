package semato.semato_med.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import semato.semato_med.model.Clinic;
import semato.semato_med.repository.ClinicRepository;

@Component
@Order(1)
public class ClinicLoader implements ApplicationRunner {

    public static final String EMAIL = "centrum_zdrowia_szczescia_i_pomyslnosci@example.com";

    @Autowired
    private ClinicRepository clinicRepository;

    @Override
    public void run(ApplicationArguments args) {

        if (! clinicRepository.findByEmail(EMAIL).isPresent()) {

            Clinic clinic = new Clinic();
            clinic.setCity("Kraków");
            clinic.setCountry("Polska");
            clinic.setEmail(EMAIL);
            clinic.setName("Centrum zdrowia, szczęścia i pomyślności w Krakowie");
            clinic.setHouseNumber("22");
            clinic.setStreet("Ofiar słóżby zdrowia");
            clinic.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/6/69/The_Westminster_Hospital%2C_London._Engraving._Wellcome_V0013809.jpg");
            clinic.setLatitude(50.0532811F);
            clinic.setLongitude(19.9437568F);
            clinic.setPostalCode("33-333");

            clinicRepository.save(clinic);
        }
    }
}
