package gruporas.dttabelatarifaagua.web.controller;

import gruporas.dttabelatarifaagua.core.usecases.CalculateWaterTariffUseCase;
import gruporas.dttabelatarifaagua.web.dto.TariffCalculationRequest;
import gruporas.dttabelatarifaagua.web.dto.TariffCalculationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/calculations")
public class CalculationController {

    private final CalculateWaterTariffUseCase calculateWaterTariffUseCase;

    @PostMapping
    public ResponseEntity<TariffCalculationResponse> calcular(@RequestBody TariffCalculationRequest request) {
        var response = calculateWaterTariffUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
}
