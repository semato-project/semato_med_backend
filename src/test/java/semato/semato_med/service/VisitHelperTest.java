package semato.semato_med.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import semato.semato_med.model.Clinic;
import semato.semato_med.repository.SpecialityRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VisitHelperTest {

    @Autowired
    SpecialityRepository specialityRepository;

    @Autowired
    VisitHelper visitHelper;

    @Test
    void getClinicListBySpeciality() {

        List<Clinic> clinicList =  visitHelper.getClinicListBySpeciality(specialityRepository.findByName("Neurochirurgia").get());
        assertEquals( 1, clinicList.size());

    }
}