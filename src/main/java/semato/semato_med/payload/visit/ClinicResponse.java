package semato.semato_med.payload.visit;

import lombok.AllArgsConstructor;
import semato.semato_med.model.Clinic;

@AllArgsConstructor
public class ClinicResponse {
    private Clinic clinic;

    public long getId() {
        return clinic.getId();
    }

    public String getName() {
        return clinic.getName();
    }
}
