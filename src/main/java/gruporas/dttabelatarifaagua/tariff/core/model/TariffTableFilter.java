package gruporas.dttabelatarifaagua.tariff.core.model;

import gruporas.dttabelatarifaagua.shared.pagination.Filter;
import gruporas.dttabelatarifaagua.shared.pagination.Pageable;
import lombok.Getter;

@Getter
public class TariffTableFilter extends Filter {
    private final String category;

    public TariffTableFilter(String category, Pageable pageable) {
        super(pageable);
        this.category = category;
    }

    @Override
    public int getMaxPageSize() {
        return 10;
    }
}
