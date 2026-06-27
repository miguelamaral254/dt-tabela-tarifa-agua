package gruporas.dttabelatarifaagua.persistence.model;

import java.math.BigDecimal;

public interface TariffCalculationProjection {
    Integer getStart();
    Integer getEnd();
    BigDecimal getUnitValue();
    Integer getConsumedM3();
    BigDecimal getSubtotal();
}
