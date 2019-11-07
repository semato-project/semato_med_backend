package semato.semato_med.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import semato.semato_med.model.Role;
import semato.semato_med.model.RoleName;
import semato.semato_med.repository.RoleRepository;

@Component
public class RoleLoader implements ApplicationRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) {
        roleRepository.deleteAll();
        roleRepository.save(new Role(RoleName.ROLE_PATIENT));
        roleRepository.save(new Role(RoleName.ROLE_ADMIN));
        roleRepository.save(new Role(RoleName.ROLE_PHYSICIAN));
    }

}
