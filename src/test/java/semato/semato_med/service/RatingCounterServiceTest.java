package semato.semato_med.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import semato.semato_med.model.Physician;
import semato.semato_med.model.Visit;
import semato.semato_med.repository.PhysicianRepository;
import semato.semato_med.repository.VisitRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
public class RatingCounterServiceTest {

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private RatingCounterService ratingCounterService;

    @Autowired
    private VisitService visitService;

    @Autowired
    private VisitRepository visitRepository;

    @Test
    void getAvarangeOfRatingAndRoundItForExampleDoctor() {
        Optional<Physician> drStrange = physicianRepository.findById(2L);
        Optional<ArrayList<Visit>> visits = visitRepository.findByPhysician(drStrange.get());

       // visitService.rate();
        int avarange = ratingCounterService.countRatingForPhysician(drStrange.get());
        assertEquals(0, avarange);
    }
}
