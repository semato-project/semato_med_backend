package semato.semato_med.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import semato.semato_med.model.Patient;
import semato.semato_med.model.Role;
import semato.semato_med.model.User;
import semato.semato_med.repository.PatientRepository;
import semato.semato_med.repository.RoleRepository;
import semato.semato_med.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class SoftDeleteService {

    @Autowired
    private UserRepository userRepository;

    protected String getObliterateStringUnique() {
        return getObliterateString() + "_" + UUID.randomUUID().toString();
    }

    protected String getObliterateString() {
        return "_obliterated";
    }

    protected LocalDate getObliterateLocalDate() {
        return LocalDate.ofEpochDay(0);
    }

    public void deletePatient(Patient patient) {

        patient.setHouseNumber(getObliterateString());
        patient.setCity(getObliterateString());
        patient.setStreet(getObliterateString());
        patient.setPesel(getObliterateString());
        patient.setPostalCode(getObliterateString());
        patient.setBirthDate(getObliterateLocalDate());

        User user = patient.getUser();
        user.setEmail(getObliterateStringUnique());
        user.setFirstName(getObliterateString());
        user.setLastName(getObliterateString());
        user.setPhone(getObliterateString());
        user.setPassword(getObliterateString());
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);
    }



}
