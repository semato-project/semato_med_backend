package semato.semato_med.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import semato.semato_med.model.*;
import semato.semato_med.repository.*;
import semato.semato_med.service.VisitService;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;

@Profile("dev")
@Component
@Transactional
@Order(4)
public class VisitLoader implements ApplicationRunner {

    @Autowired
    private WorkScheduleRepository workScheduleRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private VisitService visitService;

    @Autowired
    EntityManager entityManager;

    public void run(ApplicationArguments args) {

        Random random = new Random();

        Optional<Physician> strange = physicianRepository.findOneByEmail(PhysicianLoader.STRANGE_EXAMPLE_EMAIL);
        Optional<Physician> house = physicianRepository.findOneByEmail(PhysicianLoader.HOUSE_EXAMPLE_EMAIL);
        Optional<Physician> lecter = physicianRepository.findOneByEmail(PhysicianLoader.LECTER_EXAMPLE_EMAIL);
        Optional<Physician> najlepszy = physicianRepository.findOneByEmail(PhysicianLoader.NAJLEPSZY_EXAMPLE_EMAIL);

        List<Physician> physicians = new ArrayList<>();
        physicians.add(strange.get());
        physicians.add(house.get());
        physicians.add(lecter.get());
        physicians.add(najlepszy.get());

        List<WorkSchedule> strangeWorkSchedule = workScheduleRepository.findByPhysician(strange.get());
        List<WorkSchedule> houseWorkSchedule = workScheduleRepository.findByPhysician(house.get());
        List<WorkSchedule> lecterWorkSchedule = workScheduleRepository.findByPhysician(lecter.get());
        List<WorkSchedule> najlepszyWorkSchedule = workScheduleRepository.findByPhysician(najlepszy.get());

        List<List<WorkSchedule>> workSchedules = new ArrayList<>();
        workSchedules.add(strangeWorkSchedule);
        workSchedules.add(houseWorkSchedule);
        workSchedules.add(lecterWorkSchedule);
        workSchedules.add(najlepszyWorkSchedule);

        Patient alekLolek = patientRepository.findOneByEmail(PatientLoader.ALEK_OLEK_EXAMPLE_EMAIL).get();
        Patient tristanPakistanczyk = patientRepository.findOneByEmail(PatientLoader.TRISTAN_PAKISTANCZYK_EXAMPLE_EMAIL).get();
        Patient paulinaRekin = patientRepository.findOneByEmail(PatientLoader.PAULINA_REKIN_EXAMPLE_EMAIL).get();
        Patient romekBromek = patientRepository.findOneByEmail(PatientLoader.ROMEK_BROMEK_EXAMPLE_EMAIL).get();
        Patient kasiaZpodlasia = patientRepository.findOneByEmail(PatientLoader.KASIA_ZPODLASIA_EXAMPLE_EMAIL).get();
        Patient waclawKumaty = patientRepository.findOneByEmail(PatientLoader.WACLAW_KUMATY_EXAMPLE_EMAIL).get();

        List<Patient> patients = new ArrayList<>();
        patients.add(alekLolek);
        patients.add(tristanPakistanczyk);
        patients.add(paulinaRekin);
        patients.add(romekBromek);
        patients.add(kasiaZpodlasia);
        patients.add(waclawKumaty);

        List<Clinic> clinics = clinicRepository.findAll();

        for(int i=0;i<16;i++){
            int doctorsAndHisWorkScheduleIndexInArray = random.nextInt(physicians.size());
            List<WorkSchedule> workScheduleList = workSchedules.get(doctorsAndHisWorkScheduleIndexInArray);
            addVisit(
                    workScheduleList
                            .get(random.nextInt(workScheduleList.size()))
                            .getDateTimeStart()
                            .plusSeconds(i * Visit.VISIT_LENGHT_SECONDS),
                    physicians.get(doctorsAndHisWorkScheduleIndexInArray),
                    physicians.get(doctorsAndHisWorkScheduleIndexInArray).getSpecialitySet(),
                    patients.get(random.nextInt(patients.size())),
                    clinics.get(random.nextInt(clinics.size()))
            );
        }


    }

    private void addVisit(
            LocalDateTime dateTimeStart,
            Physician physician,
            Set<Speciality> speciality,
            Patient patient,
            Clinic clinic
    ) {

        List<Speciality> specialities = new ArrayList<>(speciality);

        Random random = new Random();
        visitService.bookVisitWithParams(
                specialities.get(random.nextInt(specialities.size())),
                dateTimeStart,
                dateTimeStart.plusSeconds(Visit.VISIT_LENGHT_SECONDS),
                clinic,
                physician,
                patient);


    }

}
