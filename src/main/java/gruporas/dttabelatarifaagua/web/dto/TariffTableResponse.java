package gruporas.dttabelatarifaagua.web.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TariffTableResponse(
    UUID id,
    String name,
    LocalDate effectiveDate,
    List<ConsumptionRangeResponse> consumptionRanges
) {}
