package gruporas.dttabelatarifaagua.mapper;

import gruporas.dttabelatarifaagua.dto.TariffCalculationResponse;
import gruporas.dttabelatarifaagua.dto.TariffCalculationDetailResponse;
import gruporas.dttabelatarifaagua.dto.FaixaRangeDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import gruporas.dttabelatarifaagua.service.CalculoService.CalculoResult;

@Component
public class CalculoMapper {

    public TariffCalculationResponse toCalculoResponse(String categoria, Integer consumoTotal, CalculoResult calculoResult) {
        List<TariffCalculationDetailResponse> detalhamentoDTO = calculoResult.getDetalhamento().stream()
                .map(originalFaixa -> new TariffCalculationDetailResponse(
                        new FaixaRangeDTO(originalFaixa.getFaixa().getInicio(), originalFaixa.getFaixa().getFim()),
                        originalFaixa.getM3Cobrados(),
                        originalFaixa.getValorUnitario(),
                        originalFaixa.getSubtotal()
                ))
                .collect(Collectors.toList());

        return new TariffCalculationResponse(
                categoria,
                consumoTotal,
                calculoResult.getValorTotal(),
                detalhamentoDTO
        );
    }
}
