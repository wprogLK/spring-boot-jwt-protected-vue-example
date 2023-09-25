package ch.wproglk.example.service.user;

import ch.wproglk.example.data.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = {"id"})
@SuppressWarnings("squid:S2176")
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails
{
    private Long id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetails build(User user)
    {
        List<SimpleGrantedAuthority> authorities = user.getRoles()
                                                       .stream()
                                                       .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                                                       .toList();

        return new UserDetails(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }
}
