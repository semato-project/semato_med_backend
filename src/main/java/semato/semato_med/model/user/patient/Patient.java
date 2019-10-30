package semato.semato_med.model.user.patient;

import lombok.*;
import semato.semato_med.model.user.User;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
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

    @NonNull
    private LocalDate birthDate;

    private String postalCode;

    private String city;

    private String street;

    private String houseNumber;

}
