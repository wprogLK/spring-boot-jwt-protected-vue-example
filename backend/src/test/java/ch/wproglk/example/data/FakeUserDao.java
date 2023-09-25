package ch.wproglk.example.data;

import ch.wproglk.example.data.user.User;
import ch.wproglk.example.data.user.UserDao;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Profile("test")
public class FakeUserDao extends FakeJpaRepository<User, Long> implements UserDao
{
    @Override
    public Optional<User> findByUsername(String username)
    {
        return db.values().stream()
                 .filter(u -> u.getUsername().equals(username))
                 .findFirst();
    }

    @Override
    public boolean existsByUsername(String username)
    {
        return findByUsername(username).isPresent();
    }

    @Override
    public boolean existsByEmail(String email)
    {
        return db.values().stream()
                 .anyMatch(u -> u.getEmail().equals(email));
    }

    @Override
    public User save(User user)
    {
        if (user.getId() == null)
        {
            user.setId((long) getNextId());
        }

        db.put(user.getId(), user);
        return user;
    }
}
