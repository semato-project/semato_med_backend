package semato.semato_med.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import semato.semato_med.model.audit.DateAudit;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WorkSchedule extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn (name = "physician_id", referencedColumnName = "id")
    private Physician physician;

    @ManyToOne
    @JoinColumn (name = "clinic_id", referencedColumnName = "id")
    private Clinic clinic;

    private LocalDateTime dateTimeStart;

    private LocalDateTime dateTimeEnd;

}
