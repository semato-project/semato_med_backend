package semato.semato_med.payload.visit;

import lombok.AllArgsConstructor;
import semato.semato_med.model.Physician;

@AllArgsConstructor
public class PhysicianResponse {
    private Physician physician;

    public long getId() {
        return physician.getId();
    }

    public String getName() {
        return physician.getFullName();
    }

}
