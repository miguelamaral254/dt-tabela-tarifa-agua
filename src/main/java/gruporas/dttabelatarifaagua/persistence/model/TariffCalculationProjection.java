package gruporas.dttabelatarifaagua.persistence.model;

import java.math.BigDecimal;

public interface TariffCalculationProjection {
    Integer getInicio();
    Integer getFim();
    BigDecimal getValorUnitario();
    Integer getM3Cobrados();
    BigDecimal getSubtotal();
}
