package gruporas.dttabelatarifaagua.web.dto;

import java.math.BigDecimal;

public record TariffCalculationDetailResponse(
    FaixaRangeResponse faixa,
    Integer m3Cobrados,
    BigDecimal unitValue,
    BigDecimal subtotal
) {}
