package semato.semato_med.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import semato.semato_med.repository.SpecialityRepository;

@Component
public class SpecialityLoader implements ApplicationRunner {

    @Autowired
    private SpecialityRepository SpecialityRepository;

    @Override
    public void run(ApplicationArguments args) {

    }

}
