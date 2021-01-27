package semato.semato_med.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import semato.semato_med.exception.AppException;
import semato.semato_med.model.*;
import semato.semato_med.mother.ClinicMother;
import semato.semato_med.mother.PhysicianMother;
import semato.semato_med.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PhysicianMother physicianMother;

    @Autowired
    ClinicMother clinicMother;

    @Test
    void finds_clinic_by_speciality_when_clinic_with_proper_speciality_exists() {
        Speciality speciality = new Speciality();
        speciality.setName("Neurochirurgia");
        specialityRepository.save(speciality);
        assumeClinicWithSpecialityExists(speciality);

        List<Clinic> clinicListContains =  visitService.getClinicListBySpeciality(specialityRepository.findByName("Neurochirurgia").get());

        assertEquals( 1, clinicListContains.size());
    }

    @Test
    void finds_no_clinic_by_speciality_when_clinic_with_searched_speciality_does_not_exist() {
        Speciality assignedSpeciality = new Speciality();
        assignedSpeciality.setName("Stomatologia");
        specialityRepository.save(assignedSpeciality);
        assumeClinicWithSpecialityExists(assignedSpeciality);

        Speciality notAssignedSpeciality = new Speciality();
        notAssignedSpeciality.setName("Kosmetologia");
        specialityRepository.save(notAssignedSpeciality);

        List<Clinic> clinicListContains =  visitService.getClinicListBySpeciality(specialityRepository.findByName("Kosmetologia").get());

        assertEquals( 0, clinicListContains.size());
    }

    @Test
    void finds_physician_by_speciality_and_clinic_when_exists() {
        Speciality speciality = new Speciality();
        speciality.setName("Neurochirurgia");
        specialityRepository.save(speciality);
        Clinic clinic = assumeClinicWithSpecialityExists(speciality);

        List<Physician> physicianList =  visitService.getPhysicianListBySpecialityAndClinic(speciality, clinic);
        assertEquals( 1, physicianList.size());
    }

    @Test
    void finds_no_physician_by_speciality_and_clinic_when_does_not_exist() {
        Speciality assignedSpeciality = new Speciality();
        assignedSpeciality.setName("Kardiologia");
        specialityRepository.save(assignedSpeciality);
        Clinic clinic = assumeClinicWithSpecialityExists(assignedSpeciality);

        Speciality notAssignedSpeciality = new Speciality();
        notAssignedSpeciality.setName("Astrologia");
        specialityRepository.save(notAssignedSpeciality);

        List<Physician> physicianList =  visitService.getPhysicianListBySpecialityAndClinic(notAssignedSpeciality, clinic);
        assertEquals( 0, physicianList.size());
    }

//    @Test
//    void bookVisitWithParams() {
//        Visit visit = visitRepository.findAll().get(0);
//
//        assertThrows(BookingException.class, () -> {
//            visitService.bookVisitWithParams(
//                    visit.getSpeciality(),
//                    visit.getDateTimeStart(),
//                    visit.getDateTimeEnd(),
//                    visit.getClinic(),
//                    visit.getPhysician(),
//                    visit.getPatient()
//            );
//        });
//
//        assertThrows(BookingException.class, () -> {
//            visitService.bookVisitWithParams(
//                    visit.getSpeciality(),
//                    visit.getDateTimeStart().minusSeconds(Visit.VISIT_LENGHT_SECONDS / 2),
//                    visit.getDateTimeEnd().minusSeconds(Visit.VISIT_LENGHT_SECONDS / 2),
//                    visit.getClinic(),
//                    visit.getPhysician(),
//                    visit.getPatient()
//            );
//        });
//
//        assertThrows(BookingException.class, () -> {
//            visitService.bookVisitWithParams(
//                    visit.getSpeciality(),
//                    visit.getDateTimeStart().plusSeconds(Visit.VISIT_LENGHT_SECONDS / 2),
//                    visit.getDateTimeEnd().plusSeconds(Visit.VISIT_LENGHT_SECONDS / 2),
//                    visit.getClinic(),
//                    visit.getPhysician(),
//                    visit.getPatient()
//            );
//        });
//
//        Visit bookedVisit1 = visitService.bookVisitWithParams(
//                visit.getSpeciality(),
//                visit.getDateTimeStart().minusSeconds(Visit.VISIT_LENGHT_SECONDS),
//                visit.getDateTimeEnd().minusSeconds(Visit.VISIT_LENGHT_SECONDS),
//                visit.getClinic(),
//                visit.getPhysician(),
//                visit.getPatient()
//        );
//
//        assertTrue(bookedVisit1 instanceof Visit);
//
//        Visit bookedVisit2 = visitService.bookVisitWithParams(
//                visit.getSpeciality(),
//                visit.getDateTimeStart().plusSeconds(Visit.VISIT_LENGHT_SECONDS),
//                visit.getDateTimeEnd().plusSeconds(Visit.VISIT_LENGHT_SECONDS),
//                visit.getClinic(),
//                visit.getPhysician(),
//                visit.getPatient()
//        );
//
//        assertTrue(bookedVisit2 instanceof Visit);
//
//        Visit bookedVisit3 = visitService.bookVisitWithParams(
//                visit.getSpeciality(),
//                visit.getDateTimeStart().plusSeconds(Visit.VISIT_LENGHT_SECONDS * 4),
//                visit.getDateTimeEnd().plusSeconds(Visit.VISIT_LENGHT_SECONDS * 4),
//                visit.getClinic(),
//                visit.getPhysician(),
//                visit.getPatient()
//        );
//
//        assertTrue(bookedVisit3 instanceof Visit);
//    }

    private Clinic assumeClinicWithSpecialityExists(Speciality speciality) {

        Clinic clinic = clinicMother.get();
        clinicRepository.save(clinic);

        Physician physician = physicianMother.createWithSpeciality(speciality, roleRepository);
        physicianRepository.save(physician);

        WorkSchedule workScheduleEntry = new WorkSchedule();
        workScheduleEntry.setPhysician(physician);
        workScheduleEntry.setClinic(clinic);
        workScheduleEntry.setDateTimeStart(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.NOON.minusHours(4)));
        workScheduleEntry.setDateTimeEnd(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.NOON.plusHours(4)));
        workScheduleRepository.save(workScheduleEntry);

        return clinic;
    }

}