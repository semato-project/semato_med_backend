package semato.semato_med.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import semato.semato_med.model.Patient;
import semato.semato_med.model.Physician;
import semato.semato_med.model.Visit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    Optional<ArrayList<Visit>> findByPatient(Patient patient);
    Optional<ArrayList<Visit>> findByPhysician(Physician physician);

    @Query("SELECT v FROM Visit v WHERE v.patient = :patient AND v.dateTimeStart < :dateTime")
    Optional<ArrayList<Visit>> findByPatientBeforeDate(@Param("patient") Patient patient, @Param("dateTime") LocalDateTime dateTime);

    @Query("SELECT v FROM Visit v WHERE v.patient = :patient AND v.dateTimeStart > :dateTime")
    Optional<ArrayList<Visit>> findByPatientAfterDate(@Param("patient") Patient patient, @Param("dateTime") LocalDateTime dateTime);

}
