package semato.semato_med.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Setter
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

    @NotBlank
    private LocalDate birthDate;


    //Nie wymagane:
    private String phone;

    private String postalCode;

    private String city;

    private String street;

    private String houseNumber;
}
