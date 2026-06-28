package gruporas.dttabelatarifaagua.tariff.core.usecases;

import gruporas.dttabelatarifaagua.shared.exception.EntityNotFoundException;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTableFullProjection;
import gruporas.dttabelatarifaagua.tariff.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.tariff.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetTariffTableByIdUseCase implements UseCase<UUID, TariffTableResponse> {

    private final TariffTableRepository tariffTableRepository;

    @Override
    public TariffTableResponse execute(UUID id) {
        List<TariffTableFullProjection> projections = tariffTableRepository.findByIdProjected(id);

        if (projections.isEmpty()) {
            throw new EntityNotFoundException("tariffTable.notFound");
        }

        TariffTableFullProjection first = projections.get(0);

        List<ConsumptionRangeResponse> consumptionRanges = projections.stream()
                .filter(p -> p.getRangeId() != null)
                .map(p -> new ConsumptionRangeResponse(
                        p.getRangeId(),
                        new ConsumerCategoryResponse(p.getCategoryId(), p.getCategoryName()),
                        p.getStartRange(),
                        p.getEndRange(),
                        p.getUnitValue()
                )).toList();

        var createdBy = new CreatedByResponse(
                first.getUserId(),
                first.getUsername(),
                first.getFirstName(),
                first.getLastName()
        );

        return new TariffTableResponse(first.getTableId(), first.getTableName(), first.getTableEffectiveDate(), createdBy, consumptionRanges);
    }
}
