package semato.semato_med.payload.visit;

import lombok.AllArgsConstructor;
import semato.semato_med.model.Clinic;
import semato.semato_med.model.Visit;
import semato.semato_med.model.VisitStatus;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    public String getPhysicianName() {
        return visit.getPhysician().getFullName();
    }

    public LocalDateTime getDateTimeStart() {
        return visit.getDateTimeStart();
    }

    public LocalDateTime getDateTimeEnd() {
        return visit.getDateTimeEnd();
    }

    public String getDayOfWeek() {
        return visit.getDateTimeStart().format(DateTimeFormatter.ofPattern("EEEE", new Locale.Builder().setLanguage("pl").build()));
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

    public Long getPatientId() {
        if (visit.getPatient() == null) {
            return null;
        }

        return visit.getPatient().getId();
    }

    public String getPatientName() {
        if (visit.getPatient() == null) {
            return null;
        }

        return visit.getPatient().getFullName();
    }

    public String getPatientPesel() {
        if (visit.getPatient() == null) {
            return null;
        }

        return visit.getPatient().getPesel();
    }

}
