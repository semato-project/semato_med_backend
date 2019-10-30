package semato.semato_med;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import semato.semato_med.models.Admin;
import semato.semato_med.models.User;
import semato.semato_med.repositories.AdminRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AdminRepositoryTest {

    @Autowired
    AdminRepository adminRepository;

    @Test
    public void addNewAdmins(){
        adminRepository.save(new Admin(new User("test@test.pl", "Roman", "Polanski" ,"123123123")));
        adminRepository.save(new Admin(new User("test2@test.pl", "Małgorzata", "Małolepsza","123123123")));
        adminRepository.save(new Admin(new User("test3@test.pl", "Jaś", "Słaby","123123123")));

        assertNotNull(adminRepository.findAll());
    }
}
