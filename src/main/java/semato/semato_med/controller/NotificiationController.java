package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.model.Admin;
import semato.semato_med.model.Notification;
import semato.semato_med.payload.notification.AddNotificationRequest;
import semato.semato_med.payload.notification.NotificationResponse;
import semato.semato_med.repository.AdminRepository;
import semato.semato_med.repository.NotificationRepository;
import semato.semato_med.security.CurrentUser;
import semato.semato_med.security.UserPrincipal;
import semato.semato_med.service.NotificationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@Secured({"ROLE_ADMIN"})
@RequestMapping("/api/notification")
public class NotificiationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AdminRepository adminRepository;

    @PutMapping("/add")
    public ResponseEntity<?> addNotification(@RequestBody AddNotificationRequest notificationRequest, @CurrentUser UserPrincipal userPrincipal) {
        Optional<Admin> admin = adminRepository.findById(userPrincipal.getUser().getId());
        Notification notification = notificationService.addNote(notificationRequest.getNote(), admin.get());
        if(notification != null) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/get/{notificationId}")
    public ResponseEntity<?> getNotificationById(@PathVariable Long notificationId){
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if(notification.isPresent()){
            NotificationResponse notificationResponse = notificationService.createNotificationResponse(notification.get());
            return new ResponseEntity<>(notificationResponse, HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllNotification(){
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationResponse> notificationResponseList = notificationService.createNotificationResponseList(notifications);
        return new ResponseEntity<>(notificationResponseList, HttpStatus.OK);
    }
}


