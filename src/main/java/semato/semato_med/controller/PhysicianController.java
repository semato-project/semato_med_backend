package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semato.semato_med.model.Patient;
import semato.semato_med.model.Physician;
import semato.semato_med.model.Visit;
import semato.semato_med.payload.visit.ClinicListResponse;
import semato.semato_med.payload.visit.VisitListResponse;
import semato.semato_med.repository.ClinicRepository;
import semato.semato_med.repository.VisitRepository;
import semato.semato_med.security.CurrentUser;
import semato.semato_med.security.UserPrincipal;
import semato.semato_med.service.EmailSender;

import java.util.ArrayList;


@RestController
@RequestMapping("/api/physician")
public class PhysicianController {

    @Autowired
    private VisitRepository visitRepository;

    @GetMapping("/visit/list/get")
    @PreAuthorize("hasRole('ROLE_PHYSICIAN')")
    public VisitListResponse getVisitList(@CurrentUser UserPrincipal userPrincipal) {

        Physician physician = userPrincipal.getUser().getPhysician();
        ArrayList<Visit> visitList = visitRepository.findByPhysician(physician).get();

        return new VisitListResponse(visitList);
    }



}
