package semato.semato_med.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfo {
    String email;

    String firstName;

    String lastName;
}
