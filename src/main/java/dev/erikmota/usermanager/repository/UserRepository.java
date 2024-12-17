package dev.erikmota.usermanager.repository;

import dev.erikmota.usermanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    boolean existsByLoginAndId(String login, Long id);
    boolean existsByEmailAndId(String email, Long id);
}
