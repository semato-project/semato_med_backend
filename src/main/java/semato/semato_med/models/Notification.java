package semato.semato_med.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String note;

    @ManyToOne
    @JoinColumn(name="admin_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private Admin createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;
}
