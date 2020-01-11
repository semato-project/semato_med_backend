package semato.semato_med.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import semato.semato_med.model.Admin;
import semato.semato_med.model.Notification;
import semato.semato_med.repository.AdminRepository;
import semato.semato_med.repository.NotificationRepository;

@Component
@Order(5)
public class NotificationLoader /*implements ApplicationRunner*/ {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AdminRepository adminRepository;

    public void run(ApplicationArguments args) {

        Admin admin = adminRepository.findAll().get(0);

        addNotification(admin, "System będzie działał sprawnie w najbliższy weekend.");
        addNotification(admin, "Szczęściwego nowego roku dla wszystkich naszych użytkowników. Dużo miłości, pieniędzy i paradoksalnie zdrowia.");

    }

    public void addNotification(Admin admin, String note) {

        Notification notification = new Notification();
        notification.setCreatedBy(admin);
        notification.setNote(note);

        notificationRepository.save(notification);

    }

}
