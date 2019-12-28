package semato.semato_med.loader;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
import org.springframework.stereotype.Component;
import semato.semato_med.model.*;
import semato.semato_med.repository.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@Order(3)
public class VisitLoader implements ApplicationRunner {

    @Autowired
    private WorkScheduleRepository workScheduleRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    EntityManager entityManager;

    @Override
    public void run(ApplicationArguments args) {

        String jpgl =
                "select p " +
                        "from Physician p " +
                        "inner join p.user u " +
                        "inner join fetch p.specialitySet s " +
                        "where u.email = :email "
                ;

        TypedQuery<Physician> query = entityManager.createQuery(
                jpgl,
                Physician.class)
                .setParameter("email", PhysicianLoader.EMAIL)
        ;

        Physician physician = query.getResultList().get(0);

        Patient patient = patientRepository.findOneByEmail(PatientLoader.EMAIL).get();

        WorkSchedule workSchedule = workScheduleRepository.findOneByPhysician(physician).get();

        addVisit(
                workSchedule.getDateTimeStart().plusSeconds(Visit.VISIT_LENGHT_SECONDS),
                physician,
                physician.getSpecialitySet().iterator().next(),
                patient,
                clinicRepository.findByEmail(ClinicLoader.EMAIL).get()
        );

        addVisit(
                workSchedule.getDateTimeStart().plusSeconds(4 * Visit.VISIT_LENGHT_SECONDS),
                physician,
                physician.getSpecialitySet().iterator().next(),
                patient,
                clinicRepository.findByEmail(ClinicLoader.EMAIL).get()
        );

        addVisit(
                workSchedule.getDateTimeStart().plusSeconds(7 * Visit.VISIT_LENGHT_SECONDS),
                physician,
                physician.getSpecialitySet().iterator().next(),
                patient,
                clinicRepository.findByEmail(ClinicLoader.EMAIL).get()
        );

    }

    private void addVisit(
            LocalDateTime dateTimeStart,
            Physician physician,
            Speciality speciality,
            Patient patient,
            Clinic clinic
    ) {

        Visit visit = new Visit();
        visit.setDateTimeStart(dateTimeStart);
        visit.setDateTimeEnd(dateTimeStart.plusSeconds(Visit.VISIT_LENGHT_SECONDS));
        visit.setPhysician(physician);
        visit.setSpeciality(speciality);
        visit.setPatient(patient);
        visit.setClinic(clinic);
        visit.setStatus(VisitStatus.RESERVED);

        visitRepository.save(visit);
    }

}
