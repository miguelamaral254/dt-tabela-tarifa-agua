package gruporas.dttabelatarifaagua.tariff.persistence.model;

import java.time.LocalDate;
import java.util.UUID;

public interface TariffTableSummaryProjection {
    UUID getTableId();
    String getTableName();
    LocalDate getTableEffectiveDate();
    UUID getUserId();
    String getUsername();
    String getFirstName();
    String getLastName();
}
