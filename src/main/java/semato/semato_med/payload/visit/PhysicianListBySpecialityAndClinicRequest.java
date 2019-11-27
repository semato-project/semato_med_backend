package semato.semato_med.payload.visit;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PhysicianListBySpecialityAndClinicRequest {

    @NotNull
    private Long specialityId;

    private Long clinicId;

}
