package ch.wproglk.example.data.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleDao extends JpaRepository<Role, Long>
{
    Optional<Role> findByName(ch.wproglk.example.model.Role name);
}

