package gruporas.dttabelatarifaagua.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ConsumptionRangeResponse(
    UUID id,
    ConsumerCategoryResponse consumerCategory,
    Integer start,
    Integer end,
    BigDecimal unitValue
) {}
