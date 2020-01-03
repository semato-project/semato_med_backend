package semato.semato_med.payload.userMgmt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import semato.semato_med.model.Role;
import semato.semato_med.model.User;

import java.util.Set;

@Getter
@AllArgsConstructor
public class GetUserResponse {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private Set<Role> roles;

    public GetUserResponse(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.roles = user.getRoles();
    }
}
