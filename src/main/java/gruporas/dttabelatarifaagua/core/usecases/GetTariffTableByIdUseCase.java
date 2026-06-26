package gruporas.dttabelatarifaagua.core.usecases;

import gruporas.dttabelatarifaagua.persistence.model.TariffTable;
import gruporas.dttabelatarifaagua.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.shared.exception.EntityNotFoundException;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.web.dto.TariffTableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetTariffTableByIdUseCase implements UseCase<UUID, TariffTableResponse> {

    private final TariffTableRepository tabelaTarifariaRepository;

    @Override
    public TariffTableResponse execute(UUID id) {
        TariffTable tabela = tabelaTarifariaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("tabelaTarifaria.notFound"));
        
        return mapToResponse(tabela);
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
