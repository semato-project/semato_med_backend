package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semato.semato_med.payload.ApiResponse;
import semato.semato_med.payload.visitMgmt.VisitResponse;
import semato.semato_med.repository.VisitRepository;
import semato.semato_med.service.VisitManagementService;

import java.util.List;

@RestController
@Secured({"ROLE_ADMIN"})
@RequestMapping("/api/mgmt/visit")
public class VisitManagementController {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private VisitManagementService visitManagementService;

    @GetMapping("/get/{visitId}")
    public ResponseEntity<?> getVisit(@PathVariable Long visitId) {
        VisitResponse visit = visitManagementService.getById(visitId);
        if (visit == null) {
            return new ResponseEntity<>(new ApiResponse(false, "Visit not found"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(visit, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        List<VisitResponse> visitList = visitManagementService.getAll();

        return new ResponseEntity<>(visitList, HttpStatus.OK);
    }



}
