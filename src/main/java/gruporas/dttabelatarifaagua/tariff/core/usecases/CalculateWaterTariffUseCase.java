package gruporas.dttabelatarifaagua.tariff.core.usecases;

import gruporas.dttabelatarifaagua.tariff.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.shared.exception.ResourceNotFoundException;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.tariff.web.dto.RangeResponse;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffCalculationDetailResponse;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffCalculationRequest;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffCalculationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
            BigDecimal subtotal = p.getSubtotal().setScale(2, RoundingMode.HALF_UP);
            totalValue = totalValue.add(subtotal);

            detailResponses.add(new TariffCalculationDetailResponse(
                    new RangeResponse(p.getStart(), p.getEnd()),
                    p.getConsumedM3(),
                    p.getUnitValue().setScale(2, RoundingMode.HALF_UP),
                    subtotal
            ));
        }

        return new TariffCalculationResponse(request.category(), request.consumption(), totalValue.setScale(2, RoundingMode.HALF_UP), detailResponses);
    }
}

