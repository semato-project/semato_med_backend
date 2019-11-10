package semato.semato_med.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semato.semato_med.model.WorkSchedule;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Integer> {
}
