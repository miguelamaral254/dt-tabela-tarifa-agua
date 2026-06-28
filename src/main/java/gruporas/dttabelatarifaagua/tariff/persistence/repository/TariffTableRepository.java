package gruporas.dttabelatarifaagua.tariff.persistence.repository;

import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTable;
import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffCalculationProjection;
import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTableFullProjection;
import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTableSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TariffTableRepository extends JpaRepository<TariffTable, UUID> {

    @Query(value = """
            SELECT 
                tt.id AS tableId, tt.name AS tableName, tt.effective_date AS tableEffectiveDate,
                u.id AS userId, u.username AS username, u.first_name AS firstName, u.last_name AS lastName
            FROM tariff_table tt
            JOIN users u ON tt.created_by = u.id
            """,
            countQuery = "SELECT count(*) FROM tariff_table tt",
            nativeQuery = true)
    Page<TariffTableSummaryProjection> findAllSummaryProjected(Pageable pageable);

    @Query(value = """
            SELECT 
                tt.id AS tableId, tt.name AS tableName, tt.effective_date AS tableEffectiveDate,
                u.id AS userId, u.username AS username, u.first_name AS firstName, u.last_name AS lastName,
                cr.id AS rangeId, cr.start_range AS startRange, cr.end_range AS endRange, cr.unit_value AS unitValue,
                cc.id AS categoryId, cc.name AS categoryName
            FROM tariff_table tt
            JOIN users u ON tt.created_by = u.id
            JOIN consumption_range cr ON cr.tariff_table_id = tt.id
            JOIN consumer_category cc ON cr.consumer_category_id = cc.id
            WHERE tt.id = :id
            ORDER BY cc.name ASC, cr.start_range ASC
            """, nativeQuery = true)
    List<TariffTableFullProjection> findByIdProjected(@Param("id") UUID id);

    @Query(value = """
            SELECT 
                tt.id AS tableId, tt.name AS tableName, tt.effective_date AS tableEffectiveDate,
                u.id AS userId, u.username AS username, u.first_name AS firstName, u.last_name AS lastName,
                cr.id AS rangeId, cr.start_range AS startRange, cr.end_range AS endRange, cr.unit_value AS unitValue,
                cc.id AS categoryId, cc.name AS categoryName
            FROM tariff_table tt
            JOIN users u ON tt.created_by = u.id
            JOIN consumption_range cr ON cr.tariff_table_id = tt.id
            JOIN consumer_category cc ON cr.consumer_category_id = cc.id
            WHERE tt.id = (SELECT id FROM tariff_table ORDER BY effective_date DESC LIMIT 1)
            ORDER BY cc.name ASC, cr.start_range ASC
            """, nativeQuery = true)
    List<TariffTableFullProjection> findCurrentProjected();

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

    boolean existsByEffectiveDate(java.time.LocalDate effectiveDate);
}
