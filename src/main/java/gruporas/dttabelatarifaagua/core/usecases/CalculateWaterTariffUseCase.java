package gruporas.dttabelatarifaagua.core.usecases;

import gruporas.dttabelatarifaagua.persistence.model.TariffCalculationProjection;
import gruporas.dttabelatarifaagua.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.shared.exception.ResourceNotFoundException;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.web.dto.FaixaRangeResponse;
import gruporas.dttabelatarifaagua.web.dto.TariffCalculationDetailResponse;
import gruporas.dttabelatarifaagua.web.dto.TariffCalculationRequest;
import gruporas.dttabelatarifaagua.web.dto.TariffCalculationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CalculateWaterTariffUseCase implements UseCase<TariffCalculationRequest, TariffCalculationResponse> {

    private final TariffTableRepository tabelaTarifariaRepository;

    @Override
    public TariffCalculationResponse execute(TariffCalculationRequest request) {
        var detailsProjections = tabelaTarifariaRepository.calculateTariffDetails(request.categoria(), request.consumo());

        if (detailsProjections.isEmpty()) {
            throw new ResourceNotFoundException("tariff.notFound");
        }

        BigDecimal totalValue = BigDecimal.ZERO;
        var detailResponses = new java.util.ArrayList<TariffCalculationDetailResponse>();

        for (var p : detailsProjections) {
            BigDecimal subtotal = p.getSubtotal();
            totalValue = totalValue.add(subtotal);
            
            detailResponses.add(new TariffCalculationDetailResponse(
                    new FaixaRangeResponse(p.getInicio(), p.getFim()),
                    p.getM3Cobrados(),
                    p.getValorUnitario(),
                    subtotal
            ));
        }

        return new TariffCalculationResponse(request.categoria(), request.consumo(), totalValue, detailResponses);
    }
}
