package semato.semato_med.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semato.semato_med.model.Clinic;
import semato.semato_med.payload.clinic.ClinicAddingRequest;
import semato.semato_med.payload.clinic.ClinicResponse;
import semato.semato_med.payload.clinic.ClinicUpdateRequest;
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
            if(clinicUpdateRequest.getName() != null){
                clinic.get().setName(clinicUpdateRequest.getName());
            }
            if(clinicUpdateRequest.getCity() != null){
                clinic.get().setCity(clinicUpdateRequest.getCity());
            }
            if(clinicUpdateRequest.getCountry() != null){
                clinic.get().setCountry(clinicUpdateRequest.getCountry());
            }
            if(clinicUpdateRequest.getPostalCode() != null){
                clinic.get().setPostalCode(clinicUpdateRequest.getPostalCode());
            }
            if(clinicUpdateRequest.getStreet() != null){
                clinic.get().setStreet(clinicUpdateRequest.getStreet());
            }
            if(clinicUpdateRequest.getHouseNumber() != null){
                clinic.get().setHouseNumber(clinicUpdateRequest.getHouseNumber());
            }
            if(clinicUpdateRequest.getEmail() != null){
                clinic.get().setEmail(clinicUpdateRequest.getEmail());
            }
            if(clinicUpdateRequest.getLatitude() != null){
                clinic.get().setLatitude(clinicUpdateRequest.getLatitude());
            }
            if(clinicUpdateRequest.getLongitude() != null){
                clinic.get().setLongitude(clinicUpdateRequest.getLongitude());
            }
            if(clinicUpdateRequest.getImageUrl() != null){
                clinic.get().setImageUrl(clinicUpdateRequest.getImageUrl());
            }
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
