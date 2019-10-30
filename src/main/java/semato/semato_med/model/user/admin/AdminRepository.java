package semato.semato_med.model.user.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semato.semato_med.model.user.admin.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

}
