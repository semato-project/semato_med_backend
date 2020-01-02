package semato.semato_med.payload.user;

import lombok.Getter;

@Getter
public class UserEditRequest {

    private String firstName;

    private String lastName;

    private String phone;
}
