package semato.semato_med.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semato.semato_med.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

}
