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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Order(2)
public class PhysicianLoader /*implements ApplicationRunner*/ {

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

    public static final String STRANGE_EXAMPLE_EMAIL = "strange@example.com";
    public static final String LECTER_EXAMPLE_EMAIL = "lecter@example.com";
    public static final String HOUSE_EXAMPLE_EMAIL = "house@example.com";
    public static final String NAJLEPSZY_EXAMPLE_EMAIL = "najlepszy@example.com";
    public static final String PASSWORD = "qwerty";

    public void run(ApplicationArguments args) {

        Speciality neurochirurgia = specialityRepository.findByName("Neurochirurgia").get();
        Speciality stomatologia = specialityRepository.findByName("Stomatologia").get();
        Speciality ortopedia = specialityRepository.findByName("Ortopedia").get();


        Set<Speciality> strangerSet = new HashSet<>();
        strangerSet.add(neurochirurgia);

        Physician strange = addPhysician(
                STRANGE_EXAMPLE_EMAIL,
                PASSWORD,
                "Stephen",
                "Strange",
                "123-123-123",
                "doktor",
                "Stephen Strange (Benedict Cumberbatch) jest aroganckim i ambitnym neurochirurgiem.",
                "Dr",
                "https://upload.wikimedia.org/wikipedia/en/0/0a/Benedict_Cumberbatch_as_Doctor_Strange.jpg",
                strangerSet
        );

        setWorkSchedule(-2, strange, ClinicLoader.CENTRUM_ZDROWIA_SZCZESCIA_I_POMYSLNOSCI_EXAMPLE_EMAIL);
        setWorkSchedule(1, strange, ClinicLoader.CENTRUM_ZDROWIA_SZCZESCIA_I_POMYSLNOSCI_EXAMPLE_EMAIL);
        setWorkSchedule(2, strange, ClinicLoader.LESNA_GORA_EMAIL);
        setWorkSchedule(3, strange, ClinicLoader.LESNA_GORA_EMAIL);
        setWorkSchedule(4, strange, ClinicLoader.LESNA_GORA_EMAIL);

        Set<Speciality> lecterSet = new HashSet<>();
        lecterSet.add(neurochirurgia);
        lecterSet.add(stomatologia);

        Physician lecter = addPhysician(
                LECTER_EXAMPLE_EMAIL,
                PASSWORD,
                "Hannibal",
                "Lecter",
                "111-222-333",
                "doktor",
                "Hannibal Lecter przeżył piekło wojny, w czasie której został sierotą.",
                "Dr",
                "https://upload.wikimedia.org/wikipedia/commons/c/c6/Anthony_Hopkins_cropped_2009.jpg",
                lecterSet
        );

        setWorkSchedule(-2, lecter, ClinicLoader.ARKHAM_EMAIL);
        setWorkSchedule(2, lecter, ClinicLoader.CENTRUM_ZDROWIA_SZCZESCIA_I_POMYSLNOSCI_EXAMPLE_EMAIL);
        setWorkSchedule(3, lecter, ClinicLoader.ARKHAM_EMAIL);
        setWorkSchedule(4, lecter, ClinicLoader.ARKHAM_EMAIL);


        Set<Speciality> houseSet = new HashSet<>();
        houseSet.add(neurochirurgia);
        houseSet.add(stomatologia);
        houseSet.add(ortopedia);

        Physician house = addPhysician(
                HOUSE_EXAMPLE_EMAIL,
                PASSWORD,
                "Gregory",
                "House",
                "444-555-666",
                "doktor",
                "House jest synem zawodowego wojskowego, Johna House'a, który służył jako pilot US Marines oraz Blythe House, która zajmowała się domem.",
                "Dr",
                "https://upload.wikimedia.org/wikipedia/commons/b/bb/Hugh_Laurie_Actors_Guild.jpg",
                houseSet
        );

        setWorkSchedule(-2, house, ClinicLoader.CENTRUM_ZDROWIA_SZCZESCIA_I_POMYSLNOSCI_EXAMPLE_EMAIL);
        setWorkSchedule(2, house, ClinicLoader.CENTRUM_ZDROWIA_SZCZESCIA_I_POMYSLNOSCI_EXAMPLE_EMAIL);
        setWorkSchedule(3, house, ClinicLoader.CENTRUM_ZDROWIA_SZCZESCIA_I_POMYSLNOSCI_EXAMPLE_EMAIL);
        setWorkSchedule(4, house, ClinicLoader.LESNA_GORA_EMAIL);

        List<Speciality> specialitiesList = specialityRepository.findAll();
        Set<Speciality> specialitiesSet = new HashSet<>(specialitiesList);
        Physician najlepszy = addPhysician(
                NAJLEPSZY_EXAMPLE_EMAIL,
                PASSWORD,
                "Mirosław",
                "Najlepszy",
                "111-222-333",
                "doktor",
                "Mirosław jest profesorem Uniwersystetu w USA. Skończył wiele specjalności, dzięki którym może leczyć na wielu płaszczyznach",
                "Prof.",
                "https://upload.wikimedia.org/wikipedia/commons/7/75/Prof_Derek_Bell%2C_President_of_the_Royal_College_of_Physicians_of_Edinburgh.jpg",
                specialitiesSet
        );

        setWorkSchedule(-2, najlepszy, ClinicLoader.CENTRUM_ZDROWIA_SZCZESCIA_I_POMYSLNOSCI_EXAMPLE_EMAIL);
        setWorkSchedule(2, najlepszy, ClinicLoader.CENTRUM_ZDROWIA_SZCZESCIA_I_POMYSLNOSCI_EXAMPLE_EMAIL);
        setWorkSchedule(3, najlepszy, ClinicLoader.ARKHAM_EMAIL);
        setWorkSchedule(4, najlepszy, ClinicLoader.LESNA_GORA_EMAIL);

    }

    private Physician addPhysician(
            String email,
            String password,
            String firstName,
            String lastName,
            String phoneNumber,
            String medicalDegrees,
            String note,
            String title,
            String imageUrl,
            Set<Speciality> specialities
    ) {
        if (!userRepository.findByEmail(email).isPresent()) {

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

            physician.setSpecialitySet(specialities);

            physician.setUser(user);
            physicianRepository.save(physician);

            return physician;
        }

        return null;
    }

    public void setWorkSchedule(int workDayAfterDays, Physician physician, String clinicEmail) // po ilu dniach licząc od dzisiaj dodać mu dzień pracy w grafiku
    {
        WorkSchedule workScheduleEntry = new WorkSchedule();
        workScheduleEntry.setPhysician(physician);
        workScheduleEntry.setClinic(clinicRepository.findByEmail(clinicEmail).get());
        workScheduleEntry.setDateTimeStart(LocalDateTime.of(LocalDate.now().plusDays(workDayAfterDays), LocalTime.NOON.minusHours(4)));
        workScheduleEntry.setDateTimeEnd(LocalDateTime.of(LocalDate.now().plusDays(workDayAfterDays), LocalTime.NOON.plusHours(4)));

        workScheduleRepository.save(workScheduleEntry);
    }
}
