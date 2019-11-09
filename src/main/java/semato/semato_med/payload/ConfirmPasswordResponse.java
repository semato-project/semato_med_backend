package semato.semato_med.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ConfirmPasswordResponse {

    private Long id;

    private String token;

}
