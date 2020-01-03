package semato.semato_med.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semato.semato_med.model.Clinic;
import semato.semato_med.payload.clinicMgmt.ClinicAddingRequest;
import semato.semato_med.payload.clinicMgmt.ClinicResponse;
import semato.semato_med.payload.clinicMgmt.ClinicUpdateRequest;
import semato.semato_med.repository.ClinicRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClinicManagementService {

    @Autowired
    private ClinicRepository clinicRepository;

    public void add(ClinicAddingRequest clinicAddingRequest) {
        Clinic clinic = new Clinic(
                clinicAddingRequest.getName(),
                clinicAddingRequest.getCity(),
                clinicAddingRequest.getCountry(),
                clinicAddingRequest.getPostalCode(),
                clinicAddingRequest.getStreet(),
                clinicAddingRequest.getHouseNumber(),
                clinicAddingRequest.getEmail(),
                clinicAddingRequest.getLatitude(),
                clinicAddingRequest.getLongitude(),
                clinicAddingRequest.getImageUrl());
        clinicRepository.save(clinic);
    }

    public List<ClinicResponse> getAll() {
        List<Clinic> clinicList = clinicRepository.findAll();
        List<ClinicResponse> clinicResponses = new ArrayList<>();
        for (Clinic clinic : clinicList) {
            clinicResponses.add(new ClinicResponse(clinic));
        }

        return clinicResponses;
    }

    public void update(Clinic clinic, ClinicUpdateRequest clinicUpdateRequest) {

        Optional.ofNullable(clinicUpdateRequest.getName()).ifPresent(clinic::setName);
        Optional.ofNullable(clinicUpdateRequest.getCity()).ifPresent(clinic::setCity);
        Optional.ofNullable(clinicUpdateRequest.getCountry()).ifPresent(clinic::setCountry);
        Optional.ofNullable(clinicUpdateRequest.getPostalCode()).ifPresent(clinic::setPostalCode);
        Optional.ofNullable(clinicUpdateRequest.getStreet()).ifPresent(clinic::setStreet);
        Optional.ofNullable(clinicUpdateRequest.getHouseNumber()).ifPresent(clinic::setHouseNumber);
        Optional.ofNullable(clinicUpdateRequest.getEmail()).ifPresent(clinic::setEmail);
        Optional.ofNullable(clinicUpdateRequest.getLatitude()).ifPresent(clinic::setLatitude);
        Optional.ofNullable(clinicUpdateRequest.getLongitude()).ifPresent(clinic::setLongitude);
        Optional.ofNullable(clinicUpdateRequest.getImageUrl()).ifPresent(clinic::setImageUrl);
        clinicRepository.save(clinic);

    }

    public void delete(Clinic clinic) {
        clinic.setDeletedAt(LocalDateTime.now());
        clinicRepository.save(clinic);

    }
}
