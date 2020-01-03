package semato.semato_med.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semato.semato_med.model.Visit;
import semato.semato_med.repository.VisitRepository;

import java.util.Optional;

@Service
public class VisitManagementService {

    @Autowired
    private VisitRepository visitRepository;

    public Visit getById(Long id){
        Optional<Visit> visitOptional = visitRepository.findById(id);
        return visitOptional.orElse(null);

    }
}
