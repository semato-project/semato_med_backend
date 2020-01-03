package semato.semato_med.payload.clinicMgmt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ClinicResponse {

    Long id;

    private String name;

    private String city;

    private String country;

    private String postalCode;

    private String street;

    private String houseNumber;

    private String email;

    private float latitude;

    private float longitude;

    private String imageUrl;

    private LocalDateTime deletedAt;
}
