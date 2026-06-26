package gruporas.dttabelatarifaagua.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ConsumptionRangeRequest(
    ConsumerCategoryRequest consumerCategory,
    Integer start,
    Integer end,
    BigDecimal unitValue
) {}
