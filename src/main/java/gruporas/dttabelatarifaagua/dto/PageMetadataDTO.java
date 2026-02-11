package gruporas.dttabelatarifaagua.dto;

public class PageMetadataDTO {
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;

    public PageMetadataDTO() {
    }

    public PageMetadataDTO(int number, int size, long totalElements, int totalPages) {
        this.number = number;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
