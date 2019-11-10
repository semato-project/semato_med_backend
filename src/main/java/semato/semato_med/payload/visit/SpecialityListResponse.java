package semato.semato_med.payload.visit;

import semato.semato_med.model.Speciality;

import java.util.LinkedList;
import java.util.List;

public class SpecialityListResponse {

    private List<Speciality> specialityList;

    public SpecialityListResponse(List<Speciality> specialityList) {
        this.specialityList = specialityList;
    }

    public List<SpecialityResponse> getSpecialityList() {

        List<SpecialityResponse> response = new LinkedList<SpecialityResponse>();

        for (Speciality speciality: specialityList) {
            response.add(new SpecialityResponse(speciality));
        }

        return response;
    }

}
