package semato.semato_med.model;

import lombok.*;
import semato.semato_med.model.audit.DateAudit;
import semato.semato_med.util.Slotable;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Visit extends DateAudit implements Slotable {

    public static final int VISIT_LENGHT_SECONDS = 60 * 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @ManyToOne
    @JoinColumn (name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @ManyToOne
    @JoinColumn (name = "physician_id", referencedColumnName = "id")
    private Physician physician;

    @ManyToOne
    @JoinColumn (name = "clinic_id", referencedColumnName = "id")
    private Clinic clinic;

    @ManyToOne
    @JoinColumn (name = "speciality_id", referencedColumnName = "id")
    private Speciality speciality;

    private LocalDateTime dateTimeStart;

    private LocalDateTime dateTimeEnd;

    private VisitStatus status;

}
