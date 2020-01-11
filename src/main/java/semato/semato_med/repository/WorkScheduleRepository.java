package semato.semato_med.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semato.semato_med.model.Physician;
import semato.semato_med.model.WorkSchedule;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {

    Optional<WorkSchedule> findOneByPhysician(Physician physician);

    List<WorkSchedule> findByPhysician(Physician physician);

}
