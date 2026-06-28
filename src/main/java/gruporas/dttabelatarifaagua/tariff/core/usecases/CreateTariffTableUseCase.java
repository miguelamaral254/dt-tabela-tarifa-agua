package gruporas.dttabelatarifaagua.tariff.core.usecases;

import gruporas.dttabelatarifaagua.auth.core.usecases.GetAuthenticatedUserUseCase;

import gruporas.dttabelatarifaagua.shared.exception.ValidationException;
import gruporas.dttabelatarifaagua.shared.usecase.UseCase;
import gruporas.dttabelatarifaagua.shared.utils.ObjectUtils;
import gruporas.dttabelatarifaagua.tariff.persistence.model.ConsumerCategory;
import gruporas.dttabelatarifaagua.tariff.persistence.model.ConsumptionRange;
import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTable;
import gruporas.dttabelatarifaagua.tariff.persistence.repository.ConsumerCategoryRepository;
import gruporas.dttabelatarifaagua.tariff.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.tariff.web.dto.CategoryRequest;
import gruporas.dttabelatarifaagua.tariff.web.dto.RangeRequest;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffTableRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class CreateTariffTableUseCase implements UseCase<TariffTableRequest, UUID> {

    private final TariffTableRepository tariffTableRepository;
    private final ConsumerCategoryRepository consumerCategoryRepository;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    @Transactional
    @Override
    public UUID execute(TariffTableRequest request) {
        validateRequest(request);

        if (tariffTableRepository.existsByEffectiveDate(request.effectiveDate())) {
            throw new ValidationException("tariffTable.date.alreadyExists");
        }

        var user = getAuthenticatedUserUseCase.execute();

        TariffTable tariffTable = new TariffTable();
        tariffTable.setName(request.name());
        tariffTable.setEffectiveDate(request.effectiveDate());
        tariffTable.setCreatedBy(user.id());

        Map<String, ConsumerCategory> categories = resolveCategories(request.categories());

        List<ConsumptionRange> ranges = request.categories().stream()
                .flatMap(catReq -> catReq.ranges().stream()
                        .map(rangeReq -> mapToRange(rangeReq, tariffTable, categories.get(catReq.name()))))
                .collect(Collectors.toList());

        tariffTable.setConsumptionRanges(ranges);

        validateConsumptionRanges(ranges);
        TariffTable saved = tariffTableRepository.save(tariffTable);
        return saved.getId();
    }

    private void validateRequest(TariffTableRequest request) {
        ObjectUtils.requireNonNull(request, "tariffTable.notNull");
        ObjectUtils.requireNonNull(request.name(), "tariffTable.name.notNull");
        ObjectUtils.requireNonNull(request.effectiveDate(), "tariffTable.effectiveDate.notNull");

        if (request.categories() == null || request.categories().isEmpty()) {
            throw new ValidationException("tariffTable.ranges.empty");
        }
    }

    private Map<String, ConsumerCategory> resolveCategories(List<CategoryRequest> categoryRequests) {
        return categoryRequests.stream()
                .map(CategoryRequest::name)
                .distinct()
                .collect(Collectors.toMap(
                        name -> name,
                        name -> consumerCategoryRepository.findByName(name)
                                .orElseGet(() -> consumerCategoryRepository.save(ConsumerCategory.builder().name(name).build()))
                ));
    }

    private ConsumptionRange mapToRange(RangeRequest request, TariffTable tariffTable, ConsumerCategory category) {
        return ConsumptionRange.builder()
                .tariffTable(tariffTable)
                .consumerCategory(category)
                .start(request.start())
                .end(request.end())
                .unitValue(request.unitValue())
                .build();
    }

    private void validateConsumptionRanges(List<ConsumptionRange> ranges) {
        ranges.stream()
                .collect(Collectors.groupingBy(ConsumptionRange::getConsumerCategory))
                .values()
                .forEach(this::validateCategoryRanges);
    }

    private void validateCategoryRanges(List<ConsumptionRange> categoryRanges) {
        categoryRanges.sort(Comparator.comparing(ConsumptionRange::getStart));

        if (categoryRanges.isEmpty() || categoryRanges.get(0).getStart() != 0) {
            throw new ValidationException("range.start.zero");
        }

        IntStream.range(1, categoryRanges.size()).forEach(i -> {
            ConsumptionRange prev = categoryRanges.get(i - 1);
            ConsumptionRange curr = categoryRanges.get(i);

            if (curr.getStart() >= curr.getEnd()) throw new ValidationException("range.invalidRange");
            if (curr.getStart() <= prev.getEnd()) throw new ValidationException("range.overlap");
            if (curr.getStart() != prev.getEnd() + 1) throw new ValidationException("range.gap");
        });

        if (categoryRanges.get(categoryRanges.size() - 1).getEnd() < 99999) {
            throw new ValidationException("range.insufficientCoverage");
        }
    }
}
