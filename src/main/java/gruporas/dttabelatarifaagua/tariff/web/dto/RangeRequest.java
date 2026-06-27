package gruporas.dttabelatarifaagua.tariff.web.dto;

import java.math.BigDecimal;

public record RangeRequest(
    Integer start,
    Integer end,
    BigDecimal unitValue
) {}
