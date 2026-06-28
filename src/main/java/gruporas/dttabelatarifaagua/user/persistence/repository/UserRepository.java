package gruporas.dttabelatarifaagua.user.persistence.repository;

import gruporas.dttabelatarifaagua.user.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import gruporas.dttabelatarifaagua.user.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByCpf(String cpf);
    Optional<User> findByEmail(String email);
}
