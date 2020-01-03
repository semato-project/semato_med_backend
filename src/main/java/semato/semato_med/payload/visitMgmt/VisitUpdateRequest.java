package semato.semato_med.payload.visitMgmt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import semato.semato_med.model.Visit;
import semato.semato_med.model.VisitStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class VisitUpdateRequest {
    private VisitStatus status;
    private LocalDateTime dateTimeStart;
    private LocalDateTime dateTimeEnd;
    private Long specialityId;
    private Long physicianId;
    private Long clinicId;
    private Long patientId;

    public VisitUpdateRequest(Visit visit){
        this.status = visit.getStatus();
        this.dateTimeStart = visit.getDateTimeStart();
        this.dateTimeEnd = visit.getDateTimeEnd();
        this.specialityId = visit.getSpeciality().getId();
        this.physicianId = visit.getPhysician().getId();
        this.clinicId = visit.getClinic().getId();
        this.patientId = visit.getPatient().getId();
    }
}
