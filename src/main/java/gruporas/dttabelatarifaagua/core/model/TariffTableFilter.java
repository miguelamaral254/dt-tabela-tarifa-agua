package gruporas.dttabelatarifaagua.core.model;

import gruporas.dttabelatarifaagua.shared.pagination.Filter;
import gruporas.dttabelatarifaagua.shared.pagination.Pageable;
import lombok.Getter;

@Getter
public class TariffTableFilter extends Filter {
    private static final int MAX_PAGE_SIZE = 60;
    private final String category;

    public TariffTableFilter(String category, Pageable pageable) {
        super(pageable);
        this.category = category;
    }

    @Override
    public int getMaxPageSize() {
        return MAX_PAGE_SIZE;
    }
}
