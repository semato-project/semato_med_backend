package semato.semato_med.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import semato.semato_med.model.Patient;
import semato.semato_med.model.Physician;
import semato.semato_med.model.Visit;
import semato.semato_med.model.VisitStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    Optional<ArrayList<Visit>> findByPatient(Patient patient);
    Optional<ArrayList<Visit>> findByPhysician(Physician physician);

    ArrayList<Visit> findByRatingIsNotNullAndPhysician(Physician physician);

    @Query("SELECT v FROM Visit v WHERE v.physician = :physician AND v.dateTimeEnd > :dateTime AND v.status <> :omitStatus")
    Optional<ArrayList<Visit>> findByPhysicianAfterDate(@Param("physician") Physician physician, @Param("dateTime") LocalDateTime dateTime,  @Param("omitStatus") VisitStatus omitStatus);

    @Query("SELECT v FROM Visit v WHERE v.patient = :patient AND v.dateTimeStart < :dateTime AND v.status <> :omitStatus")
    Optional<ArrayList<Visit>> findByPatientBeforeDate(@Param("patient") Patient patient, @Param("dateTime") LocalDateTime dateTime, @Param("omitStatus") VisitStatus omitStatus);

    @Query("SELECT v FROM Visit v WHERE v.patient = :patient AND v.dateTimeStart > :dateTime AND v.status <> :omitStatus")
    Optional<ArrayList<Visit>> findByPatientAfterDate(@Param("patient") Patient patient, @Param("dateTime") LocalDateTime dateTime, @Param("omitStatus") VisitStatus omitStatus);

    @Query("SELECT v FROM Visit v WHERE v.dateTimeStart >= :timeFrameStart AND v.dateTimeStart <= :timeFrameEnd AND v.status = :status")
    Optional<ArrayList<Visit>> findVisitsByStartTimeFrameAndStatus(@Param("timeFrameStart") LocalDateTime timeFrameStart, @Param("timeFrameEnd") LocalDateTime timeFrameEnd, @Param("status") VisitStatus status);

}
