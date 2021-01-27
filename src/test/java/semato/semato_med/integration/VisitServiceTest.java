package semato.semato_med.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import semato.semato_med.exception.BookingException;
import semato.semato_med.model.*;
import semato.semato_med.mother.ClinicMother;
import semato.semato_med.mother.PatientMother;
import semato.semato_med.mother.PhysicianMother;
import semato.semato_med.repository.*;
import semato.semato_med.service.VisitService;

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

    @Autowired
    PatientMother patientMother;

    @Test
    void finds_clinic_by_speciality_when_clinic_with_proper_speciality_exists() {
        Speciality speciality = new Speciality();
        speciality.setName("Neurochirurgia");
        specialityRepository.save(speciality);
        assumeClinicWithSpecialityExists(speciality);

        List<Clinic> clinicListContains = visitService.getClinicListBySpeciality(specialityRepository.findByName("Neurochirurgia").get());

        assertEquals(1, clinicListContains.size());
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

        List<Clinic> clinicListContains = visitService.getClinicListBySpeciality(specialityRepository.findByName("Kosmetologia").get());

        assertEquals(0, clinicListContains.size());
    }

    @Test
    void finds_physician_by_speciality_and_clinic_when_exists() {
        Speciality speciality = new Speciality();
        speciality.setName("Neurochirurgia");
        specialityRepository.save(speciality);
        Clinic clinic = assumeClinicWithSpecialityExists(speciality);

        List<Physician> physicianList = visitService.getPhysicianListBySpecialityAndClinic(speciality, clinic);
        assertEquals(1, physicianList.size());
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

        List<Physician> physicianList = visitService.getPhysicianListBySpecialityAndClinic(notAssignedSpeciality, clinic);
        assertEquals(0, physicianList.size());
    }

    @Test
    void visit_can_be_booked_whern_there_is_no_collision() {
        Clinic clinic = clinicMother.get();
        clinicRepository.save(clinic);
        Speciality speciality = new Speciality();
        speciality.setName("Kardiologia");
        specialityRepository.save(speciality);
        Physician physician = physicianMother.getWithSpeciality(speciality, roleRepository);
        physicianRepository.save(physician);

        WorkSchedule workScheduleEntry = new WorkSchedule();
        workScheduleEntry.setPhysician(physician);
        workScheduleEntry.setClinic(clinic);
        workScheduleEntry.setDateTimeStart(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.NOON.minusHours(4)));
        workScheduleEntry.setDateTimeEnd(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.NOON.plusHours(4)));
        workScheduleRepository.save(workScheduleEntry);

        Visit visit = visitService.bookVisitWithParams(
                speciality,
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.NOON.minusHours(4)),
                LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.NOON.minusHours(4).plusSeconds(Visit.VISIT_LENGHT_SECONDS)),
                clinic,
                physician,
                patientRepository.save(patientMother.get(roleRepository))
        );

        assertTrue(visit instanceof Visit);
    }

    @Test
    void visit_can_not_be_booked_whern_there_is_collision() {
        Clinic clinic = clinicMother.get();
        clinicRepository.save(clinic);
        Speciality speciality = new Speciality();
        speciality.setName("Kardiologia");
        specialityRepository.save(speciality);
        Physician physician = physicianMother.getWithSpeciality(speciality, roleRepository);
        physicianRepository.save(physician);

        WorkSchedule workScheduleEntry = new WorkSchedule();
        workScheduleEntry.setPhysician(physician);
        workScheduleEntry.setClinic(clinic);
        workScheduleEntry.setDateTimeStart(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.NOON.minusHours(4)));
        workScheduleEntry.setDateTimeEnd(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.NOON.plusHours(4)));
        workScheduleRepository.save(workScheduleEntry);

        Visit visit = new Visit();
        visit.setStatus(VisitStatus.RESERVED);
        visit.setClinic(clinic);
        visit.setPhysician(physician);
        visit.setPatient(patientRepository.save(patientMother.get(roleRepository)));
        visit.setDateTimeStart(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.NOON.minusHours(4)));
        visit.setDateTimeEnd(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.NOON.minusHours(4).plusSeconds(Visit.VISIT_LENGHT_SECONDS)));
        visitRepository.save(visit);

        assertThrows(BookingException.class, () -> {
            visitService.bookVisitWithParams(
                    speciality,
                    LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.NOON.minusHours(4).plusSeconds(Visit.VISIT_LENGHT_SECONDS - 1)),
                    LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.NOON.minusHours(4).plusSeconds(2 * Visit.VISIT_LENGHT_SECONDS - 1)),
                    clinic,
                    physician,
                    patientRepository.save(patientMother.get(roleRepository))
            );
        });
    }

    private Clinic assumeClinicWithSpecialityExists(Speciality speciality) {

        Clinic clinic = clinicMother.get();
        clinicRepository.save(clinic);

        Physician physician = physicianMother.getWithSpeciality(speciality, roleRepository);
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