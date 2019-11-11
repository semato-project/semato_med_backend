package semato.semato_med.payload.visit;

import semato.semato_med.model.Physician;

import java.util.LinkedList;
import java.util.List;

public class PhysicianListResponse {

    private List<Physician> physicianList;

    public PhysicianListResponse(List<Physician> physicianList) {
        this.physicianList = physicianList;
    }

    public List<PhysicianResponse> getClinicList() {

        List<PhysicianResponse> response = new LinkedList<PhysicianResponse>();

        for (Physician physician: physicianList) {
            response.add(new PhysicianResponse(physician));
        }

        return response;
    }

}
