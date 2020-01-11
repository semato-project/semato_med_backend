package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.exception.ResourceNotFoundException;
import semato.semato_med.model.*;
import semato.semato_med.payload.ApiResponse;
import semato.semato_med.payload.user.PatientUpdateRequest;
import semato.semato_med.payload.user.SendFeedbackRequest;
import semato.semato_med.payload.visit.*;
import semato.semato_med.repository.AdminRepository;
import semato.semato_med.repository.ClinicRepository;
import semato.semato_med.repository.PatientRepository;
import semato.semato_med.repository.VisitRepository;
import semato.semato_med.security.CurrentUser;
import semato.semato_med.security.UserPrincipal;
import semato.semato_med.service.EmailSender;
import semato.semato_med.service.SoftDeleteService;
import semato.semato_med.util.VisitComparator;

import java.time.LocalDateTime;
import java.util.*;


@RestController
@PreAuthorize("hasRole('PATIENT')")
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private SoftDeleteService softDeleteService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/visit/list/get")
    public VisitListResponse getVisitList(@CurrentUser UserPrincipal userPrincipal, @RequestParam Optional<VisitListMode> mode) {

        Patient patient = userPrincipal.getUser().getPatient();
        ArrayList<Visit> visitList;

        try {

            if (!mode.isPresent()) {
                visitList = visitRepository.findByPatient(patient).get();
            } else {
                LocalDateTime now = LocalDateTime.now();
                switch (mode.get()) {
                    case past:
                        visitList = visitRepository.findByPatientBeforeDate(patient, now, VisitStatus.CANCELED).get();
                        break;
                    case future:
                        visitList = visitRepository.findByPatientAfterDate(patient, now, VisitStatus.CANCELED).get();
                        break;
                    default:
                        throw new ResourceNotFoundException("VisitListMode", "mode", mode.get());
                }
            }

            visitList.sort(new VisitComparator());

        } catch (NoSuchElementException e) {
            visitList = null;
        }

        return new VisitListResponse(visitList);
    }

    private enum VisitListMode {
        future,
        past
    }

    @GetMapping("/clinic/list/get")
    public ClinicListResponse getClinicList() {
        return new ClinicListResponse(clinicRepository.findAll());
    }

    @PostMapping("/user/edit")
    public ResponseEntity<?> editProfile(@RequestBody PatientUpdateRequest
                                                 patientUpdateRequest, @CurrentUser UserPrincipal userPrincipal) {

        Optional<Patient> patient = patientRepository.findPatientByUser(userPrincipal.getUser());
        if (patient.isPresent()) {
            Optional.ofNullable(patientUpdateRequest.getPostalCode()).ifPresent(patient.get()::setPostalCode);
            Optional.ofNullable(patientUpdateRequest.getCity()).ifPresent(patient.get()::setCity);
            Optional.ofNullable(patientUpdateRequest.getStreet()).ifPresent(patient.get()::setStreet);
            Optional.ofNullable(patientUpdateRequest.getHouseNumber()).ifPresent(patient.get()::setHouseNumber);
            Optional.ofNullable(patientUpdateRequest.getFirstName()).ifPresent(patient.get().getUser()::setFirstName);
            Optional.ofNullable(patientUpdateRequest.getLastName()).ifPresent(patient.get().getUser()::setLastName);
            Optional.ofNullable(patientUpdateRequest.getPhone()).ifPresent(patient.get().getUser()::setPhone);
            patientRepository.save(patient.get());
            return new ResponseEntity<>(new ApiResponse(true, "Profile has been updated!"), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ApiResponse(false, "Something goes wrong!"), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/account/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@CurrentUser UserPrincipal userPrincipal) {

        Patient patient = userPrincipal.getUser().getPatient();
        softDeleteService.deletePatient(patient);
    }

    @PutMapping("/feedback/send")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendFeedback(@RequestBody SendFeedbackRequest sendFeedbackRequest, @CurrentUser UserPrincipal userPrincipal) {

        Patient patient = userPrincipal.getUser().getPatient();

        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Zgłoszenie użytkownika: " + patient.getUser().getEmail() + "; temat: " + sendFeedbackRequest.getSubject());
        email.setText(sendFeedbackRequest.getContent());
        email.setFrom("SematoMedClinic");

        List<Admin> adminList = adminRepository.findAll();

        for (Admin admin: adminList) {
            email.setTo(admin.getUser().getEmail());
                emailSender.send(email);
        }
    }

}
