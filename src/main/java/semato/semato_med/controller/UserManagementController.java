package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semato.semato_med.exception.AppException;
import semato.semato_med.model.*;
import semato.semato_med.payload.ApiResponse;
import semato.semato_med.payload.crud.PhysicianAddingRequest;
import semato.semato_med.repository.PhysicianRepository;
import semato.semato_med.repository.RoleRepository;
import semato.semato_med.repository.SpecialityRepository;
import semato.semato_med.repository.UserRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserManagementController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SpecialityRepository specialityRepository;

    @PutMapping("/user/physician/add")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> addPhysician(@RequestBody PhysicianAddingRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

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

        Role userRole = roleRepository.findByName(RoleName.ROLE_PHYSICIAN).orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        physicianRepository.save(physician);

        return new ResponseEntity<>(new ApiResponse(true, "User registered successfully"), HttpStatus.CREATED);
    }

}
