package semato.semato_med.payload.visit;

import lombok.AllArgsConstructor;
import semato.semato_med.model.Physician;

@AllArgsConstructor
public class PhysicianResponse {
    private Physician physician;
    private Integer rating;

    public long getId() {
        return physician.getId();
    }

    public String getName() {
        return physician.getFullName();
    }

    public int getRating(){
        return this.rating;
    }
}
