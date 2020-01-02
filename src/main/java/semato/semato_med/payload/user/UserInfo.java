package semato.semato_med.payload.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import semato.semato_med.model.Role;

import java.util.Set;

@Getter
@AllArgsConstructor
public class UserInfo {
    String email;

    String firstName;

    String lastName;

    Set<Role> Role;
}
