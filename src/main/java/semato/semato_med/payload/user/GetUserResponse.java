package semato.semato_med.payload.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import semato.semato_med.model.Role;

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
}
