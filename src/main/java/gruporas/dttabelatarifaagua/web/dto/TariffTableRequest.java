package gruporas.dttabelatarifaagua.web.dto;

import java.time.LocalDate;
import java.util.List;

public record TariffTableRequest(
    String name,
    LocalDate effectiveDate,
    List<ConsumptionRangeRequest> consumptionRanges
) {}
