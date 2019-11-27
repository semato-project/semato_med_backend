package semato.semato_med.payload.visit;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
public class AvailableVisitListRequest {

    @NotNull
    private Long specialityId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodStart;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodEnd;

    private Long clinicId;

    private Long physicianId;

}
