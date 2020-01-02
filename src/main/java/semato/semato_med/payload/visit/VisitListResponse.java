package semato.semato_med.payload.visit;

import semato.semato_med.model.Visit;

import java.util.LinkedList;
import java.util.List;

public class VisitListResponse {

    private List<Visit> visitListInternal;

    public VisitListResponse(List<Visit> visitList) {
        this.visitListInternal = visitList;
    }

    public List<VisitResponse> getVisitList() {

        List<VisitResponse> response = new LinkedList<VisitResponse>();

        for (Visit visit: visitListInternal) {
            response.add(new VisitResponse(visit));
        }

        return response;
    }

}
