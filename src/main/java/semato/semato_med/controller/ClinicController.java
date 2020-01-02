package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.model.Clinic;
import semato.semato_med.payload.clinic.ClinicResponse;
import semato.semato_med.payload.clinic.ClinicUpdateRequest;
import semato.semato_med.payload.clinic.ClinicAddingRequest;
import semato.semato_med.service.ClinicManagementService;


@RestController
@Secured({"ROLE_ADMIN"})
@RequestMapping("/api/clinic")
public class ClinicController {


    @Autowired
    private ClinicManagementService clinicService;


    @PutMapping("/add")
    public ResponseEntity<?> addClinic(@RequestBody ClinicAddingRequest clinicAddingRequest) {
        return new ResponseEntity<>(clinicService.add(clinicAddingRequest), HttpStatus.CREATED);
    }

    @GetMapping("/get/{clinicId}")
    public ResponseEntity<?> getClinicById(@PathVariable Long clinicId) {
        ClinicResponse clinic = clinicService.get(clinicId);
        if (clinic != null) {
            return new ResponseEntity<>(clinic, HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllClinics() {
        return new ResponseEntity<>(clinicService.getAll(), HttpStatus.OK);
    }

    @DeleteMapping("/del/{clinicId}")
    public ResponseEntity<?> deleteClinicById(@PathVariable Long clinicId) {
        if (clinicService.delete(clinicId)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/update/{clinicId}")
    public ResponseEntity<?> updateNotification(
            @PathVariable Long clinicId,
            @RequestBody ClinicUpdateRequest clinicUpdateRequest) {

        Clinic clinic = clinicService.update(clinicUpdateRequest, clinicId);
        if (clinic != null) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
