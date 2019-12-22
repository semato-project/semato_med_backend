package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.model.Notification;
import semato.semato_med.payload.notification.AddNotificationRequest;
import semato.semato_med.security.CurrentUser;
import semato.semato_med.security.UserPrincipal;
import semato.semato_med.service.NotificationService;


@RestController
@RequestMapping("/api/notification")
public class NotificiationController {

    @Autowired
    private NotificationService notificationService;

    @PutMapping("/add")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> addNotification(@RequestBody AddNotificationRequest notificationRequest, @CurrentUser UserPrincipal userPrincipal) {
        Notification notification = notificationService.addNote(notificationRequest.getNote(), userPrincipal.getUser().getAdmin());
        if(notification != null) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}


