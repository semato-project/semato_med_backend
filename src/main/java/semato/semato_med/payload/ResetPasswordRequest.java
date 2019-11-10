package semato.semato_med.payload;

import lombok.Getter;

import javax.validation.constraints.NotBlank;


@Getter
public class ResetPasswordRequest {

    @NotBlank
    private long id;

    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;

}
