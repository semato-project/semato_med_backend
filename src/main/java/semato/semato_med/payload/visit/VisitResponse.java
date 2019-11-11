package semato.semato_med.payload.visit;

import lombok.AllArgsConstructor;
import semato.semato_med.model.Clinic;
import semato.semato_med.model.Visit;

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

//    public Long getPhysicianId() {
//        return visit.sta;
//    }

}
