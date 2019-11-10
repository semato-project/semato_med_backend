package semato.semato_med.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
public class SingUpRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String pesel;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    //Nie wymagane:
    private String phone;

    private String postalCode;

    private String city;

    private String street;

    private String houseNumber;
}
