package semato.semato_med.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semato.semato_med.model.Admin;
import semato.semato_med.model.Notification;
import semato.semato_med.payload.notificationMgmt.NotificationResponse;
import semato.semato_med.repository.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationManagementService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void addNote(String note, Admin admin){
        Notification notification = new Notification();
        notification.setNote(note);
        notification.setCreatedBy(admin);
        notificationRepository.save(notification);
    }

    public List<NotificationResponse> createNotificationResponseList(List<Notification> notifications){
        List<NotificationResponse> notificationResponseList = new ArrayList<>();
        for (Notification notification : notifications) {
            notificationResponseList.add(new NotificationResponse(notification));
        }
        return notificationResponseList;
    }

    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
    }

    public void updateNote(String note, Notification notification) {
            notification.setNote(note);
            notificationRepository.save(notification);
    }
}
