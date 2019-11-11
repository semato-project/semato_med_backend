package semato.semato_med.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.xml.internal.ws.developer.Serialization;
import lombok.*;
import semato.semato_med.model.audit.DateAudit;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Visit extends DateAudit {

    public static final int VISIT_LENGHT_SECONDS = 60 * 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn (name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @ManyToOne
    @JoinColumn (name = "physician_id", referencedColumnName = "id")
    private Physician physician;

    @ManyToOne
    @JoinColumn (name = "clinic_id", referencedColumnName = "id")
    private Clinic clinic;

    private LocalDateTime dateTimeStart;

    private LocalDateTime dateTimeEnd;

    private VisitStatus status;

}
