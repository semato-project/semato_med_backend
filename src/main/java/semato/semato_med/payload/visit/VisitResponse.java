package semato.semato_med.payload.visit;

import lombok.AllArgsConstructor;
import semato.semato_med.model.Clinic;
import semato.semato_med.model.Visit;
import semato.semato_med.model.VisitStatus;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@AllArgsConstructor
public class VisitResponse {
    private Visit visit;

    public Long getId() {
        return visit.getId();
    }

    public Long getClinicId() {
        return visit.getClinic().getId();
    }

    public String getClinicName() {
        return visit.getClinic().getName();
    }

    public Long getPhysicianId() {
        return visit.getPhysician().getId();
    }

    public String getPhysicianFullName() {
        return visit.getPhysician().getFullName();
    }

    public LocalDateTime getDateTimeStart() {
        return visit.getDateTimeStart();
    }

    public LocalDateTime getDateTimeEnd() {
        return visit.getDateTimeEnd();
    }

    public DayOfWeek getDayOfWeek() {
        return visit.getDateTimeStart().getDayOfWeek();
    }

    public Long getSpecialityId() {
        return visit.getSpeciality().getId();
    }

    public String getSpecialityName() {
        return visit.getSpeciality().getName();
    }

    public VisitStatus getStatus() {
        return visit.getStatus();
    }
}
