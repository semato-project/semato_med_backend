package semato.semato_med.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import semato.semato_med.loader.ClinicLoader;
import semato.semato_med.model.Clinic;
import semato.semato_med.model.Physician;
import semato.semato_med.repository.ClinicRepository;
import semato.semato_med.repository.SpecialityRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VisitServiceTest {

    @Autowired
    SpecialityRepository specialityRepository;

    @Autowired
    ClinicRepository clinicRepository;

    @Autowired
    VisitService visitService;

    @Test
    void getClinicListBySpeciality() {
        List<Clinic> clinicList =  visitService.getClinicListBySpeciality(specialityRepository.findByName("Neurochirurgia").get());
        assertEquals( 1, clinicList.size());
    }

    @Test
    void getPhysicianListBySpecialityAndClinic() {
        List<Physician> physicianList =  visitService.getPhysicianListBySpecialityAndClinic(
                specialityRepository.findByName("Neurochirurgia").get(),
                clinicRepository.findByEmail(ClinicLoader.EMAIL).get()
        );
        assertEquals( 1, physicianList.size());

        physicianList =  visitService.getPhysicianListBySpecialityAndClinic(
                specialityRepository.findByName("Neurochirurgia").get(),
                null
        );
        assertEquals( 1, physicianList.size());

    }

}