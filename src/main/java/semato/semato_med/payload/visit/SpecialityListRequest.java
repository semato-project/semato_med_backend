package semato.semato_med.payload.visit;

import lombok.Getter;
import lombok.Setter;
import semato.semato_med.model.Speciality;

import java.util.List;

@Setter
@Getter
public class SpecialityListRequest {

    private List<Speciality> specialityList;

}
