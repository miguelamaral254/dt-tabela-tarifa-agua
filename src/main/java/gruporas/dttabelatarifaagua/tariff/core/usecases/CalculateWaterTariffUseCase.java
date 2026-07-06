package gruporas.dttabelatarifaagua.tariff.core.usecases;

import gruporas.dttabelatarifaagua.tariff.persistence.model.ConsumptionRange;
import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTable;
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
import java.util.List;

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

        var details = calculateDetails(tariffTable, request);

        if (details.isEmpty()) {
            throw new ResourceNotFoundException("tariff.notFound");
        }

        BigDecimal totalValue = calculateTotalValue(details);

        return new TariffCalculationResponse(
                request.category(),
                request.consumption(),
                totalValue.setScale(2, RoundingMode.HALF_UP),
                details
        );
    }

    private List<TariffCalculationDetailResponse> calculateDetails(TariffTable tariffTable, TariffCalculationRequest request) {
        return tariffTable.getConsumptionRanges().stream()
                .filter(cr -> cr.getConsumerCategory().getName().equals(request.category()))
                .filter(cr -> request.consumption() >= cr.getStart())
                .sorted(Comparator.comparingInt(ConsumptionRange::getStart))
                .map(cr -> calculateDetail(cr, request.consumption()))
                .toList();
    }

    private TariffCalculationDetailResponse calculateDetail(ConsumptionRange cr, int consumption) {
        int consumedM3 = calculateConsumedVolume(cr, consumption);
        BigDecimal unitValue = cr.getUnitValue().setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotal = unitValue.multiply(BigDecimal.valueOf(consumedM3)).setScale(2, RoundingMode.HALF_UP);

        return new TariffCalculationDetailResponse(
                new RangeResponse(cr.getStart(), cr.getEnd()),
                consumedM3,
                unitValue,
                subtotal
        );
    }

    private int calculateConsumedVolume(ConsumptionRange cr, int totalConsumption) {
        return Math.min(totalConsumption, cr.getEnd()) - cr.getStart() + (cr.getStart() == 0 ? 0 : 1);
    }

    private BigDecimal calculateTotalValue(List<TariffCalculationDetailResponse> details) {
        return details.stream()
                .map(TariffCalculationDetailResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

