package semato.semato_med.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semato.semato_med.model.Speciality;

import java.util.Optional;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Integer> {

    Optional<Speciality> findById(Long specialityId);
    Optional<Speciality> findByName(String name);

}
