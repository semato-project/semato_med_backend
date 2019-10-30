package semato.semato_med.model.visit;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String city;

    @NonNull
    private String country;

    @NonNull
    private String postalCode;

    @NonNull
    private String street;

    @NonNull
    private String houseNumber;

    private float latitude;

    private float longitude;

    private String imageUrl;

    private LocalDateTime deletedAt;
}
