package gruporas.dttabelatarifaagua.tariff.web.dto;

public record TariffCalculationRequest(
    String category,
    Integer consumption
) {}
