package semato.semato_med.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Physician {

    @Id
    private long id;

    @NonNull
    private String medicalDegrees;

    @NonNull
    private String title;

    private String note;

    private String image_url;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "id", referencedColumnName = "id")
    @MapsId
    @NonNull
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "physician_speciality",
            joinColumns = {@JoinColumn(name = "physician_id")},
            inverseJoinColumns = {@JoinColumn(name = "speciality_id")}
    )
    private Set<Speciality> specialities;

}
