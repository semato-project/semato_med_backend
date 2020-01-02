package semato.semato_med.payload.clinic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ClinicAddingRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String street;

    @NotBlank
    private String houseNumber;

    @NotBlank
    private String email;

    private float latitude;

    private float longitude;

    private String imageUrl;

    private LocalDateTime deletedAt;
}
