package gruporas.dttabelatarifaagua.web.dto;

import java.util.List;

public record CategoryRequest(
    String name,
    List<RangeRequest> ranges
) {}
