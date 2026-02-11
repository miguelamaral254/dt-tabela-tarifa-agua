package gruporas.dttabelatarifaagua.repository;

import gruporas.dttabelatarifaagua.model.CategoriaConsumidor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoriaConsumidorRepository extends JpaRepository<CategoriaConsumidor, UUID> {
    Optional<CategoriaConsumidor> findByNome(String nome);
}
