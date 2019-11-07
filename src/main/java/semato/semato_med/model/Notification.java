package semato.semato_med.model;

import lombok.*;
import semato.semato_med.model.audit.DateAudit;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Notification extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String note;

    @ManyToOne
    @JoinColumn(name="admin_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private Admin createdBy;
}
