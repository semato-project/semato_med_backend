package semato.semato_med.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import semato.semato_med.model.Visit;
import semato.semato_med.model.VisitStatus;
import semato.semato_med.repository.VisitRepository;
import semato.semato_med.service.EmailSender;
import semato.semato_med.service.VisitService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MailerThread extends Thread {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    VisitService visitService;

    @Autowired
    EmailSender emailSender;

    @Override
    public void run() {

        while (true) {

            LocalDateTime now = LocalDateTime.now();

            System.out.println("MailerThread! now: " + now);

            LocalDateTime tomorrow = now.plusDays(1).withNano(0);

            LocalDateTime frameStart = tomorrow.withSecond(0).withNano(0);
            LocalDateTime frameEnd = tomorrow.withSecond(59).withNano(999999999);

            Optional<ArrayList<Visit>> visitListOptional =  visitRepository.findVisitsByStartTimeFrameAndStatus(frameStart, frameEnd, VisitStatus.RESERVED);

            if (visitListOptional.isPresent()) {
                for (Visit visit: visitListOptional.get()) {
                    SimpleMailMessage mail = visitService.constructVisitReminderEmail(visit.getPatient(), visit);
                    emailSender.send(mail);
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
