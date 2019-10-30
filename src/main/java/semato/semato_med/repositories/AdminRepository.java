package semato.semato_med.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semato.semato_med.models.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

}
