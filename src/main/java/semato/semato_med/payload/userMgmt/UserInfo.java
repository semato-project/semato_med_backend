package semato.semato_med.payload.userMgmt;

import lombok.AllArgsConstructor;
import semato.semato_med.model.Role;
import semato.semato_med.model.User;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
public class UserInfo {

    private User user;

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public Long getId() {
        return user.getId();
    }

    public Set<Role> getRoles() {
        return user.getRoles();
    }

    public LocalDate getBirthDate() {
        if (user.getPatient() == null) {
            return null;
        }

        return user.getPatient().getBirthDate();
    }

    public String getCity() {
        if (user.getPatient() == null) {
            return null;
        }

        return user.getPatient().getCity();
    }

    public String getHouseNumber() {
        if (user.getPatient() == null) {
            return null;
        }

        return user.getPatient().getHouseNumber();
    }

    public String getPesel() {
        if (user.getPatient() == null) {
            return null;
        }

        return user.getPatient().getPesel();
    }

    public String getStreet() {
        if (user.getPatient() == null) {
            return null;
        }

        return user.getPatient().getStreet();
    }

    public String getPostalCode() {
        if (user.getPatient() == null) {
            return null;
        }

        return user.getPatient().getPostalCode();
    }

}
