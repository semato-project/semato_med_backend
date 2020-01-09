package semato.semato_med.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.exception.ResourceNotFoundException;
import semato.semato_med.model.*;
import semato.semato_med.payload.visit.*;
import semato.semato_med.repository.ClinicRepository;
import semato.semato_med.repository.PhysicianRepository;
import semato.semato_med.repository.SpecialityRepository;
import semato.semato_med.repository.VisitRepository;
import semato.semato_med.security.CurrentUser;
import semato.semato_med.security.UserPrincipal;
import semato.semato_med.service.EmailSender;
import semato.semato_med.service.VisitService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

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

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private VisitRepository visitRepository;

    @GetMapping("/speciality/list/get")
    @PreAuthorize("hasRole('PATIENT')")
    public SpecialityListResponse getSpecialityList() {
        return new SpecialityListResponse(specialityRepository.findAll());
    }

    @GetMapping("/clinic/list/by/speciality/get")
    @PreAuthorize("hasRole('PATIENT')")
    public ClinicListResponse getClinicListBySpeciality(@RequestParam Long specialityId) {
        return new ClinicListResponse(visitService.getClinicListBySpeciality(specialityRepository.findById(specialityId).get()));
    }

    @GetMapping("/physician/list/by/speciality/and/clinic/get")
    @PreAuthorize("hasRole('PATIENT')")
    public PhysicianListResponse getPhysicianListBySpecialityAndClinic(@RequestParam Long specialityId, @RequestParam Optional<Long> clinicId) {

        Clinic clinic = null;

        if (clinicId.isPresent()) {
            clinic = clinicRepository.findById(clinicId.get()).get();
        }

        return new PhysicianListResponse(
            visitService.getPhysicianListBySpecialityAndClinic(
                specialityRepository.findById(specialityId).get(),
                clinic
            )
        );
    }

    @GetMapping("/available/list/get")
    @PreAuthorize("hasRole('PATIENT')")
    public AvailableVisitListResponse getAvailableVisitList(
            @RequestParam Long specialityId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate periodStart,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate periodEnd,
            @RequestParam Optional<Long> clinicId,
            @RequestParam Optional<Long> physicianId
    ) {

        Clinic clinic = null;
        Physician physician = null;

        if (clinicId.isPresent()) {
            clinic = clinicRepository.findById(clinicId.get()).get();
        }

        if (physicianId.isPresent()) {
            physician = physicianRepository.findById(physicianId.get()).get();
        }

        return new AvailableVisitListResponse(
                visitService.getAvailableVisitList(
                        specialityRepository.findById(specialityId).get(),
                        periodStart,
                        periodEnd,
                        clinic,
                        physician
                )
        );
    }

    @PutMapping("/book")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public void bookVisit(@Valid @RequestBody BookVisitRequest request, @CurrentUser UserPrincipal userPrincipal) {

        Clinic clinic = clinicRepository.findById(request.getClinicId()).get();
        Physician physician = physicianRepository.findById(request.getPhysicianId()).get();
        Speciality speciality = specialityRepository.findById(request.getSpecialityId()).get();

        Patient patient = userPrincipal.getUser().getPatient();

        Visit visit = visitService.bookVisitWithParams(speciality, request.getDateTimeStart(), request.getDateTimeEnd(), clinic, physician, patient);
        emailSender.send(visitService.constructConfirmationVisitEmail(patient, visit));
    }

    @DeleteMapping("/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('PATIENT')")
    public void cancelVisit(@RequestParam Long visitId) {

        Optional<Visit> visitOptional = visitRepository.findById(visitId);

        if (! visitOptional.isPresent()) {
            throw new ResourceNotFoundException("Visit", "id", visitId);
        }

        visitService.cancel(visitOptional.get());
        emailSender.send(visitService.constructCancelVisitEmail(visitOptional.get().getPatient(), visitOptional.get()));
    }


}
