package semato.semato_med.model;

import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "id", referencedColumnName = "id")
    @MapsId
    @NonNull
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "physician_speciality",
            joinColumns = {@JoinColumn(name = "physician_id")},
            inverseJoinColumns = {@JoinColumn(name = "speciality_id")}
    )
    private Set<Speciality> specialitySet;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "physician_id")
    private Set<WorkSchedule> workScheduleSet;

     public Physician(@NonNull User user, @NonNull String medicalDegrees, @NonNull String title, @Nullable String note, @Nullable String image_url) {
        this.user = user;
        this.medicalDegrees = medicalDegrees;
        this.title = title;
        this.note = note;
        this.image_url = image_url;
    }

    public String getFullName() {
        return
                title
                + " " + user.getFirstName()
                + " " + user.getLastName()
                ;
    }

}
