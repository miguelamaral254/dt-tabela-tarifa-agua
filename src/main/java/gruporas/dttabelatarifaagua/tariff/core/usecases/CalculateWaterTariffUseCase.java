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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

@RequiredArgsConstructor
@Service
public class CalculateWaterTariffUseCase implements UseCase<TariffCalculationRequest, TariffCalculationResponse> {

    private final TariffTableRepository tariffTableRepository;

    @Override
    @Transactional(readOnly = true)
    public TariffCalculationResponse execute(TariffCalculationRequest request) {
        var tariffTable = tariffTableRepository.findTopByOrderByEffectiveDateDesc();

        if (tariffTable == null) {
            throw new ResourceNotFoundException("tariff.notFound");
        }

        var details = tariffTable.getConsumptionRanges().stream()
                .filter(cr -> cr.getConsumerCategory().getName().equals(request.category()))
                .filter(cr -> request.consumption() >= cr.getStart())
                .sorted(Comparator.comparingInt(cr -> cr.getStart()))
                .map(cr -> {
                    int consumedM3 = Math.min(request.consumption(), cr.getEnd()) - cr.getStart() + (cr.getStart() == 0 ? 0 : 1);
                    BigDecimal subtotal = cr.getUnitValue().multiply(BigDecimal.valueOf(consumedM3)).setScale(2, RoundingMode.HALF_UP);
                    return new TariffCalculationDetailResponse(
                            new RangeResponse(cr.getStart(), cr.getEnd()),
                            consumedM3,
                            cr.getUnitValue().setScale(2, RoundingMode.HALF_UP),
                            subtotal
                    );
                })
                .toList();

        if (details.isEmpty()) {
            throw new ResourceNotFoundException("tariff.notFound");
        }

        BigDecimal totalValue = details.stream()
                .map(TariffCalculationDetailResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new TariffCalculationResponse(request.category(), request.consumption(), totalValue.setScale(2, RoundingMode.HALF_UP), details);
    }
}

