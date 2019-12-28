package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.model.*;
import semato.semato_med.payload.visit.*;
import semato.semato_med.repository.ClinicRepository;
import semato.semato_med.repository.VisitRepository;
import semato.semato_med.security.CurrentUser;
import semato.semato_med.security.UserPrincipal;
import semato.semato_med.service.EmailSender;
import semato.semato_med.service.VisitService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedList;


@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @GetMapping("/visit/list/get")
    @PreAuthorize("hasRole('PATIENT')")
    public VisitListResponse getVisitList(@CurrentUser UserPrincipal userPrincipal) {

        Patient patient = userPrincipal.getUser().getPatient();
        ArrayList<Visit> visitList = visitRepository.findByPatientId(patient.getId()).get();

        return new VisitListResponse(visitList);
    }

    @GetMapping("/clinic/list/get")
    @PreAuthorize("hasRole('PATIENT')")
    public ClinicListResponse getClinicList() {
        return new ClinicListResponse(clinicRepository.findAll());
    }


}
