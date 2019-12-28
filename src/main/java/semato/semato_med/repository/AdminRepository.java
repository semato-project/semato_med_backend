package semato.semato_med.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semato.semato_med.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

}
