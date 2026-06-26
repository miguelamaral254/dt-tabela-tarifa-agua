package gruporas.dttabelatarifaagua.core.usecases;

import gruporas.dttabelatarifaagua.persistence.model.TariffTable;
import gruporas.dttabelatarifaagua.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.shared.exception.EntityNotFoundException;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.web.dto.TariffTableRequest;
import gruporas.dttabelatarifaagua.web.dto.TariffTableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UpdateTariffTableUseCase implements UnitUseCase<UpdateTariffTableRequest> {

    private final TariffTableRepository tabelaTarifariaRepository;

    @Transactional
    @Override
    public TariffTableResponse execute(UpdateTariffTableRequest request) {
        TariffTable tabela = tabelaTarifariaRepository.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("tabelaTarifaria.notFound"));

        tabela.setNome(request.name());
        tabela.setDataVigencia(request.effectiveDate());
        
        TariffTable updated = tabelaTarifariaRepository.save(tabela);
        return mapToResponse(updated);
    }

    private TariffTableResponse mapToResponse(TariffTable t) {
        var faixas = t.getFaixasConsumo().stream()
                .map(f -> new gruporas.dttabelatarifaagua.web.dto.ConsumptionRangeResponse(
                        f.getId(),
                        new gruporas.dttabelatarifaagua.web.dto.ConsumerCategoryResponse(f.getConsumerCategory().getId(), f.getConsumerCategory().getNome()),
                        f.getInicio(),
                        f.getFim(),
                        f.getValorUnitario()
                )).toList();
                
        return new TariffTableResponse(t.getId(), t.getNome(), t.getDataVigencia(), faixas);
    }
}
