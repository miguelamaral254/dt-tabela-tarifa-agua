package gruporas.dttabelatarifaagua.controller;

import gruporas.dttabelatarifaagua.dto.TariffCalculationRequest;
import gruporas.dttabelatarifaagua.dto.TariffCalculationResponse;
import gruporas.dttabelatarifaagua.service.CalculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gruporas.dttabelatarifaagua.mapper.CalculoMapper;

@RestController
@RequestMapping("/api/v1/calculos")
public class CalculoController {

    @Autowired
    private CalculoService calculoService;

    @Autowired
    private CalculoMapper calculoMapper;

    @PostMapping
    public ResponseEntity<TariffCalculationResponse> calcular(@RequestBody TariffCalculationRequest request) {
        CalculoService.CalculoResult result = calculoService.calcularTarifa(request.getCategoria(), request.getConsumo());
        TariffCalculationResponse response = calculoMapper.toCalculoResponse(request.getCategoria(), request.getConsumo(), result);
        return ResponseEntity.ok(response);
    }
}
