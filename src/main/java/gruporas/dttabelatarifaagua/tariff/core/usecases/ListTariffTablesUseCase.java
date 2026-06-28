package gruporas.dttabelatarifaagua.tariff.core.usecases;

import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTable;
import gruporas.dttabelatarifaagua.tariff.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.shared.exception.ValidationException;
import gruporas.dttabelatarifaagua.shared.pagination.PageResult;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.shared.utils.ObjectUtils;
import gruporas.dttabelatarifaagua.tariff.core.model.TariffTableFilter;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffTableResponse;
import gruporas.dttabelatarifaagua.tariff.web.dto.ConsumptionRangeResponse;
import gruporas.dttabelatarifaagua.tariff.web.dto.ConsumerCategoryResponse;
import gruporas.dttabelatarifaagua.tariff.web.dto.CreatedByResponse;
import gruporas.dttabelatarifaagua.user.persistence.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ListTariffTablesUseCase implements UseCase<TariffTableFilter, PageResult<TariffTableResponse>> {

    private final TariffTableRepository tariffTableRepository;

    @Override
    public PageResult<TariffTableResponse> execute(TariffTableFilter filter) {
        validate(filter);

        Page<TariffTable> page = tariffTableRepository.findAll(filter.getPageable().toPageRequest());
        
        var content = page.getContent().stream()
                .filter(t -> filter.getCategory() == null || 
                        t.getConsumptionRanges().stream()
                         .anyMatch(r -> r.getConsumerCategory().getName().equalsIgnoreCase(filter.getCategory())))
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
        var ranges = t.getConsumptionRanges().stream()
                .map(r -> new ConsumptionRangeResponse(
                        r.getId(),
                        new ConsumerCategoryResponse(r.getConsumerCategory().getId(), r.getConsumerCategory().getName()),
                        r.getStart(),
                        r.getEnd(),
                        r.getUnitValue()
                )).toList();
        
        User creator = t.getCreator();
        CreatedByResponse createdBy = new CreatedByResponse(
                creator.getId(), creator.getUsername(), creator.getFirstName(), creator.getLastName()
        );
                
        return new TariffTableResponse(t.getId(), t.getName(), t.getEffectiveDate(), createdBy, ranges);
    }
}
