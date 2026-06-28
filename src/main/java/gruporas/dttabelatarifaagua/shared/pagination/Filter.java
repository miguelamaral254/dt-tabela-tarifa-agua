package gruporas.dttabelatarifaagua.shared.pagination;

import lombok.Getter;
import java.util.Objects;

@Getter
public abstract class Filter {
    private final Pageable pageable;

    protected Filter(Pageable pageable) {
        this.pageable = pageable;
    }

    public abstract int getMaxPageSize();

    public boolean isValidPageNumber() {
        return Objects.nonNull(pageable) && pageable.isValidPageNumber();
    }

    public boolean isValidPageSize() {
        return Objects.nonNull(pageable) && pageable.isValidPageSize(getMaxPageSize());
    }

    public int getPageSize() {
        return Objects.nonNull(pageable) ? pageable.pageSize() : getMaxPageSize();
    }

    public int getOffset() {
        return Objects.nonNull(pageable) ? pageable.offset() : 0;
    }

    public Pageable getPageable() {
        return pageable;
    }
}
