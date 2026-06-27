package gruporas.dttabelatarifaagua.tariff.core.usecases;

import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTable;
import gruporas.dttabelatarifaagua.tariff.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.shared.exception.EntityNotFoundException;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffTableResponse;
import gruporas.dttabelatarifaagua.tariff.web.dto.UpdateTariffTableRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UpdateTariffTableUseCase implements UseCase<UpdateTariffTableRequest, TariffTableResponse> {

    private final TariffTableRepository tariffTableRepository;

    @Transactional
    @Override
    public TariffTableResponse execute(UpdateTariffTableRequest request) {
        TariffTable tariffTable = tariffTableRepository.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("tabelaTarifaria.notFound"));

        tariffTable.setName(request.name());
        tariffTable.setEffectiveDate(request.effectiveDate());
        
        TariffTable updated = tariffTableRepository.save(tariffTable);
        return mapToResponse(updated);
    }

    private TariffTableResponse mapToResponse(TariffTable t) {
        List<gruporas.dttabelatarifaagua.tariff.internal.web.dto.ConsumptionRangeResponse> consumptionRanges = (t.getConsumptionRanges() == null) 
                ? Collections.emptyList() 
                : t.getConsumptionRanges().stream()
                .map(f -> new gruporas.dttabelatarifaagua.tariff.internal.web.dto.ConsumptionRangeResponse(
                        f.getId(),
                        new gruporas.dttabelatarifaagua.tariff.internal.web.dto.ConsumerCategoryResponse(f.getConsumerCategory().getId(), f.getConsumerCategory().getName()),
                        f.getStart(),
                        f.getEnd(),
                        f.getUnitValue()
                )).toList();
                
        return new TariffTableResponse(t.getId(), t.getName(), t.getEffectiveDate(), consumptionRanges);
    }
}
