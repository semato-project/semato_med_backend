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
        ArrayList<Visit> physicianVisitList = visitRepository.findByPhysicianAndRatingIsNotNull(physician);
        System.out.println(physicianVisitList);
        for (Visit visit : physicianVisitList) {
            System.out.println(visit.getRating());
        }

        return (int) Math.round(physicianVisitList.stream().mapToDouble(Visit::getRating).average().orElse(Double.NaN));

    }
}
