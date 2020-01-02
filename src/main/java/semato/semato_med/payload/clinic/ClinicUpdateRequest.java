package semato.semato_med.payload.clinic;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ClinicUpdateRequest {


    private String name;

    private String city;

    private String country;

    private String postalCode;

    private String street;

    private String houseNumber;

    private String email;

    private Float latitude;

    private Float longitude;

    private String imageUrl;

    private LocalDateTime deletedAt;
}

