package semato.semato_med.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semato.semato_med.model.Admin;
import semato.semato_med.model.Notification;
import semato.semato_med.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification addNote(String note, Admin admin){
        Notification notification = new Notification();
        notification.setNote(note);
        notification.setCreatedBy(admin);
        return notificationRepository.save(notification);
    }
}
