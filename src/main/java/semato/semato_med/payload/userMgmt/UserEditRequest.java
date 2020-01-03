package semato.semato_med.payload.userMgmt;

import lombok.Getter;

@Getter
public class UserEditRequest {

    private String firstName;

    private String lastName;

    private String phone;
}
