package gruporas.dttabelatarifaagua.web.dto;

import java.math.BigDecimal;
import java.util.List;

public record TariffCalculationResponse(
    String categoria,
    Integer consumoTotal,
    BigDecimal valorTotal,
    List<TariffCalculationDetailResponse> detalhamento
) {}
