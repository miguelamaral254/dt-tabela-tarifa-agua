package gruporas.dttabelatarifaagua.tariff.core.usecases;

import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTable;
import gruporas.dttabelatarifaagua.tariff.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.shared.exception.EntityNotFoundException;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffTableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetTariffTableByIdUseCase implements UseCase<UUID, TariffTableResponse> {

    private final TariffTableRepository tariffTableRepository;

    @Override
    public TariffTableResponse execute(UUID id) {
        TariffTable tariffTable = tariffTableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("tariffTable.notFound"));
        
        return mapToResponse(tariffTable);
    }

    private TariffTableResponse mapToResponse(TariffTable t) {
        var ranges = t.getConsumptionRanges().stream()
                .map(r -> new gruporas.dttabelatarifaagua.tariff.internal.web.dto.ConsumptionRangeResponse(
                        r.getId(),
                        new gruporas.dttabelatarifaagua.tariff.internal.web.dto.ConsumerCategoryResponse(r.getConsumerCategory().getId(), r.getConsumerCategory().getName()),
                        r.getStart(),
                        r.getEnd(),
                        r.getUnitValue()
                )).toList();
                
        return new TariffTableResponse(t.getId(), t.getName(), t.getEffectiveDate(), ranges);
    }
}
