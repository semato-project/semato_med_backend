package semato.semato_med.payload.notificationMgmt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class NotificationResponse {

    private Long id;

    private String note;

    private LocalDate noteCreated;

    private LocalDate noteUpdated;

    private String userEmail;

    private String userName;

    private String userLastName;

    private String userPhone;
}

