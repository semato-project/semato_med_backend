package semato.semato_med.payload.visit;

import semato.semato_med.model.Visit;

import java.util.List;

public class AvailableVisitListResponse extends VisitListResponse {

    public AvailableVisitListResponse(List<Visit> visitList) {
        super(visitList);
    }

    public List<VisitResponse> getAvailableVisitList() {
        return super.getVisitList();
    }

}
