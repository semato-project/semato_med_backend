package semato.semato_med.payload.visit;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class BookVisitRequest {

    @NotNull
    private Long specialityId;

    @NotNull
    private Long clinicId;

    @NotNull
    private Long physicianId;

    @NotNull
    private LocalDateTime dateTimeStart;

    @NotNull
    private LocalDateTime dateTimeEnd;

}
