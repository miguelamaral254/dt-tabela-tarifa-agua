package gruporas.dttabelatarifaagua.persistence.repository;

import gruporas.dttabelatarifaagua.persistence.model.TariffTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TariffTableRepository extends JpaRepository<TariffTable, UUID> {
    @Query("SELECT t FROM TariffTable t LEFT JOIN FETCH t.consumptionRanges fc WHERE fc IS NOT NULL ORDER BY t.effectiveDate DESC")
    List<TariffTable> findTopWithFaixasConsumoOrderByDataVigenciaDesc(Pageable pageable);

    @Query(value = """
            SELECT 
                fc.start as start, 
                fc.end as end, 
                fc.valor_unitario as unitValue,
                (LEAST(:consumption, fc.end) - fc.start) as m3Cobrados,
                (LEAST(:consumption, fc.end) - fc.start) * fc.valor_unitario as subtotal
            FROM faixa_consumo fc
            JOIN tabela_tarifaria tt ON fc.tabela_tarifaria_id = tt.id
            JOIN categoria_consumidor cc ON fc.categoria_consumidor_id = cc.id
            WHERE cc.name = :categoryName
            AND tt.id = (SELECT id FROM tabela_tarifaria ORDER BY data_vigencia DESC LIMIT 1)
            AND :consumption > fc.start
            ORDER BY fc.start ASC
            """, nativeQuery = true)
    List<TariffCalculationProjection> calculateTariffDetails(@Param("categoryName") String categoryName, @Param("consumption") Integer consumption);
}

