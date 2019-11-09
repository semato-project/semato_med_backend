package semato.semato_med.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.payload.*;
import semato.semato_med.security.CurrentUser;
import semato.semato_med.security.UserPrincipal;


@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('PATIENT')")
    public UserInfo getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return new UserInfo(currentUser.getEmail(), currentUser.getName(), currentUser.getSurname());
    }




}
