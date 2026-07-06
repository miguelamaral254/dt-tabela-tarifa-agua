package gruporas.dttabelatarifaagua.tariff.web.controller;

import gruporas.dttabelatarifaagua.tariff.core.usecases.*;
import gruporas.dttabelatarifaagua.shared.pagination.Pageable;
import gruporas.dttabelatarifaagua.shared.pagination.PageResult;
import gruporas.dttabelatarifaagua.tariff.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tabelas-tarifarias")
public class TariffTableController {

    private final CreateTariffTableUseCase createTariffTableUseCase;
    private final ListTariffTablesUseCase listTariffTablesUseCase;
    private final GetTariffTableByIdUseCase getTariffTableByIdUseCase;
    private final GetCurrentTariffTableUseCase getCurrentTariffTableUseCase;
    private final DeleteTariffTableUseCase deleteTariffTableUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID create(@RequestBody TariffTableRequest request) {
        return createTariffTableUseCase.execute(request);
    }

    @GetMapping("/atual")
    public TariffTableResponse getCurrent() {
        return getCurrentTariffTableUseCase.execute();
    }

    @GetMapping
    public PageResult<TariffTableSummaryResponse> list(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        return listTariffTablesUseCase.execute(new Pageable(pageNumber, pageSize));
    }

    @GetMapping("/{id}")
    public TariffTableResponse getById(@PathVariable UUID id) {
        return getTariffTableByIdUseCase.execute(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteTariffTableUseCase.execute(id);
    }
}

