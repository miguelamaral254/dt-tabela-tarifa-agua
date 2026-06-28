package gruporas.dttabelatarifaagua.tariff.core.usecases;

import gruporas.dttabelatarifaagua.shared.exception.EntityNotFoundException;
import gruporas.dttabelatarifaagua.shared.usecase.NullaryUseCase;
import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTableFullProjection;
import gruporas.dttabelatarifaagua.tariff.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.tariff.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetCurrentTariffTableUseCase implements NullaryUseCase<TariffTableResponse> {

    private final TariffTableRepository tariffTableRepository;

    @Override
    public TariffTableResponse execute() {
        List<TariffTableFullProjection> projections = tariffTableRepository.findCurrentProjected();

        if (projections.isEmpty()) {
            throw new EntityNotFoundException("tariffTable.notFound");
        }

        return mapProjectionsToResponse(projections);
    }

    private TariffTableResponse mapProjectionsToResponse(List<TariffTableFullProjection> projections) {
        TariffTableFullProjection first = projections.get(0);

        List<ConsumptionRangeResponse> ranges = projections.stream()
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

        return new TariffTableResponse(
                first.getTableId(),
                first.getTableName(),
                first.getTableEffectiveDate(),
                createdBy,
                ranges
        );
    }
}
