package gruporas.dttabelatarifaagua.tariff.core.usecases;

import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTableSummaryProjection;
import gruporas.dttabelatarifaagua.tariff.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.shared.exception.ValidationException;
import gruporas.dttabelatarifaagua.shared.pagination.PageResult;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.shared.utils.ObjectUtils;
import gruporas.dttabelatarifaagua.tariff.core.model.TariffTableFilter;
import gruporas.dttabelatarifaagua.tariff.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ListTariffTablesUseCase implements UseCase<TariffTableFilter, PageResult<TariffTableSummaryResponse>> {

    private final TariffTableRepository tariffTableRepository;

    @Override
    public PageResult<TariffTableSummaryResponse> execute(TariffTableFilter filter) {
        validate(filter);

        Page<TariffTableSummaryProjection> page = tariffTableRepository.findAllSummaryProjected(filter.getPageable().toPageRequest());
List<TariffTableSummaryResponse> content = page.getContent().stream()
        .map(p -> new TariffTableSummaryResponse(
                p.getTableId(),
                p.getTableName(),
                p.getTableEffectiveDate(),
                new CreatedByResponse(p.getUserId(), p.getUsername(), p.getFirstName(), p.getLastName())
        ))
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
}
