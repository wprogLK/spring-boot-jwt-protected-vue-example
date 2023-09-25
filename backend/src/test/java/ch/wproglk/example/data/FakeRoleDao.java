package ch.wproglk.example.data;

import ch.wproglk.example.data.role.Role;
import ch.wproglk.example.data.role.RoleDao;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("test")
public class FakeRoleDao extends FakeJpaRepository<Role, Long> implements RoleDao
{
    public FakeRoleDao()
    {
        for (ch.wproglk.example.model.Role r : ch.wproglk.example.model.Role.values())
        {
            Role role = new Role();
            role.setName(r);

            save(role);
        }
    }

    @Override
    public Optional<Role> findByName(ch.wproglk.example.model.Role name)
    {
        return db.values().stream()
                 .filter(r -> r.getName().equals(name))
                 .findFirst();
    }

    @Override
    public Role save(Role role)
    {
        if (role.getId() == null)
        {
            role.setId(getNextId());
        }

        db.put(role.getId().longValue(), role);
        return role;
    }
}
