package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.model.Clinic;
import semato.semato_med.payload.ApiResponse;
import semato.semato_med.payload.clinicMgmt.ClinicResponse;
import semato.semato_med.payload.clinicMgmt.ClinicUpdateRequest;
import semato.semato_med.payload.clinicMgmt.ClinicAddingRequest;
import semato.semato_med.repository.ClinicRepository;
import semato.semato_med.service.ClinicManagementService;

import java.util.Optional;


@RestController
@Secured({"ROLE_ADMIN"})
@RequestMapping("/api/mgmt/clinic")
public class ClinicManagementController {

    private final ResponseEntity notFoundResponse = new ResponseEntity<>(new ApiResponse(false, "Clinic not found!"), HttpStatus.NOT_FOUND);

    private final ClinicManagementService clinicService;

    private final ClinicRepository clinicRepository;

    public ClinicManagementController(ClinicManagementService clinicService, ClinicRepository clinicRepository) {
        this.clinicService = clinicService;
        this.clinicRepository = clinicRepository;
    }

    @PutMapping("/add")
    public ResponseEntity<?> addClinic(@RequestBody ClinicAddingRequest clinicAddingRequest) {
        Optional<Clinic> clinicOptional = clinicRepository.findByEmail(clinicAddingRequest.getEmail());
        if (!clinicOptional.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(false, "This clinic already exists!"), HttpStatus.BAD_REQUEST);
        }

        clinicService.add(clinicAddingRequest);
        return new ResponseEntity<>(new ApiResponse(true, "Clinic has been added!"), HttpStatus.CREATED);
    }

    @GetMapping("/get/{clinicId}")
    public ResponseEntity<?> getClinicById(@PathVariable Long clinicId) {
        Optional<Clinic> clinicOptional = clinicRepository.findById(clinicId);
        return clinicOptional.map(clinic -> new ResponseEntity<>(new ClinicResponse(clinic), HttpStatus.OK)).orElse(notFoundResponse);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllClinics() {
        return new ResponseEntity<>(clinicService.getAll(), HttpStatus.OK);
    }

    @DeleteMapping("/del/{clinicId}")
    public ResponseEntity<?> deleteClinicById(@PathVariable Long clinicId) {
        Optional<Clinic> clinicOptional = clinicRepository.findById(clinicId);
        if (!clinicOptional.isPresent()) {
            return notFoundResponse;
        }
        clinicService.delete(clinicOptional.get());
        return new ResponseEntity<>(new ApiResponse(true, "Clinic deleted successfully"), HttpStatus.OK);
}

    @PostMapping("/update/{clinicId}")
    public ResponseEntity<?> updateNotification(
            @PathVariable Long clinicId,
            @RequestBody ClinicUpdateRequest clinicUpdateRequest) {

        Optional<Clinic> clinicOptional = clinicRepository.findById(clinicId);
        if (!clinicOptional.isPresent()) {
            return notFoundResponse;
        }

        clinicService.update(clinicOptional.get(), clinicUpdateRequest);
        return new ResponseEntity<>(new ApiResponse(true, "Clinic updated successfully"), HttpStatus.OK);
    }
}
