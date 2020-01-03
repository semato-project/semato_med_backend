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
    ClinicRepository clinicRepository;

    public Clinic add(ClinicAddingRequest clinicAddingRequest){
        Optional<Clinic> clinicOptional = clinicRepository.findByEmail(clinicAddingRequest.getEmail());
        if(!clinicOptional.isPresent()) {
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
            return clinic;
        }

        return null;
    }

    public ClinicResponse get(Long id){
        Optional<Clinic> clinicOptional = clinicRepository.findById(id);
        return clinicOptional.map(clinic -> new ClinicResponse(clinic.getId(),
                clinic.getName(),
                clinic.getCity(),
                clinic.getCountry(),
                clinic.getPostalCode(),
                clinic.getStreet(),
                clinic.getHouseNumber(),
                clinic.getEmail(),
                clinic.getLatitude(),
                clinic.getLongitude(),
                clinic.getImageUrl(),
                clinic.getDeletedAt())).orElse(null);

    }

    public List<ClinicResponse> getAll(){
        List<Clinic> clinicList = clinicRepository.findAll();
        List<ClinicResponse> clinicResponses = new ArrayList<>();
        for (Clinic clinic : clinicList) {
            clinicResponses.add(new ClinicResponse(
                    clinic.getId(),
                    clinic.getName(),
                    clinic.getCity(),
                    clinic.getCountry(),
                    clinic.getPostalCode(),
                    clinic.getStreet(),
                    clinic.getHouseNumber(),
                    clinic.getEmail(),
                    clinic.getLatitude(),
                    clinic.getLongitude(),
                    clinic.getImageUrl(),
                    clinic.getDeletedAt()));
        }

        return clinicResponses;
    }

    public Clinic update(ClinicUpdateRequest clinicUpdateRequest, Long clinicId){
        Optional<Clinic> clinic = clinicRepository.findById(clinicId);
        if(clinic.isPresent()){
            Optional.ofNullable(clinicUpdateRequest.getName()).ifPresent(clinic.get()::setName);
            Optional.ofNullable(clinicUpdateRequest.getCity()).ifPresent(clinic.get()::setCity);
            Optional.ofNullable(clinicUpdateRequest.getCountry()).ifPresent(clinic.get()::setCountry);
            Optional.ofNullable(clinicUpdateRequest.getPostalCode()).ifPresent(clinic.get()::setPostalCode);
            Optional.ofNullable(clinicUpdateRequest.getStreet()).ifPresent(clinic.get()::setStreet);
            Optional.ofNullable(clinicUpdateRequest.getHouseNumber()).ifPresent(clinic.get()::setHouseNumber);
            Optional.ofNullable(clinicUpdateRequest.getEmail()).ifPresent(clinic.get()::setEmail);
            Optional.ofNullable(clinicUpdateRequest.getLatitude()).ifPresent(clinic.get()::setLatitude);
            Optional.ofNullable(clinicUpdateRequest.getLongitude()).ifPresent(clinic.get()::setLongitude);
            Optional.ofNullable(clinicUpdateRequest.getImageUrl()).ifPresent(clinic.get()::setImageUrl);
            clinicRepository.save(clinic.get());
            return clinic.get();
        }

        return null;
    }

    public boolean delete(Long clinicId){
        Optional<Clinic> clinic = clinicRepository.findById(clinicId);
        if (clinic.isPresent()) {
            clinic.get().setDeletedAt(LocalDateTime.now());
            clinicRepository.save(clinic.get());
            return true;
        }

        return false;
    }
}
