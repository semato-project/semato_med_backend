package semato.semato_med.service;


import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import semato.semato_med.exception.BookingException;
import semato.semato_med.model.*;
import semato.semato_med.repository.VisitRepository;
import semato.semato_med.security.CurrentUser;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class VisitService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    WorkScheduleService workScheduleService;

    @Autowired
    VisitRepository visitRepository;


    public List<Clinic> getClinicListBySpeciality(Speciality speciality) {

        List<Clinic> clinicList = entityManager.createQuery(
                "select c " +
                        "from Clinic c " +
                        "inner join fetch c.workScheduleSet ws " +
                        "inner join fetch ws.physician p " +
                        "inner join fetch p.specialitySet s " +
                        "where s.id = :specialityId " +
                        "and ws.dateTimeEnd > :now",
                Clinic.class)
                .setParameter("specialityId", speciality.getId())
                .setParameter("now", LocalDateTime.now())
                .getResultList();

        return clinicList;
    }

    public List<Physician> getPhysicianListBySpecialityAndClinic(Speciality speciality, @Nullable Clinic clinic) {

        String jpql =
                "select p " +
                "from Physician p " +
                "inner join fetch p.workScheduleSet ws " +
                "inner join fetch ws.clinic c " +
                "inner join fetch p.specialitySet s " +
                "where s.id = :specialityId " +
                "and ws.dateTimeEnd > :now "
                ;

        if (clinic != null) {
            jpql += "and c.id = :clinicId ";
        }

        TypedQuery<Physician> query = entityManager.createQuery(
                jpql,
                Physician.class)
                .setParameter("specialityId", speciality.getId())
                .setParameter("now", LocalDateTime.now()
        );

        if (clinic != null) {
            query.setParameter("clinicId", clinic.getId());
        }

        List<Physician> physicianList = query.getResultList();

        return physicianList;
    }

    public List<Visit> getAvailableVisitList(Speciality speciality, LocalDate periodStart, LocalDate periodEnd, @Nullable Clinic clinic, @Nullable Physician physician) {

        String jpql =
                "select ws " +
                        "from WorkSchedule ws " +
                        "inner join fetch ws.physician p " +
                        "inner join fetch p.specialitySet s " +
                        "inner join fetch ws.clinic c " +
                        "where s.id = :specialityId " +
                        "and ws.dateTimeEnd > :now " +
                        "and ws.dateTimeEnd <= :periodEnd " +
                        "and ws.dateTimeStart >= :periodStart "
                ;

        if (clinic != null) {
            jpql += "and c.id = :clinicId ";
        }

        if (physician != null) {
            jpql += "and p.id = :physicianId ";
        }

        TypedQuery<WorkSchedule> query = entityManager.createQuery(
            jpql,
            WorkSchedule.class
        )
            .setParameter("specialityId", speciality.getId())
            .setParameter("now", LocalDateTime.now())
            .setParameter("periodEnd", LocalDateTime.of(periodEnd, LocalTime.MAX))
            .setParameter("periodStart", LocalDateTime.of(periodStart, LocalTime.MIN))
        ;

        if (clinic != null) {
            query.setParameter("clinicId", clinic.getId());
        }

        if (physician != null) {
            query.setParameter("physicianId", physician.getId());
        }

        List<WorkSchedule> workScheduleList = query.getResultList();

        List<Visit> availableVisitList = new LinkedList<Visit>();

        for (WorkSchedule workSchedule: workScheduleList) {

            List<WorkScheduleService.Slot> availableSlotList = workScheduleService.getAvailableSlotList(workSchedule);

            for (WorkScheduleService.Slot slot: availableSlotList) {

                Visit virtualVisit = new Visit();
                virtualVisit.setClinic(clinic);
                virtualVisit.setDateTimeStart(slot.getDateTimeStart());
                virtualVisit.setDateTimeEnd(slot.getDateTimeEnd());
                virtualVisit.setPhysician(physician);
                virtualVisit.setSpeciality(speciality);
                virtualVisit.setStatus(VisitStatus.AVAILABLE);
                availableVisitList.add(virtualVisit);
            }
        }

        return availableVisitList;
    }

    private boolean visitFits(Visit visit) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<WorkSchedule> criteriaQuery = criteriaBuilder.createQuery(WorkSchedule.class);

        Root<WorkSchedule> root = criteriaQuery.from(WorkSchedule.class);

        criteriaQuery
                .select(root)
                .where(criteriaBuilder.lessThanOrEqualTo(root.get("dateTimeStart"), visit.getDateTimeStart()))
                .where(criteriaBuilder.greaterThanOrEqualTo(root.get("dateTimeEnd"), visit.getDateTimeEnd()))
                .where(criteriaBuilder.equal(root.get("physician"), visit.getPhysician()))
                .where(criteriaBuilder.equal(root.get("clinic"), visit.getClinic()))
        ;

        TypedQuery<WorkSchedule> query = entityManager.createQuery(criteriaQuery);
        List<WorkSchedule> workScheduleList = query.getResultList(); // @Todo na prawdę niema getOneResult()??1
        WorkSchedule workSchedule = workScheduleList.get(0);

        return workScheduleService.slotableFits(visit, workSchedule);
    }

    public Visit bookVisitWithParams(Speciality speciality, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, Clinic clinic, Physician physician, Patient patient) {

        Visit virtualVisit = createVirtualVisit(speciality, dateTimeStart, dateTimeEnd, clinic, physician, patient);

        if (! visitFits(virtualVisit)) {
            throw new BookingException();
        }

        virtualVisit.setStatus(VisitStatus.RESERVED);

        visitRepository.save(virtualVisit);
        return virtualVisit;
    }

    protected Visit createVirtualVisit(Speciality speciality, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, Clinic clinic, Physician physician, Patient patient) {

        Visit visit = new Visit();
        visit.setDateTimeStart(dateTimeStart);
        visit.setDateTimeEnd(dateTimeEnd);
        visit.setClinic(clinic);
        visit.setPhysician(physician);
        visit.setSpeciality(speciality);
        visit.setPatient(patient);

        return visit;
    }

    public SimpleMailMessage constructConfirmationVisitEmail(Patient patient, Visit visit){
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Confirmation visit");
        email.setText("You reserved visit successfully: " + System.getProperty("line.separator") +
                "Doctor: " + visit.getPhysician().getFullName() + System.getProperty("line.separator") +
                "Date and time: " + visit.getDateTimeStart() + System.getProperty("line.separator") +
                "Clinic: " + visit.getClinic().getName() + System.getProperty("line.separator") +
                "Localization: " + visit.getClinic().getCountry() + " " + visit.getClinic().getCity() + " "
                + visit.getClinic().getStreet() + " " + visit.getClinic().getHouseNumber());
        email.setTo(patient.getUser().getEmail());
        email.setFrom("SematoMedClinic");
        return email;
    }

    public SimpleMailMessage constructCancelVisitEmail(Patient patient, Visit visit){
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Cancel visit");
        email.setText("You cancel a visit: " + System.getProperty("line.separator") +
                "Doctor: " + visit.getPhysician().getFullName() + System.getProperty("line.separator") +
                "Date and time: " + visit.getDateTimeStart() + System.getProperty("line.separator") +
                "Clinic: " + visit.getClinic().getName() + System.getProperty("line.separator") +
                "Localization: " + visit.getClinic().getCountry() + " " + visit.getClinic().getCity() + " "
                + visit.getClinic().getStreet() + " " + visit.getClinic().getHouseNumber());
        email.setTo(patient.getUser().getEmail());
        email.setFrom("SematoMedClinic");
        return email;
    }
}
