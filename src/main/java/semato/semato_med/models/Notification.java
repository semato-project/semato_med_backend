package semato.semato_med.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String note;

    @ManyToOne
    @JoinColumn(name="admin_id", referencedColumnName = "id", nullable = false)
    private Admin createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;
}
