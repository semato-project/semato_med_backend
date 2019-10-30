package semato.semato_med.model.user.admin;

import lombok.*;
import semato.semato_med.model.user.User;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Admin {

    @Id
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "id", referencedColumnName = "id")
    @MapsId
    @NonNull
    private User user;
}
