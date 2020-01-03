package semato.semato_med.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semato.semato_med.model.Visit;
import semato.semato_med.payload.visitMgmt.VisitResponse;
import semato.semato_med.repository.VisitRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VisitManagementService {

    @Autowired
    private VisitRepository visitRepository;

    public VisitResponse getById(Long id) {
        Optional<Visit> visitOptional = visitRepository.findById(id);
        if (visitOptional.isPresent()) {
            return new VisitResponse(visitOptional.get());
        }
        return null;
    }

    public List<VisitResponse> getAll() {
        List<Visit> visitList = visitRepository.findAll();
        List<VisitResponse> visitResponses = new ArrayList<>();
        for (Visit visit : visitList) {
            visitResponses.add(new VisitResponse(visit));
        }

        return visitResponses;
    }
}
