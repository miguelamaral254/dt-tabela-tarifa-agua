package gruporas.dttabelatarifaagua.tariff.web.dto;

import java.util.List;

public record CategoryRequest(
    String name,
    List<RangeRequest> ranges
) {}
