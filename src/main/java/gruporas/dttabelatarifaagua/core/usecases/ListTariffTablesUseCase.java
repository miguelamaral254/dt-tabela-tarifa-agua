package gruporas.dttabelatarifaagua.core.usecases;

import gruporas.dttabelatarifaagua.persistence.model.TariffTable;
import gruporas.dttabelatarifaagua.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.shared.exception.ValidationException;
import gruporas.dttabelatarifaagua.shared.pagination.PageResult;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.shared.utils.ObjectUtils;
import gruporas.dttabelatarifaagua.core.model.TariffTableFilter;
import gruporas.dttabelatarifaagua.web.dto.TariffTableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ListTabelasTarifariasUseCase implements UseCase<TariffTableFilter, PageResult<TariffTableResponse>> {

    private final TariffTableRepository tabelaTarifariaRepository;

    @Override
    public PageResult<TariffTableResponse> execute(TariffTableFilter filter) {
        validate(filter);

        // For now, we use the standard findAll with pagination. 
        // If category filter is provided, we'll filter the results in memory or add a repository method.
        Page<TariffTable> page = tabelaTarifariaRepository.findAll(filter.getPageable().toPageRequest());
        
        var content = page.getContent().stream()
                .filter(t -> filter.getCategory() == null || 
                        t.getFaixasConsumo().stream()
                         .anyMatch(f -> f.getConsumerCategory().getNome().equalsIgnoreCase(filter.getCategory())))
                .map(this::mapToResponse)
                .toList();
        
        return new PageResult<>(
                content, 
                page.getNumber(), 
                page.getTotalPages(), 
                page.getTotalElements(), 
                page.getSize(), 
                page.isFirst(), 
                page.isLast());
    }

    private void validate(TariffTableFilter filter) {
        ObjectUtils.requireNonNull(filter, "filter.notNull");
        ObjectUtils.requireNonNull(filter.getPageable(), "filter.pageable.notNull");

        if (!filter.isValidPageNumber()) {
            throw new ValidationException("filter.pageable.pageNumber.invalid");
        }

        if (!filter.isValidPageSize()) {
            throw new ValidationException("filter.pageable.pageSize.invalid");
        }
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
