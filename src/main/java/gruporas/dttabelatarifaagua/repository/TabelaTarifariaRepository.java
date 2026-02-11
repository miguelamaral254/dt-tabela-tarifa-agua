package gruporas.dttabelatarifaagua.repository;

import gruporas.dttabelatarifaagua.model.TabelaTarifaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TabelaTarifariaRepository extends JpaRepository<TabelaTarifaria, UUID> {
    @Query("SELECT t FROM TabelaTarifaria t LEFT JOIN FETCH t.faixasConsumo fc WHERE fc IS NOT NULL ORDER BY t.dataVigencia DESC")
    List<TabelaTarifaria> findTopWithFaixasConsumoOrderByDataVigenciaDesc(Pageable pageable);
}
