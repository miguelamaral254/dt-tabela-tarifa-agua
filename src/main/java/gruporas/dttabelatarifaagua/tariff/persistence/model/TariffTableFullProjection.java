package gruporas.dttabelatarifaagua.tariff.persistence.model;

import java.time.LocalDate;
import java.util.UUID;
import java.math.BigDecimal;

public interface TariffTableFullProjection {
    UUID getTableId();
    String getTableName();
    LocalDate getTableEffectiveDate();
    UUID getUserId();
    String getUsername();
    String getFirstName();
    String getLastName();
    UUID getRangeId();
    Integer getStartRange();
    Integer getEndRange();
    BigDecimal getUnitValue();
    UUID getCategoryId();
    String getCategoryName();
}
