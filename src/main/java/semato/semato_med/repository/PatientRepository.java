package semato.semato_med.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import semato.semato_med.model.Patient;
import semato.semato_med.model.Physician;
import semato.semato_med.model.User;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT p FROM Patient p INNER JOIN p.user u WHERE u.email = :email")
    Optional<Patient> findOneByEmail(@Param("email") String email);

    Optional<Patient> findPatientByUser(User user);
}
