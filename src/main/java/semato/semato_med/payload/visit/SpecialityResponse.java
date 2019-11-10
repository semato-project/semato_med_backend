package semato.semato_med.payload.visit;

import lombok.AllArgsConstructor;
import semato.semato_med.model.Speciality;

@AllArgsConstructor
public class SpecialityResponse {
    private Speciality speciality;

    public long getId() {
        return speciality.getId();
    }

    public String getName() {
        return speciality.getName();
    }


}
