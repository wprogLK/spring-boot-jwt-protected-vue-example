package ch.wproglk.example.service.user;

import ch.wproglk.example.data.user.User;
import ch.wproglk.example.data.user.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@SuppressWarnings("squid:S2176")
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService
{
    private final UserDao userDao;

    @Override
    @Transactional
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userDao.findByUsername(username)
                           .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found."));

        return UserDetails.build(user);
    }
}
