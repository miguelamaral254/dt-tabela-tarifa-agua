package gruporas.dttabelatarifaagua.dto;

import java.util.List;

public class PageableDTO<T> {
    private List<T> content;
    private PageMetadataDTO pageMetadata;

    public PageableDTO() {
    }

    public PageableDTO(List<T> content, PageMetadataDTO pageMetadata) {
        this.content = content;
        this.pageMetadata = pageMetadata;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public PageMetadataDTO getPageMetadata() {
        return pageMetadata;
    }

    public void setPageMetadata(PageMetadataDTO pageMetadata) {
        this.pageMetadata = pageMetadata;
    }
}
