package dev.erikmota.usermanager.repository;

import dev.erikmota.usermanager.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    boolean existsByLoginAndId(String login, Long id);
    boolean existsByEmailAndId(String email, Long id);

    @Query("""
        SELECT u FROM User u
        WHERE ((:search IS NULL OR LOWER(u.name) LIKE %:search%)
          OR (:search IS NULL OR u.login LIKE %:search%)
          OR (:search IS NULL OR u.email LIKE %:search%))
          AND (:enabled iS NULL OR u.enabled = :enabled)
    """)
    Page<User> search(@Param("search") String search, @Param("enabled") Boolean enabled, Pageable pageable);
}
