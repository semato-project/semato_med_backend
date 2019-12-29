package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.exception.ResourceNotFoundException;
import semato.semato_med.model.*;
import semato.semato_med.payload.visit.*;
import semato.semato_med.repository.ClinicRepository;
import semato.semato_med.repository.VisitRepository;
import semato.semato_med.security.CurrentUser;
import semato.semato_med.security.UserPrincipal;
import semato.semato_med.service.EmailSender;
import semato.semato_med.service.VisitService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;


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
    public VisitListResponse getVisitList(@CurrentUser UserPrincipal userPrincipal, @RequestParam Optional<VisitListMode> mode) {

        Patient patient = userPrincipal.getUser().getPatient();
        ArrayList<Visit> visitList;

        try {

            if (! mode.isPresent()) {
                visitList = visitRepository.findByPatient(patient).get();
            } else {
                LocalDateTime now = LocalDateTime.now();
                switch (mode.get()) {
                    case past:
                        visitList = visitRepository.findByPatientBeforeDate(patient, now).get();
                        break;
                    case future:
                        visitList = visitRepository.findByPatientAfterDate(patient, now).get();
                        break;
                    default:
                        throw new ResourceNotFoundException("VisitListMode", "mode", mode.get());
                }
            }

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
    @PreAuthorize("hasRole('PATIENT')")
    public ClinicListResponse getClinicList() {
        return new ClinicListResponse(clinicRepository.findAll());
    }


}
