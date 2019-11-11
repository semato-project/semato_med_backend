package semato.semato_med.model;

import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.jdbc.Work;
import org.hibernate.validator.constraints.UniqueElements;
import semato.semato_med.repository.ClinicRepository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String name;

    @NonNull
    private String city;

    @NonNull
    private String country;

    @NonNull
    private String postalCode;

    @NonNull
    private String street;

    @NonNull
    private String houseNumber;

    @NonNull
    @NaturalId
    private String email;

    private float latitude;

    private float longitude;

    private String imageUrl;

    private LocalDateTime deletedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "clinic_id")
    private List<WorkSchedule> workScheduleList;
}
