package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.model.Patient;
import semato.semato_med.model.Physician;
import semato.semato_med.model.Visit;
import semato.semato_med.model.VisitStatus;
import semato.semato_med.payload.ApiResponse;
import semato.semato_med.payload.user.PatientUpdateRequest;
import semato.semato_med.payload.user.PhysicianUpdateRequest;
import semato.semato_med.payload.visit.ClinicListResponse;
import semato.semato_med.payload.visit.VisitListResponse;
import semato.semato_med.repository.ClinicRepository;
import semato.semato_med.repository.PhysicianRepository;
import semato.semato_med.repository.VisitRepository;
import semato.semato_med.security.CurrentUser;
import semato.semato_med.security.UserPrincipal;
import semato.semato_med.service.EmailSender;
import semato.semato_med.util.VisitComparator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;


@RestController
@PreAuthorize("hasRole('ROLE_PHYSICIAN')")
@RequestMapping("/api/physician")
public class PhysicianController {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @GetMapping("/visit/list/get")
    public VisitListResponse getVisitList(@CurrentUser UserPrincipal userPrincipal) {

        LocalDateTime now = LocalDateTime.now();

        Physician physician = userPrincipal.getUser().getPhysician();
        ArrayList<Visit> visitList = visitRepository.findByPhysicianAfterDate(physician, now, VisitStatus.CANCELED).get();

        visitList.sort(new VisitComparator());

        return new VisitListResponse(visitList);
    }

    @PostMapping("/user/edit")
    public ResponseEntity<?> editProfile(@RequestBody PhysicianUpdateRequest physicianUpdateRequest, @CurrentUser UserPrincipal userPrincipal) {

        Optional<Physician> physician= physicianRepository.findPhysicianByUser(userPrincipal.getUser());
        if(physician.isPresent()) {
            Optional.ofNullable(physicianUpdateRequest.getFirstName()).ifPresent(physician.get().getUser()::setFirstName);
            Optional.ofNullable(physicianUpdateRequest.getLastName()).ifPresent(physician.get().getUser()::setLastName);
            Optional.ofNullable(physicianUpdateRequest.getPhone()).ifPresent(physician.get().getUser()::setPhone);
            Optional.ofNullable(physicianUpdateRequest.getMedicalDegrees()).ifPresent(physician.get()::setMedicalDegrees);
            Optional.ofNullable(physicianUpdateRequest.getNote()).ifPresent(physician.get()::setNote);
            Optional.ofNullable(physicianUpdateRequest.getTitle()).ifPresent(physician.get()::setTitle);
            Optional.ofNullable(physicianUpdateRequest.getImage_url()).ifPresent(physician.get()::setImage_url);
            physicianRepository.save(physician.get());
            return new ResponseEntity<>(new ApiResponse(true, "Profile has been updated!"), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ApiResponse(false, "Something goes wrong!"), HttpStatus.BAD_REQUEST);
    }

}
