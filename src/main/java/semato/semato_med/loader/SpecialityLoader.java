package semato.semato_med.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import semato.semato_med.model.Speciality;
import semato.semato_med.repository.SpecialityRepository;

@Profile("dev")
@Component
@Order(1)
public class SpecialityLoader implements ApplicationRunner {

    public static final String[] specialityArray = {
            "Kardiologia",
            "Optyka",
            "Ortopedia",
            "Stomatologia",
            "Neurochirurgia",
    };

    @Autowired
    private SpecialityRepository specialityRepository;

    public void run(ApplicationArguments args) {

        for (String specialityName: specialityArray) {
            if (! specialityRepository.findByName(specialityName).isPresent()) {
                Speciality newSpeciality = new Speciality();
                newSpeciality.setName(specialityName);
                specialityRepository.save(newSpeciality);
            }
        }


    }

}
