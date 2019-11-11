package semato.semato_med.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semato.semato_med.model.Clinic;
import semato.semato_med.model.Speciality;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitHelper {

    @Autowired
    EntityManager entityManager;

    public List<Clinic> getClinicListBySpeciality(Speciality speciality) {

        List<Clinic> clinicList = entityManager.createQuery(
                "select c " +
                        "from Clinic c " +
                        "inner join fetch c.workScheduleList wsl " +
                        "inner join fetch wsl.physician p " +
                        "inner join fetch p.specialityList s " +
                        "where s.id = :specialityId " +
                        "and wsl.dateEnd > :now",
                Clinic.class)
                .setParameter("specialityId", speciality.getId())
                .setParameter("now", LocalDateTime.now())
                .getResultList();

        return clinicList;
    }

}
