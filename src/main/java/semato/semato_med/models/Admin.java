package semato.semato_med.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Admin {

    @Id
    private long id;

    @OneToOne
    @JoinColumn (name = "id", referencedColumnName = "id")
    @MapsId
    private User user;
}
