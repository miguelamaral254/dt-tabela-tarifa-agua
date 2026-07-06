package gruporas.dttabelatarifaagua.tariff.persistence.repository;

import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTable;
import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTableFullProjection;
import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTableSummaryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TariffTableRepository extends JpaRepository<TariffTable, UUID> {

    @Query(value = """
            SELECT 
                tt.id AS tableId, tt.name AS tableName, tt.effective_date AS tableEffectiveDate,
                u.id AS userId, u.username AS username, u.first_name AS firstName, u.last_name AS lastName
            FROM tariff_table tt
            JOIN users u ON tt.created_by = u.id
            LIMIT :pageSize OFFSET :offset
            """,
            nativeQuery = true)
    List<TariffTableSummaryProjection> findAllSummaryProjected(int pageSize, int offset);

    @Query(value = "SELECT count(*) FROM tariff_table tt JOIN users u ON tt.created_by = u.id", nativeQuery = true)
    long countAllSummary();

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
    List<TariffTableFullProjection> findByIdProjected(UUID id);

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
            WHERE tt.effective_date = (SELECT MAX(effective_date) FROM tariff_table)
            ORDER BY cc.name ASC, cr.start_range ASC
            """, nativeQuery = true)
    List<TariffTableFullProjection> findCurrentProjected();

    TariffTable findTopByOrderByEffectiveDateDesc();

    boolean existsByEffectiveDate(java.time.LocalDate effectiveDate);
}
