package gruporas.dttabelatarifaagua.tariff.persistence.model;

import java.time.LocalDate;
import java.util.UUID;

public interface TariffTableProjection {
    UUID getId();
    String getName();
    LocalDate getEffectiveDate();
    UUID getCreatedBy();
    String getCreatorUsername();
    String getCreatorFirstName();
    String getCreatorLastName();
}
