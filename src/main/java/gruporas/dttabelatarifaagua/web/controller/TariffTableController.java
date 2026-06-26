package gruporas.dttabelatarifaagua.web.controller;

import gruporas.dttabelatarifaagua.core.usecases.*;
import gruporas.dttabelatarifaagua.shared.pagination.Pageable;
import gruporas.dttabelatarifaagua.shared.pagination.PageResult;
import gruporas.dttabelatarifaagua.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tariff-tables")
public class TariffTableController {

    private final CreateTariffTableUseCase createTariffTableUseCase;
    private final ListTabelasTarifariasUseCase listTabelasTarifariasUseCase;
    private final GetTariffTableByIdUseCase getTariffTableByIdUseCase;
    private final UpdateTariffTableUseCase updateTariffTableUseCase;
    private final DeleteTariffTableUseCase deleteTariffTableUseCase;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody TariffTableRequest request) {
        createTariffTableUseCase.execute(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResult<TariffTableResponse>> list(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        
        var filter = new TariffTableFilter(categoria, new Pageable(pageNumber, pageSize));
        var result = listTabelasTarifariasUseCase.execute(filter);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TariffTableResponse> getById(@PathVariable UUID id) {
        var response = getTariffTableByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TariffTableResponse> update(@PathVariable UUID id, @RequestBody UpdateTariffTableRequest request) {
        var updateRequest = new UpdateTariffTableRequest(id, request.name(), request.effectiveDate());
        var response = updateTariffTableUseCase.execute(updateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteTariffTableUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
