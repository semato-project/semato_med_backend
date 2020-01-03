package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.model.*;
import semato.semato_med.payload.ApiResponse;
import semato.semato_med.payload.user.PatientUpdateRequest;
import semato.semato_med.payload.visit.*;
import semato.semato_med.repository.ClinicRepository;
import semato.semato_med.repository.PatientRepository;
import semato.semato_med.repository.VisitRepository;
import semato.semato_med.security.CurrentUser;
import semato.semato_med.security.UserPrincipal;
import semato.semato_med.service.EmailSender;
import semato.semato_med.service.VisitService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;


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
    private PatientRepository patientRepository;

    @GetMapping("/visit/list/get")
    public VisitListResponse getVisitList(@CurrentUser UserPrincipal userPrincipal) {

        Patient patient = userPrincipal.getUser().getPatient();
        ArrayList<Visit> visitList = visitRepository.findByPatientId(patient.getId()).get();

        return new VisitListResponse(visitList);
    }

    @GetMapping("/clinic/list/get")
    public ClinicListResponse getClinicList() {
        return new ClinicListResponse(clinicRepository.findAll());
    }

    @PostMapping("/user/edit")
    public ResponseEntity<?> editProfile(@RequestBody PatientUpdateRequest patientUpdateRequest, @CurrentUser UserPrincipal userPrincipal) {

        Optional<Patient> patient = patientRepository.findPatientByUser(userPrincipal.getUser());
        if(patient.isPresent()) {
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
}
