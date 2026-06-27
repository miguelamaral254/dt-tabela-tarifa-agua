package gruporas.dttabelatarifaagua.tariff.web.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateTariffTableRequest(
    UUID id,
    String name,
    LocalDate effectiveDate
) {}
