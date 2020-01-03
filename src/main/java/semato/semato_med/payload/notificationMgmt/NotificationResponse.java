package semato.semato_med.payload.notificationMgmt;

import lombok.Getter;
import semato.semato_med.model.Notification;

import java.time.LocalDate;
import java.time.ZoneId;

@Getter
public class NotificationResponse {

    private Long id;

    private String note;

    private LocalDate noteCreated;

    private LocalDate noteUpdated;

    private String userEmail;

    private String userName;

    private String userLastName;

    private String userPhone;

    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.note = notification.getNote();
        this.noteCreated = notification.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate();
        this.noteUpdated = notification.getUpdatedAt().atZone(ZoneId.systemDefault()).toLocalDate();
        this.userEmail = notification.getCreatedBy().getUser().getEmail();
        this.userName = notification.getCreatedBy().getUser().getFirstName();
        this.userLastName = notification.getCreatedBy().getUser().getLastName();
        this.userPhone = notification.getCreatedBy().getUser().getPhone();
    }
}

