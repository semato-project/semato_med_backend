package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.model.Visit;
import semato.semato_med.payload.ApiResponse;
import semato.semato_med.payload.visitMgmt.VisitResponse;
import semato.semato_med.payload.visitMgmt.VisitUpdateRequest;
import semato.semato_med.repository.VisitRepository;
import semato.semato_med.service.VisitManagementService;

import java.util.List;
import java.util.Optional;

@RestController
@Secured({"ROLE_ADMIN"})
@RequestMapping("/api/mgmt/visit")
public class VisitManagementController {

    private final ResponseEntity<?> notFoundResponse = new ResponseEntity<>(new ApiResponse(false, "Visit not found"), HttpStatus.NOT_FOUND);

    @Autowired
    private VisitManagementService visitManagementService;

    @Autowired
    private VisitRepository visitRepository;

    @GetMapping("/get/{visitId}")
    public ResponseEntity<?> getVisit(@PathVariable Long visitId) {
        Optional<Visit> visitOptional = visitRepository.findById(visitId);

        return visitOptional.<ResponseEntity<?>>map(visit -> new ResponseEntity<>(new VisitResponse(visit), HttpStatus.OK)).orElse(notFoundResponse);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllVisits() {
        List<VisitResponse> visitList = visitManagementService.getAll();
        return new ResponseEntity<>(visitList, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{visitId}")
    public ResponseEntity<?> deleteVisitById(@PathVariable Long visitId) {

        Optional<Visit> visit = visitRepository.findById(visitId);
        if (!visit.isPresent()) {
            return notFoundResponse;
        }
        visitManagementService.delete(visit.get());
        return new ResponseEntity<>(new ApiResponse(true, "Visit has been canceled"), HttpStatus.OK);
    }

    @PostMapping("/update/{visitId}")
    public ResponseEntity<?> updateVisit(@PathVariable Long visitId, @RequestBody VisitUpdateRequest visitUpdateRequest){
        Optional<Visit> visit = visitRepository.findById(visitId);
        if(!visit.isPresent()){
            return notFoundResponse;
        }
        visitManagementService.update(visit.get(), visitUpdateRequest);
        return new ResponseEntity<>(new ApiResponse(true, "Visit has been updated"), HttpStatus.OK);
    }

}
