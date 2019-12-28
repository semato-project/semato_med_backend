package semato.semato_med.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semato.semato_med.model.Admin;
import semato.semato_med.model.Notification;
import semato.semato_med.payload.notification.NotificationResponse;
import semato.semato_med.repository.NotificationRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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

    public NotificationResponse createNotificationResponse(Notification notification){
        return new NotificationResponse(
                notification.getId(),
                notification.getNote(),
                notification.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate(),
                notification.getUpdatedAt().atZone(ZoneId.systemDefault()).toLocalDate(),
                notification.getCreatedBy().getUser().getEmail(),
                notification.getCreatedBy().getUser().getFirstName(),
                notification.getCreatedBy().getUser().getLastName(),
                notification.getCreatedBy().getUser().getPhone());
    }

    public List<NotificationResponse> createNotificationResponseList(List<Notification> notifications){
        List<NotificationResponse> notificationResponseList = new ArrayList<>();
        for (Notification notification : notifications) {
            notificationResponseList.add(createNotificationResponse(notification));
        }
        return notificationResponseList;
    }

    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
    }
}
