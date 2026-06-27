package gruporas.dttabelatarifaagua.persistence.repository;

import gruporas.dttabelatarifaagua.persistence.model.TariffTable;
import gruporas.dttabelatarifaagua.persistence.model.TariffCalculationProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TariffTableRepository extends JpaRepository<TariffTable, UUID> {
    @Query("SELECT t FROM TariffTable t LEFT JOIN FETCH t.consumptionRanges fc WHERE fc IS NOT NULL ORDER BY t.effectiveDate DESC")
    List<TariffTable> findTopWithConsumptionRangesOrderByEffectiveDateDesc(Pageable pageable);

    boolean existsByEffectiveDate(java.time.LocalDate effectiveDate);

    @Query(value = """
            SELECT 
                fc.start_range as start, 
                fc.end_range as end, 
                fc.unit_value as unitValue,
                CAST((LEAST(:consumption, fc.end_range) - fc.start_range + (CASE WHEN fc.start_range = 0 THEN 0 ELSE 1 END)) AS NUMERIC(19, 2)) as consumedM3,
                CAST(((LEAST(:consumption, fc.end_range) - fc.start_range + (CASE WHEN fc.start_range = 0 THEN 0 ELSE 1 END)) * fc.unit_value) AS NUMERIC(19, 2)) as subtotal
            FROM consumption_range fc
            JOIN tariff_table tt ON fc.tariff_table_id = tt.id
            JOIN consumer_category cc ON fc.consumer_category_id = cc.id
            WHERE cc.name = :categoryName
            AND tt.id = (SELECT id FROM tariff_table ORDER BY effective_date DESC LIMIT 1)
            AND :consumption >= fc.start_range
            ORDER BY fc.start_range ASC
            """, nativeQuery = true)
    List<TariffCalculationProjection> calculateTariffDetails(@Param("categoryName") String categoryName, @Param("consumption") Integer consumption);
}
