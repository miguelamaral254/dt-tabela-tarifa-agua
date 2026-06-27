package gruporas.dttabelatarifaagua.user.persistence.repository;

import gruporas.dttabelatarifaagua.user.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
