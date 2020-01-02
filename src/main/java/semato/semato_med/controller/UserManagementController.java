package semato.semato_med.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import semato.semato_med.payload.ApiResponse;
import semato.semato_med.payload.user.GetUserResponse;
import semato.semato_med.payload.user.UserEditRequest;
import semato.semato_med.payload.user.PhysicianAddingRequest;

import semato.semato_med.repository.UserRepository;
import semato.semato_med.service.UserManagementService;

import java.util.List;


@RestController
@Secured({"ROLE_ADMIN"})
@RequestMapping("/api/mgmt/user")
public class UserManagementController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserManagementService userManagementService;

    @PutMapping("/physician/add")
    public ResponseEntity<?> addPhysician(@RequestBody PhysicianAddingRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        userManagementService.createPhysician(request);
        return new ResponseEntity<>(new ApiResponse(true, "User created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        List<GetUserResponse> all = userManagementService.getAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserEditRequest request) {
        if (!userRepository.existsById(userId)) {
            return new ResponseEntity<>(new ApiResponse(false, "User not found!"),
                    HttpStatus.NOT_FOUND);
        }

        userManagementService.edit(request, userId);
        return new ResponseEntity<>(new ApiResponse(true, "User has been edited"), HttpStatus.OK);
    }

}
