package semato.semato_med.payload.visit;

import lombok.AllArgsConstructor;
import semato.semato_med.model.Clinic;
import semato.semato_med.model.Visit;
import semato.semato_med.model.VisitStatus;

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

    public Long getPhysicianId() {
        return visit.getPhysician().getId();
    }

    public LocalDateTime getDateTimeStart() {
        return visit.getDateTimeStart();
    }

    public LocalDateTime getDateTimeEnd() {
        return visit.getDateTimeEnd();
    }

    public Long getSpecialityId() {
        return visit.getSpeciality().getId();
    }

    public VisitStatus getStatus() {
        return visit.getStatus();
    }
}
