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

        Visit visit = new Visit();
        visit.setDateTimeStart(workSchedule.getDateTimeStart().plusSeconds(Visit.VISIT_LENGHT_SECONDS));
        visit.setDateTimeEnd(workSchedule.getDateTimeStart().plusSeconds(2 * Visit.VISIT_LENGHT_SECONDS));
        visit.setPhysician(workSchedule.getPhysician());
        visit.setSpeciality(physician.getSpecialitySet().iterator().next());
        visit.setPatient(patient);
        visit.setClinic(clinicRepository.findByEmail(ClinicLoader.EMAIL).get());
        visit.setStatus(VisitStatus.RESERVED);

        visitRepository.save(visit);
    }

}
