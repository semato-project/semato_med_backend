package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.model.Clinic;
import semato.semato_med.model.Physician;
import semato.semato_med.model.Speciality;
import semato.semato_med.payload.visit.*;
import semato.semato_med.repository.ClinicRepository;
import semato.semato_med.repository.PhysicianRepository;
import semato.semato_med.repository.SpecialityRepository;
import semato.semato_med.service.VisitService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/visit")
public class VisitController {

    @Autowired
    private SpecialityRepository specialityRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private VisitService visitService;

    @GetMapping("/speciality/list/get")
    @PreAuthorize("hasRole('PATIENT')")
    public SpecialityListResponse getSpecialityList() {
        return new SpecialityListResponse(specialityRepository.findAll());
    }

    @GetMapping("/clinic/list/by/speciality/get")
    @PreAuthorize("hasRole('PATIENT')")
    public ClinicListResponse getClinicListBySpeciality(@Valid @RequestBody ClinicListBySpecialityRequest request) {
        return new ClinicListResponse(visitService.getClinicListBySpeciality(specialityRepository.findById(request.getSpecialityId()).get()));
    }

    @GetMapping("/physician/list/by/speciality/and/clinic/get")
    @PreAuthorize("hasRole('PATIENT')")
    public PhysicianListResponse getPhysicianListBySpecialityAndClinic(@Valid @RequestBody PhysicianListBySpecialityAndClinicRequest request) {

        Clinic clinic = null;
        Long clinicId = request.getClinicId();

        if (clinicId != null) {
            clinic = clinicRepository.findById(clinicId).get();
        }

        return new PhysicianListResponse(
            visitService.getPhysicianListBySpecialityAndClinic(
                specialityRepository.findById(request.getSpecialityId()).get(),
                clinic
            )
        );
    }

    @GetMapping("/available/list/get")
    @PreAuthorize("hasRole('PATIENT')")
    public VisitListResponse getAvailableVisitList(@Valid @RequestBody AvailableVisitListRequest request) {

        Clinic clinic = null;
        Physician physician = null;

        Long clinicId = request.getClinicId();
        Long physicianId = request.getPhysicianId();

        if (clinicId != null) {
            clinic = clinicRepository.findById(clinicId).get();
        }

        if (physicianId != null) {
            physician = physicianRepository.findById(physicianId).get();
        }

        return new VisitListResponse(
                visitService.getAvailableVisitList(
                        specialityRepository.findById(request.getSpecialityId()).get(),
                        request.getPeriodStart(),
                        request.getPeriodEnd(),
                        clinic,
                        physician
                )
        );
    }

    @PutMapping("/book")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PATIENT')")
    public void bookVisit(@Valid @RequestBody BookVisitRequest request) {

        Clinic clinic = clinicRepository.findById(request.getClinicId()).get();
        Physician physician = physicianRepository.findById(request.getPhysicianId()).get();
        Speciality speciality = specialityRepository.findById(request.getSpecialityId();

        visitService.bookVisit(speciality, request.getDateTimeStart(), request.getDateTimeEnd(), clinic, physician);
    }


}
