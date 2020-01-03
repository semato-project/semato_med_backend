package semato.semato_med.payload.user;

import lombok.Getter;

@Getter
public class PhysicianUpdateRequest {
    private String firstName;

    private String lastName;

    private String phone;

    private String medicalDegrees;

    private String title;

    private String note;

    private String image_url;
}
