package semato.semato_med.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semato.semato_med.model.Visit;
import semato.semato_med.model.WorkSchedule;
import semato.semato_med.util.Slotable;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class WorkScheduleService {

    @Autowired
    EntityManager entityManager;

    @Getter
    public class Slot implements Slotable {

        private LocalDateTime dateTimeStart;
        private LocalDateTime dateTimeEnd;

        public Slot(LocalDateTime start, LocalDateTime end) {
            this.dateTimeStart = start;
            this.dateTimeEnd = end;
        }
     }

    private List<Slot> getAllPossibleSlotList(WorkSchedule workSchedule) {

        List<Slot> allPossibleSlotList = new LinkedList<Slot>();

        int length = Visit.VISIT_LENGHT_SECONDS;

        LocalDateTime currentSlotStart = workSchedule.getDateTimeStart();
        LocalDateTime currentSlotEnd = currentSlotStart.plusSeconds(length);

        while (isBeforeOrEquals(currentSlotEnd, workSchedule.getDateTimeEnd())) {
            allPossibleSlotList.add(new Slot(currentSlotStart, currentSlotEnd));
            currentSlotStart = currentSlotStart.plusSeconds(length);
            currentSlotEnd = currentSlotEnd.plusSeconds(length);
        }

        return allPossibleSlotList;
    }

    public List<Visit> getExistingVisitListForWorkScheduleEntry(WorkSchedule workSchedule) {

        List<Visit> visitList = entityManager.createQuery(
                "select v " +
                        "from Visit v " +
                        "inner join fetch v.physician p " +
                        "where p.id = :physicianId " +
                        "and v.dateTimeStart >= :dateTimeStart " +
                        "and v.dateTimeEnd <= :dateTimeEnd",
                Visit.class)
                .setParameter("physicianId", workSchedule.getPhysician().getId())
                .setParameter("dateTimeStart", workSchedule.getDateTimeStart())
                .setParameter("dateTimeEnd", workSchedule.getDateTimeEnd())
                .getResultList();

        return visitList;
    }

    public boolean isBeforeOrEquals(LocalDateTime d1, LocalDateTime d2) {
        if (d1.isBefore(d2)) return true;
        if (d1.isEqual(d2)) return true;
        return false;
    }

    public boolean isAfterOrEquals(LocalDateTime d1, LocalDateTime d2) {
        if (d1.isAfter(d2)) return true;
        if (d1.isEqual(d2)) return true;
        return false;
    }

    public List<Slot> getAvailableSlotList(WorkSchedule workSchedule) {


        List<Slot> slotList = getAllPossibleSlotList(workSchedule);
        List<Visit> existingVisitList = getExistingVisitListForWorkScheduleEntry(workSchedule);

        for (Slot slotCandidate: slotList) {

            if (slotableFits(slotCandidate, existingVisitList)) {
                slotList.add(slotCandidate);
            }
        }

        return slotList;
    }

    public boolean slotableFits(Slotable slotable, WorkSchedule workSchedule) {
        List<Visit> existingVisitList = getExistingVisitListForWorkScheduleEntry(workSchedule);
        return slotableFits(slotable, existingVisitList);
    }

    private boolean slotableFits(Slotable slotable, List<Visit> existingVisitList) {

        for (Visit existingVisit: existingVisitList) {

            if (isBeforeOrEquals(slotable.getDateTimeStart(), existingVisit.getDateTimeStart()) && slotable.getDateTimeEnd().isAfter(existingVisit.getDateTimeStart())) {
                break;
            }

            if (slotable.getDateTimeStart().isBefore(existingVisit.getDateTimeEnd()) && isAfterOrEquals(slotable.getDateTimeEnd(), existingVisit.getDateTimeEnd())) {
                break;
            }

            if (isBeforeOrEquals(slotable.getDateTimeStart(), existingVisit.getDateTimeStart()) && isAfterOrEquals(slotable.getDateTimeEnd(), existingVisit.getDateTimeEnd())) {
                break;
            }

            return true;
        }

        return false;
    }

}
