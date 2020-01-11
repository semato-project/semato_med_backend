package semato.semato_med.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semato.semato_med.model.Physician;
import semato.semato_med.model.Visit;
import semato.semato_med.repository.VisitRepository;

import java.util.ArrayList;

@Service
public class RatingCounterService {

    @Autowired
    private VisitRepository visitRepository;

    public int countRatingForPhysician(Physician physician){
        ArrayList<Visit> physicianVisitLists = visitRepository.findByRatingIsNotNullAndPhysician(physician);

        return (int) Math.round(physicianVisitLists.stream().mapToDouble(Visit::getRating).average().orElse(Double.NaN));

    }
}
