package semato.semato_med.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import semato.semato_med.exception.AppException;
import semato.semato_med.model.*;
import semato.semato_med.payload.userMgmt.GetUserResponse;
import semato.semato_med.payload.userMgmt.PhysicianAddingRequest;
import semato.semato_med.payload.userMgmt.UserEditRequest;
import semato.semato_med.repository.PhysicianRepository;
import semato.semato_med.repository.RoleRepository;
import semato.semato_med.repository.SpecialityRepository;
import semato.semato_med.repository.UserRepository;

import java.util.*;

@Service
public class UserManagementService {

    private final UserRepository userRepository;

    private final PhysicianRepository physicianRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final SpecialityRepository specialityRepository;

    public UserManagementService(UserRepository userRepository, PhysicianRepository physicianRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, SpecialityRepository specialityRepository) {
        this.userRepository = userRepository;
        this.physicianRepository = physicianRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.specialityRepository = specialityRepository;
    }

    public void createPhysician(PhysicianAddingRequest request){

        User user = new User(
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getPassword(),
                request.getPhone());

        Physician physician = new Physician( user,
                request.getMedicalDegrees(),
                request.getTitle(),
                request.getNote(),
                request.getImage_url());

        Set<Speciality> specialitySet = new HashSet<>();

        for(Long specialitesIds : request.getSpecialitiesIds()){
            Speciality speciality = specialityRepository.findById(specialitesIds).orElseThrow(() -> new AppException("Speciality not found!"));
            specialitySet.add(speciality);
        }

        physician.setSpecialitySet(specialitySet);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_PHYSICIAN).orElseThrow(() -> new AppException("Physician Role not set."));

        user.setRoles(Collections.singleton(userRole));

        physicianRepository.save(physician);

    }

    public List<GetUserResponse> getAll(){
        List<User> all = userRepository.findAll();
        List<GetUserResponse> userList = new ArrayList<>();
        for (User user : all) {
            userList.add(new GetUserResponse(user));
        }
        return userList;
    }

    public GetUserResponse getById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(GetUserResponse::new).orElse(null);

    }


    public void edit(UserEditRequest request, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            Optional.ofNullable(request.getFirstName()).ifPresent(userOptional.get()::setFirstName);
            Optional.ofNullable(request.getLastName()).ifPresent(userOptional.get()::setLastName);
            Optional.ofNullable(request.getPhone()).ifPresent(userOptional.get()::setPhone);
            userRepository.save(userOptional.get());
        }
    }


}
