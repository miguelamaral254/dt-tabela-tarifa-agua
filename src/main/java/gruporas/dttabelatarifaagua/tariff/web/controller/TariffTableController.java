package gruporas.dttabelatarifaagua.tariff.web.controller;

import gruporas.dttabelatarifaagua.tariff.core.usecases.*;
import gruporas.dttabelatarifaagua.shared.pagination.Pageable;
import gruporas.dttabelatarifaagua.shared.pagination.PageResult;
import gruporas.dttabelatarifaagua.tariff.web.dto.*;
import gruporas.dttabelatarifaagua.tariff.core.model.TariffTableFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UUID> create(@RequestBody TariffTableRequest request) {
        UUID id = createTariffTableUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/atual")
    public ResponseEntity<TariffTableResponse> getCurrent() {
        var response = getCurrentTariffTableUseCase.execute();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PageResult<TariffTableSummaryResponse>> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        var filter = new TariffTableFilter(category, new Pageable(pageNumber, pageSize));
        var result = listTariffTablesUseCase.execute(filter);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TariffTableResponse> getById(@PathVariable UUID id) {
        var response = getTariffTableByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteTariffTableUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}

