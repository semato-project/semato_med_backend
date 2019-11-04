package semato.semato_med.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import semato.semato_med.models.User;

import java.util.Collection;

public class SecurityUser implements UserDetails {

    private User user;

    public SecurityUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return (user.getStatus() != User.Status.EXPIRED);
    }

    @Override
    public boolean isAccountNonLocked() {
        return (user.getStatus() != User.Status.LOCKED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return (user.getStatus() != User.Status.EXPIRED);
    }

    @Override
    public boolean isEnabled() {
        return (user.getStatus() == User.Status.ACTIVE);
    }


}
