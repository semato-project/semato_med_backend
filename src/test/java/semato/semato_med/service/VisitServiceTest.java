package semato.semato_med.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import semato.semato_med.exception.BookingException;
import semato.semato_med.loader.ClinicLoader;
import semato.semato_med.model.*;
import semato.semato_med.repository.*;

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

    @Autowired
    PhysicianRepository physicianRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    WorkScheduleRepository workScheduleRepository;

    @Autowired
    VisitRepository visitRepository;

    @Test
    void getClinicListBySpeciality() {
        List<Clinic> clinicList =  visitService.getClinicListBySpeciality(specialityRepository.findByName("Neurochirurgia").get());
        assertEquals( 1, clinicList.size());
    }

    @Test
    void getPhysicianListBySpecialityAndClinic() {
        List<Physician> physicianList =  visitService.getPhysicianListBySpecialityAndClinic(
                specialityRepository.findByName("Neurochirurgia").get(),
                clinicRepository.findByEmail(ClinicLoader.CENTRUM_ZDROWIA_SZCZESCIA_I_POMYSLNOSCI_EXAMPLE_EMAIL).get()
        );
        assertEquals( 1, physicianList.size());

        physicianList =  visitService.getPhysicianListBySpecialityAndClinic(
                specialityRepository.findByName("Neurochirurgia").get(),
                null
        );
        assertEquals( 1, physicianList.size());

    }

    @Test
    void bookVisitWithParams() {
        Visit visit = visitRepository.findAll().get(0);

        assertThrows(BookingException.class, () -> {
            visitService.bookVisitWithParams(
                    visit.getSpeciality(),
                    visit.getDateTimeStart(),
                    visit.getDateTimeEnd(),
                    visit.getClinic(),
                    visit.getPhysician(),
                    visit.getPatient()
            );
        });

        assertThrows(BookingException.class, () -> {
            visitService.bookVisitWithParams(
                    visit.getSpeciality(),
                    visit.getDateTimeStart().minusSeconds(Visit.VISIT_LENGHT_SECONDS / 2),
                    visit.getDateTimeEnd().minusSeconds(Visit.VISIT_LENGHT_SECONDS / 2),
                    visit.getClinic(),
                    visit.getPhysician(),
                    visit.getPatient()
            );
        });

        assertThrows(BookingException.class, () -> {
            visitService.bookVisitWithParams(
                    visit.getSpeciality(),
                    visit.getDateTimeStart().plusSeconds(Visit.VISIT_LENGHT_SECONDS / 2),
                    visit.getDateTimeEnd().plusSeconds(Visit.VISIT_LENGHT_SECONDS / 2),
                    visit.getClinic(),
                    visit.getPhysician(),
                    visit.getPatient()
            );
        });

        Visit bookedVisit1 = visitService.bookVisitWithParams(
                visit.getSpeciality(),
                visit.getDateTimeStart().minusSeconds(Visit.VISIT_LENGHT_SECONDS),
                visit.getDateTimeEnd().minusSeconds(Visit.VISIT_LENGHT_SECONDS),
                visit.getClinic(),
                visit.getPhysician(),
                visit.getPatient()
        );

        assertTrue(bookedVisit1 instanceof Visit);

        Visit bookedVisit2 = visitService.bookVisitWithParams(
                visit.getSpeciality(),
                visit.getDateTimeStart().plusSeconds(Visit.VISIT_LENGHT_SECONDS),
                visit.getDateTimeEnd().plusSeconds(Visit.VISIT_LENGHT_SECONDS),
                visit.getClinic(),
                visit.getPhysician(),
                visit.getPatient()
        );

        assertTrue(bookedVisit2 instanceof Visit);

        Visit bookedVisit3 = visitService.bookVisitWithParams(
                visit.getSpeciality(),
                visit.getDateTimeStart().plusSeconds(Visit.VISIT_LENGHT_SECONDS * 4),
                visit.getDateTimeEnd().plusSeconds(Visit.VISIT_LENGHT_SECONDS * 4),
                visit.getClinic(),
                visit.getPhysician(),
                visit.getPatient()
        );

        assertTrue(bookedVisit3 instanceof Visit);
    }

}