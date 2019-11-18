package semato.semato_med.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import semato.semato_med.model.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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

    public List<Clinic> getClinicListBySpeciality(Speciality speciality) {

        List<Clinic> clinicList = entityManager.createQuery(
                "select c " +
                        "from Clinic c " +
                        "inner join fetch c.workScheduleList ws " +
                        "inner join fetch ws.physician p " +
                        "inner join fetch p.specialityList s " +
                        "where s.id = :specialityId " +
                        "and ws.dateTimeEnd > :now",
                Clinic.class)
                .setParameter("specialityId", speciality.getId())
                .setParameter("now", LocalDateTime.now())
                .getResultList();

        return clinicList;
    }

    public List<Physician> getPhysicianListBySpecialityAndClinic(Speciality speciality, @Nullable Clinic clinic) {

        String hql =
                "select p " +
                "from Physician p " +
                "inner join fetch p.workScheduleList ws " +
                "inner join fetch ws.clinic c " +
                "inner join fetch p.specialityList s " +
                "where s.id = :specialityId " +
                "and ws.dateTimeEnd > :now "
                ;

        if (clinic != null) {
            hql += "and c.id = :clinicId ";
        }

        TypedQuery<Physician> query = entityManager.createQuery(
                hql,
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

        String hql =
                "select ws " +
                        "from WorkSchedule ws " +
                        "inner join fetch ws.physician p " +
                        "inner join fetch p.specialityList s " +
                        "inner join fetch ws.clinic c " +
                        "where s.id = :specialityId " +
                        "and ws.dateTimeEnd > :now " +
                        "and ws.dateTimeEnd <= :periodEnd " +
                        "and ws.dateTimeStart >= :periodStart "
                ;

        if (clinic != null) {
            hql += "and c.id = :clinicId ";
        }

        if (physician != null) {
            hql += "and p.id = :physicianId ";
        }

        TypedQuery<WorkSchedule> query = entityManager.createQuery(
            hql,
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

    private boolean visitFits(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, Clinic clinic, Physician physician) {

//        String hql =
//                "select ws " +
//                        "from WorkSchedule ws " +
//                        "and ws.dateTimeEnd <= :periodEnd " +
//                        "and ws.dateTimeStart >= :periodStart " +
//                        "and
//                ;


    }

    public void bookVisit(Speciality speciality, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, Clinic clinic, Physician physician) {

    }

}
