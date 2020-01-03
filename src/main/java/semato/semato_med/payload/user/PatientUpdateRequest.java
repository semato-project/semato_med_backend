package semato.semato_med.payload.user;

import lombok.Getter;

@Getter
public class PatientUpdateRequest {

    private String firstName;

    private String lastName;

    private String phone;

    private String postalCode;

    private String city;

    private String street;

    private String houseNumber;

}
