package semato.semato_med.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import semato.semato_med.model.User;
import semato.semato_med.payload.userMgmt.UserInfo;
import semato.semato_med.security.CurrentUser;
import semato.semato_med.security.UserPrincipal;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('PATIENT') or hasRole('PHYSICIAN')")
    public UserInfo getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        User user = currentUser.getUser();
        return new UserInfo(user);
    }
}
