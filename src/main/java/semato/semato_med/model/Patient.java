package semato.semato_med.model;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Patient {
    @Id
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "id", referencedColumnName = "id")
    @MapsId
    @NonNull
    private User user;

    @NonNull
    private String pesel;

    @Column(columnDefinition = "DATE")
    @NonNull
    private LocalDate birthDate;

    @Nullable
    private String postalCode;

    @Nullable
    private String city;

    @Nullable
    private String street;

    @Nullable
    private String houseNumber;

    public Patient(@NonNull User user, @NonNull String pesel, @NonNull LocalDate birthDate, @Nullable String postalCode, @Nullable String city, @Nullable String street, @Nullable String houseNumber) {
        this.user = user;
        this.pesel = pesel;
        this.birthDate = birthDate;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
    }
}
