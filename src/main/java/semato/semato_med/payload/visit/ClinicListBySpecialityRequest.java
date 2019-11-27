package semato.semato_med.payload.visit;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ClinicListBySpecialityRequest {

    @NotNull
    private Long specialityId;

}
