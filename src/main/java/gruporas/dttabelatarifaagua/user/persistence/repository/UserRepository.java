package gruporas.dttabelatarifaagua.user.persistence.repository;

import gruporas.dttabelatarifaagua.user.persistence.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByCpf(String cpf);
    Optional<User> findByEmail(String email);

    @Query(value = """
            SELECT * FROM users u 
            WHERE (:role IS NULL OR u.role = :role)
            LIMIT :pageSize OFFSET :offset
            """,
            nativeQuery = true)
    java.util.List<User> findAllFiltered(@Param("role") String role, @Param("pageSize") int pageSize, @Param("offset") int offset);

    @Query(value = "SELECT count(*) FROM users u WHERE (:role IS NULL OR u.role = :role)", nativeQuery = true)
    long countAllFiltered(@Param("role") String role);
}
