package gruporas.dttabelatarifaagua.shared.pagination;

import org.springframework.data.domain.Page;
import java.util.List;

public record PageResult<T>(List<T> content,
int currentPage,
int totalPages,
long totalElements,
int pageSize,
boolean isFirst,
boolean isLast) {
    public PageResult(Page<T> page) {
        this(page.getContent(),
        page.getNumber(),
        page.getTotalPages(),
        page.getTotalElements(),
        page.getSize(),
        page.isFirst(),
        page.isLast());
    }
}
