package gruporas.dttabelatarifaagua.core.usecases;

import gruporas.dttabelatarifaagua.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.shared.exception.ResourceNotFoundException;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.web.dto.RangeResponse;
import gruporas.dttabelatarifaagua.web.dto.TariffCalculationDetailResponse;
import gruporas.dttabelatarifaagua.web.dto.TariffCalculationRequest;
import gruporas.dttabelatarifaagua.web.dto.TariffCalculationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CalculateWaterTariffUseCase implements UseCase<TariffCalculationRequest, TariffCalculationResponse> {

    private final TariffTableRepository tariffTableRepository;

    @Override
    public TariffCalculationResponse execute(TariffCalculationRequest request) {
        var detailsProjections = tariffTableRepository.calculateTariffDetails(request.category(), request.consumption());

        if (detailsProjections.isEmpty()) {
            throw new ResourceNotFoundException("tariff.notFound");
        }

        BigDecimal totalValue = BigDecimal.ZERO;
        var detailResponses = new ArrayList<TariffCalculationDetailResponse>();

        for (var p : detailsProjections) {
            BigDecimal subtotal = p.getSubtotal();
            totalValue = totalValue.add(subtotal);
            
            detailResponses.add(new TariffCalculationDetailResponse(
                    new RangeResponse(p.getStart(), p.getEnd()),
                    p.getConsumedM3(),
                    p.getUnitValue(),
                    subtotal
            ));
        }

        return new TariffCalculationResponse(request.category(), request.consumption(), totalValue, detailResponses);
    }
}
