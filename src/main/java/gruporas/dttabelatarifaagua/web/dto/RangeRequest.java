package gruporas.dttabelatarifaagua.web.dto;

import java.math.BigDecimal;

public record RangeRequest(
    Integer start,
    Integer end,
    BigDecimal unitValue
) {}
