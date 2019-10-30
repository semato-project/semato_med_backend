package semato.semato_med.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private User user;

    private String pesel;

    private LocalDate birthDate;

    private String postalCode;

    private String city;

    private String street;

    private String houseNumber;

}
