package gruporas.dttabelatarifaagua.shared.pagination;

import org.springframework.data.domain.PageRequest;

public record Pageable(int pageNumber, int pageSize) {
    public int offset() {
        return pageNumber * pageSize;
    }
    public PageRequest toPageRequest() {
        return PageRequest.of(pageNumber, pageSize);
    }

    public boolean isValidPageNumber() {
        return pageNumber > -1;
    }
    public boolean isValidPageSize(int maxPageSize) {
        return pageSize > 0 && pageSize <= maxPageSize;
    }
}
