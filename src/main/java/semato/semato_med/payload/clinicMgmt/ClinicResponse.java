package semato.semato_med.payload.clinicMgmt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import semato.semato_med.model.Clinic;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ClinicResponse {
    private Long id;
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

    public ClinicResponse(Clinic clinic){
        this.id = clinic.getId();
        this.name = clinic.getName();
        this.city = clinic.getCity();
        this.country = clinic.getCountry();
        this.postalCode = clinic.getPostalCode();
        this.street = clinic.getStreet();
        this.houseNumber = clinic.getHouseNumber();
        this.email = clinic.getEmail();
        this.latitude = clinic.getLatitude();
        this.longitude = clinic.getLongitude();
        this.imageUrl = clinic.getImageUrl();
        this.deletedAt = clinic.getDeletedAt();
    }
}
