package semato.semato_med.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import semato.semato_med.exception.AppException;
import semato.semato_med.model.*;
import semato.semato_med.repository.*;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Component
@Order(2)
public class PhysicianLoader implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private SpecialityRepository specialityRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private WorkScheduleRepository workScheduleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static final String EMAIL = "strange@example.com";
    public static final String PASSWORD = "qwerty";

    @Override
    public void run(ApplicationArguments args) {

        Speciality speciality = specialityRepository.findByName("Neurochirurgia").get();

        addPhysician(
                EMAIL,
                PASSWORD,
                "Stephen",
                "Strange",
                "123-123-123",
                "doktor",
                "Stephen Strange (Benedict Cumberbatch) jest aroganckim i ambitnym neurochirurgiem.",
                "Dr",
                "https://upload.wikimedia.org/wikipedia/en/0/0a/Benedict_Cumberbatch_as_Doctor_Strange.jpg",
                speciality,
                1
        );

        speciality = specialityRepository.findByName("Stomatologia").get();

        addPhysician(
                "lecter@example.com",
                PASSWORD,
                "Hannibal",
                "Lecter",
                "111-222-333",
                "doktor",
                "Hannibal Lecter przeżył piekło wojny, w czasie której został sierotą.",
                "Dr",
                "https://upload.wikimedia.org/wikipedia/commons/c/c6/Anthony_Hopkins_cropped_2009.jpg",
                speciality,
                2
        );

        speciality = specialityRepository.findByName("Ortopedia").get();

        addPhysician(
                "house@example.com",
                PASSWORD,
                "Gregory",
                "House",
                "444-555-666",
                "doktor",
                "House jest synem zawodowego wojskowego, Johna House'a, który służył jako pilot US Marines oraz Blythe House, która zajmowała się domem.",
                "Dr",
                "https://upload.wikimedia.org/wikipedia/commons/b/bb/Hugh_Laurie_Actors_Guild.jpg",
                speciality,
                3
        );

    }

    private void addPhysician(
            String email,
            String password,
            String firstName,
            String lastName,
            String phoneNumber,
            String medicalDegrees,
            String note,
            String title,
            String imageUrl,
            Speciality speciality,
            int workDayAfterDays // po ilu dniach licząc od dzisiaj dodać mu dzień pracy w grafiku
    ) {
        if(! userRepository.findByEmail(email).isPresent()) {

            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPhone(phoneNumber);
            user.setPassword(passwordEncoder.encode(password));
            Role userRole = roleRepository.findByName(RoleName.ROLE_PHYSICIAN).orElseThrow(() -> new AppException("Phisician Role not set."));
            user.setRoles(Collections.singleton(userRole));

            Physician physician = new Physician();
            physician.setMedicalDegrees(medicalDegrees);
            physician.setNote(note);
            physician.setTitle(title);
            physician.setImage_url(imageUrl);

            Set<Speciality> specialitySet = new HashSet<>();

            specialitySet.add(speciality);
            physician.setSpecialitySet(specialitySet);

            physician.setUser(user);
            physicianRepository.save(physician);

            WorkSchedule workScheduleEntry = new WorkSchedule();
            workScheduleEntry.setPhysician(physician);
            workScheduleEntry.setClinic(clinicRepository.findByEmail(ClinicLoader.EMAIL).get());
            workScheduleEntry.setDateTimeStart(LocalDateTime.of(LocalDate.now().plusDays(workDayAfterDays), LocalTime.NOON.minusHours(4)));
            workScheduleEntry.setDateTimeEnd(LocalDateTime.of(LocalDate.now().plusDays(workDayAfterDays), LocalTime.NOON.plusHours(4)));

            workScheduleRepository.save(workScheduleEntry);
        }
    }
}
