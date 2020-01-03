package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.model.Admin;
import semato.semato_med.model.Notification;
import semato.semato_med.payload.ApiResponse;
import semato.semato_med.payload.notificationMgmt.NotificationRequest;
import semato.semato_med.payload.notificationMgmt.NotificationResponse;
import semato.semato_med.repository.AdminRepository;
import semato.semato_med.repository.NotificationRepository;
import semato.semato_med.security.CurrentUser;
import semato.semato_med.security.UserPrincipal;
import semato.semato_med.service.NotificationManagementService;

import java.util.List;
import java.util.Optional;


@RestController
@Secured({"ROLE_ADMIN"})
@RequestMapping("/api/mgmt/notification")
public class NotificationManagementController {

    private final ResponseEntity notFoundResponse = new ResponseEntity<>(new ApiResponse(false, "Notification not found!"), HttpStatus.NOT_FOUND);


    @Autowired
    private NotificationManagementService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AdminRepository adminRepository;

    @PutMapping("/add")
    public ResponseEntity<?> addNotification(@RequestBody NotificationRequest notificationRequest, @CurrentUser UserPrincipal userPrincipal) {
        Optional<Admin> admin = adminRepository.findById(userPrincipal.getUser().getId());
        if(admin.isPresent()) {
            notificationService.addNote(notificationRequest.getNote(), admin.get());
            return new ResponseEntity<>(new ApiResponse(true, "Note has been added!"), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(new ApiResponse(false, "Something wrong happen!"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get/{notificationId}")
    public ResponseEntity<?> getNotificationById(@PathVariable Long notificationId){
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        return notification.map(value -> new ResponseEntity<>(new NotificationResponse(value), HttpStatus.OK)).orElse(notFoundResponse);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllNotification(){
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationResponse> notificationResponseList = notificationService.createNotificationResponseList(notifications);
        return new ResponseEntity<>(notificationResponseList, HttpStatus.OK);
    }

    @DeleteMapping("/del/{notificationId}")
    public ResponseEntity<?> deleteNotificationById(@PathVariable Long notificationId){
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if(notification.isPresent()){
            notificationService.deleteNotification(notification.get());
            return new ResponseEntity<>(new ApiResponse(true, "Notification has been deleted!"), HttpStatus.OK);
        }

        return notFoundResponse;
    }

    @PostMapping("/update/{notificationId}")
    public ResponseEntity<?> updateNotification(
            @PathVariable Long notificationId,
            @RequestBody NotificationRequest notificationRequest){

        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if(!notification.isPresent()) {
            return notFoundResponse;
        }

        notificationService.updateNote(notificationRequest.getNote(), notification.get());
        return new ResponseEntity<>(new ApiResponse(true, "Notification has been updated!"), HttpStatus.OK);
    }
}


