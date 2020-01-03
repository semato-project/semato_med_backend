package semato.semato_med.model;

import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.jdbc.Work;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.lang.Nullable;
import semato.semato_med.repository.ClinicRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Clinic implements Serializable {
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
    private Set<WorkSchedule> workScheduleSet;

    public Clinic(@NonNull String name, @NonNull String city, @NonNull String country, @NonNull String postalCode, @NonNull String street, @NonNull String houseNumber, @NonNull String email, @Nullable float latitude, @Nullable float longitude, @Nullable String imageUrl) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
    }
}
