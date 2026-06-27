package gruporas.dttabelatarifaagua.tariff.persistence.repository;

import gruporas.dttabelatarifaagua.tariff.persistence.model.ConsumerCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConsumerCategoryRepository extends JpaRepository<ConsumerCategory, UUID> {
    Optional<ConsumerCategory> findByName(String name);
}
