package semato.semato_med.models;

import lombok.*;

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

    @OneToOne
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
