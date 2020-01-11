package semato.semato_med.payload.visit;

import semato.semato_med.model.Physician;
import semato.semato_med.service.RatingCounterService;

import java.util.LinkedList;
import java.util.List;

public class PhysicianListResponse {

    private List<Physician> physicianList;

    public PhysicianListResponse(List<Physician> physicianList) {
        this.physicianList = physicianList;
    }

    public List<PhysicianResponse> getPhysicianList() {

        RatingCounterService ratingCounterService = new RatingCounterService();

        List<PhysicianResponse> response = new LinkedList<>();

        for (Physician physician: physicianList) {
            response.add(new PhysicianResponse(physician, ratingCounterService.countRatingForPhysician(physician)));
        }

        return response;
    }

}
