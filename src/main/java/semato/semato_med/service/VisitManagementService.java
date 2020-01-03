package semato.semato_med.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semato.semato_med.model.*;
import semato.semato_med.payload.visitMgmt.VisitResponse;
import semato.semato_med.payload.visitMgmt.VisitUpdateRequest;
import semato.semato_med.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VisitManagementService {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private SpecialityRepository specialityRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    public List<VisitResponse> getAll() {
        List<Visit> visitList = visitRepository.findAll();
        List<VisitResponse> visitResponses = new ArrayList<>();
        for (Visit visit : visitList) {
            visitResponses.add(new VisitResponse(visit));
        }

        return visitResponses;
    }

    public void delete(Visit visit) {
        visit.setStatus(VisitStatus.CANCELED);
        visitRepository.save(visit);
    }

    public void update(Visit visit, VisitUpdateRequest visitUpdateRequest) {
        Optional.ofNullable(visitUpdateRequest.getStatus()).ifPresent(visit::setStatus);
        Optional.ofNullable(visitUpdateRequest.getDateTimeStart()).ifPresent(visit::setDateTimeStart);
        Optional.ofNullable(visitUpdateRequest.getDateTimeEnd()).ifPresent(visit::setDateTimeEnd);
        Optional<Speciality> speciality = specialityRepository.findById(Optional.ofNullable(visitUpdateRequest.getSpecialityId()).orElse(0L));
        speciality.ifPresent(visit::setSpeciality);

        Optional<Physician> physician = physicianRepository.findById(Optional.ofNullable(visitUpdateRequest.getPhysicianId()).orElse(0L));
        physician.ifPresent(visit::setPhysician);

        Optional<Clinic> clinic = clinicRepository.findById(Optional.ofNullable(visitUpdateRequest.getClinicId()).orElse(0L));
        clinic.ifPresent(visit::setClinic);

        Optional<Patient> patient = patientRepository.findById(Optional.ofNullable(visitUpdateRequest.getPatientId()).orElse(0L));
        patient.ifPresent(visit::setPatient);

        visitRepository.save(visit);
    }
}
