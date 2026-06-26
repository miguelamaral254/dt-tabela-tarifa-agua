package gruporas.dttabelatarifaagua.core.usecases;

import gruporas.dttabelatarifaagua.persistence.model.ConsumerCategory;
import gruporas.dttabelatarifaagua.persistence.model.ConsumptionRange;
import gruporas.dttabelatarifaagua.persistence.model.TariffTable;
import gruporas.dttabelatarifaagua.persistence.repository.ConsumerCategoryRepository;
import gruporas.dttabelatarifaagua.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.shared.exception.ResourceNotFoundException;
import gruporas.dttabelatarifaagua.shared.exception.ValidationException;
import gruporas.dttabelatarifaagua.shared.usecase.UnitUseCase;
import gruporas.dttabelatarifaagua.shared.utils.ObjectUtils;
import gruporas.dttabelatarifaagua.web.dto.ConsumptionRangeRequest;
import gruporas.dttabelatarifaagua.web.dto.TariffTableRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CreateTariffTableUseCase implements UseCase<TariffTableRequest, UUID> {

    private final TariffTableRepository tabelaTarifariaRepository;
    private final ConsumerCategoryRepository consumerCategoryRepository;

    @Transactional
    @Override
    public void execute(TariffTableRequest request) {
        validate(request);
        
        TariffTable tabela = new TariffTable();
        tabela.setNome(request.name());
        tabela.setDataVigencia(request.effectiveDate());

        List<ConsumptionRange> faixas = request.consumptionRanges().stream()
                .map(f -> mapToFaixa(f, tabela))
                .toList();
        
        tabela.setFaixasConsumo(faixas);
        
        validateFaixasConsumo(faixas);
        tabelaTarifariaRepository.save(tabela);
    }

    private void validate(TariffTableRequest request) {
        ObjectUtils.requireNonNull(request, "tabelaTarifaria.notNull");
        if (request.consumptionRanges() == null || request.consumptionRanges().isEmpty()) {
            throw new ValidationException("tabelaTarifaria.faixas.empty");
        }
    }

    private ConsumptionRange mapToFaixa(ConsumptionRangeRequest request, TariffTable tabela) {
        ConsumptionRange faixa = new ConsumptionRange();
        faixa.setTariffTable(tabela);
        faixa.setInicio(request.start());
        faixa.setFim(request.end());
        faixa.setValorUnitario(request.unitValue());

        var catReq = request.consumerCategory();
        if (catReq == null) {
            throw new ValidationException("faixa.categoria.notNull");
        }

        ConsumerCategory categoria;
        if (catReq.id() != null) {
            categoria = consumerCategoryRepository.findById(catReq.id())
                    .orElseThrow(() -> new ResourceNotFoundException("categoria.notFound"));
        } else if (catReq.name() != null) {
            categoria = consumerCategoryRepository.findByNome(catReq.name())
                    .orElseGet(() -> consumerCategoryRepository.save(new ConsumerCategory(catReq.name())));
        } else {
            throw new ValidationException("categoria.invalid");
        }
        
        faixa.setConsumerCategory(categoria);
        return faixa;
    }

    private void validateFaixasConsumo(List<ConsumptionRange> faixas) {
        Map<ConsumerCategory, List<ConsumptionRange>> faixasPorCategoria = faixas.stream()
                .collect(Collectors.groupingBy(ConsumptionRange::getConsumerCategory));

        for (List<ConsumptionRange> faixasDaCategoria : faixasPorCategoria.values()) {
            faixasDaCategoria.sort(Comparator.comparing(ConsumptionRange::getInicio));

            validateOrderAndCoverage(faixasDaCategoria);
        }
    }

    private void validateOrderAndCoverage(List<ConsumptionRange> faixas) {
        if (faixas.get(0).getInicio() != 0) {
            throw new ValidationException("faixa.start.zero");
        }

        for (int i = 0; i < faixas.size(); i++) {
            ConsumptionRange current = faixas.get(i);
            if (current.getInicio() >= current.getFim()) {
                throw new ValidationException("faixa.invalidRange");
            }

            if (i < faixas.size() - 1) {
                ConsumptionRange next = faixas.get(i + 1);
                if (current.getFim() >= next.getInicio()) {
                    throw new ValidationException("faixa.overlap");
                }
                if (current.getFim() + 1 != next.getInicio()) {
                    throw new ValidationException("faixa.gap");
                }
            } else {
                if (current.getFim() < 99999) {
                    throw new ValidationException("faixa.insufficientCoverage");
                }
            }
        }
    }
}
