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

    public static final String CENTRUM_ZDROWIA_SZCZESCIA_I_POMYSLNOSCI_EXAMPLE_EMAIL = "centrum_zdrowia_szczescia_i_pomyslnosci@example.com";
    public static final String ARKHAM_EMAIL= "centrum_zdrowia_szczescia_i_pomyslnosci@example.com";
    public static final String LESNA_GORA_EMAIL= "lesna_gora@example.com";


    @Autowired
    private ClinicRepository clinicRepository;

    @Override
    public void run(ApplicationArguments args) {

        addClinic(
                "Kraków",
                "Polska",
                CENTRUM_ZDROWIA_SZCZESCIA_I_POMYSLNOSCI_EXAMPLE_EMAIL,
                "Centrum zdrowia, szczęścia i pomyślności w Krakowie",
                "22",
                "Ofiar służby zdrowia",
                "https://upload.wikimedia.org/wikipedia/commons/6/69/The_Westminster_Hospital%2C_London._Engraving._Wellcome_V0013809.jpg",
                50.0532811F,
                19.9437568F,
                "33-333"
        );

        addClinic(
                "Kraków",
                "Polska",
                ARKHAM_EMAIL,
                "Azyl Arkham",
                "66",
                "Jamesa Gordona",
                "https://upload.wikimedia.org/wikipedia/en/b/b0/Arkham-Asylum.jpg",
                50.0368691F,
                19.9395649F,
                "55-555"
        );

        addClinic(
                "Kraków",
                "Polska",
                LESNA_GORA_EMAIL,
                "Szpital w Leśnej Górze oddział Kraków",
                "67",
                "Leśnogórska",
                "https://i.iplsc.com/elewacja-po-zmianach-tak-wyglada-szpital-kliniczny-w-lesnej-/0004E6OPAJUPXL78-C122-F4.jpg",
                50.0333321F,
                19.9434231F,
                "77-777"
        );
    }

    private void addClinic(
            String city,
            String country,
            String email,
            String name,
            String houseNumber,
            String street,
            String imageUrl,
            float latitude,
            float longitude,
            String postalCode

    ) {
        if (! clinicRepository.findByEmail(email).isPresent()) {

            Clinic clinic = new Clinic();
            clinic.setCity(city);
            clinic.setCountry(country);
            clinic.setEmail(email);
            clinic.setName(name);
            clinic.setHouseNumber(houseNumber);
            clinic.setStreet(street);
            clinic.setImageUrl(imageUrl);
            clinic.setLatitude(latitude);
            clinic.setLongitude(longitude);
            clinic.setPostalCode(postalCode);

            clinicRepository.save(clinic);
        }
    }
}
