package gruporas.dttabelatarifaagua.tariff.web.controller;

import gruporas.dttabelatarifaagua.tariff.core.usecases.CalculateWaterTariffUseCase;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffCalculationRequest;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffCalculationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/calculos")
public class CalculationController {

    private final CalculateWaterTariffUseCase calculateWaterTariffUseCase;

    @PostMapping
    public ResponseEntity<TariffCalculationResponse> calcular(@RequestBody TariffCalculationRequest request) {
        var response = calculateWaterTariffUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
}
