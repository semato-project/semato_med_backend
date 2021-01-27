package semato.semato_med.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import semato.semato_med.model.*;
import semato.semato_med.service.WorkScheduleService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

class WorkScheduleServiceTest {

    @Autowired
    WorkScheduleService workScheduleService;

    @Test
    void visit_slots_are_generated_properly() {
        WorkSchedule workSchedule = new WorkSchedule();
        workSchedule.setDateTimeStart(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.NOON.minusHours(4)));
        workSchedule.setDateTimeEnd(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.NOON.plusHours(4)));

        assertEquals(16, workScheduleService.getAllPossibleSlotList(workSchedule).size());
    }

    @Test
    void visit_fits_when_there_is_room_for_it() {

        LinkedList<Visit> visitList = new LinkedList<>();
        visitList.add(getNthVisitInADay(1));
        visitList.add(getNthVisitInADay(3));

        assertTrue(workScheduleService.slotableFits(getNthVisitInADay(0), visitList));
        assertTrue(workScheduleService.slotableFits(getNthVisitInADay(2), visitList));
        assertTrue(workScheduleService.slotableFits(getNthVisitInADay(4), visitList));
    }

    @Test
    void visit_not_fits_when_there_is_no_room_for_it() {
        LinkedList<Visit> visitList = new LinkedList<>();
        visitList.add(getNthVisitInADay(1));
        visitList.add(getNthVisitInADay(3));

        assertFalse(workScheduleService.slotableFits(getNthVisitInADay(1), visitList));
        assertFalse(workScheduleService.slotableFits(getNthVisitInADay(3, -1, 1), visitList));
        assertFalse(workScheduleService.slotableFits(getNthVisitInADay(0, 0, 1), visitList));
        assertFalse(workScheduleService.slotableFits(getNthVisitInADay(2, -1, 1), visitList));
        assertFalse(workScheduleService.slotableFits(getNthVisitInADay(4, -1, 0), visitList));
    }

    private Visit getNthVisitInADay(int n){
        return getNthVisitInADay(n, 0, 0);
    }

    private Visit getNthVisitInADay(int n, int biasStartSeconds, int biasEndSeconds){

        Visit visit = new Visit();

        visit.setDateTimeStart(
                LocalDateTime.of(
                        LocalDate.now().plusDays(1),
                        LocalTime.NOON.minusHours(4).plusSeconds(n * Visit.VISIT_LENGHT_SECONDS + biasStartSeconds)
                )
        );

        visit.setDateTimeEnd(
                LocalDateTime.of(
                        LocalDate.now().plusDays(1),
                        LocalTime.NOON.minusHours(4).plusSeconds((n + 1) * Visit.VISIT_LENGHT_SECONDS + biasEndSeconds)
                )
        );

        return visit;
    }

}