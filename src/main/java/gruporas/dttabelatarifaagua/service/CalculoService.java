package gruporas.dttabelatarifaagua.service;

import gruporas.dttabelatarifaagua.dto.FaixaRangeDTO;
import gruporas.dttabelatarifaagua.dto.TariffCalculationDetailResponse;
import gruporas.dttabelatarifaagua.exception.ResourceNotFoundException;
import gruporas.dttabelatarifaagua.model.CategoriaConsumidor;
import gruporas.dttabelatarifaagua.model.FaixaConsumo;
import gruporas.dttabelatarifaagua.model.TabelaTarifaria;
import gruporas.dttabelatarifaagua.repository.CategoriaConsumidorRepository;
import gruporas.dttabelatarifaagua.repository.TabelaTarifariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalculoService {

    @Autowired
    private TabelaTarifariaRepository tabelaTarifariaRepository;

    @Autowired
    private CategoriaConsumidorRepository categoriaConsumidorRepository;

    public CalculoResult calcularTarifa(String categoriaRequest, Integer consumoRequest) {
        Pageable pageable = PageRequest.of(0, 1);
        TabelaTarifaria tabelaTarifaria = tabelaTarifariaRepository.findTopWithFaixasConsumoOrderByDataVigenciaDesc(pageable)
                .stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Nenhuma tabela de tarifa ativa encontrada."));

        CategoriaConsumidor categoria = categoriaConsumidorRepository.findByNome(categoriaRequest)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada: " + categoriaRequest));

        List<FaixaConsumo> faixasDaCategoria = tabelaTarifaria.getFaixasConsumo().stream()
                .filter(f -> f.getCategoriaConsumidor().getId().equals(categoria.getId()))
                .sorted(Comparator.comparing(FaixaConsumo::getInicio))
                .collect(Collectors.toList());

        if (faixasDaCategoria.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma faixa de consumo encontrada para a categoria " + categoriaRequest + " na tabela de tarifas ativa.");
        }

        CalculoResult result = calculateFaixaConsumptionAndValue(consumoRequest, faixasDaCategoria);

        return result;
    }

    private CalculoResult calculateFaixaConsumptionAndValue(Integer consumoTotal, List<FaixaConsumo> faixasDaCategoria) {
        BigDecimal valorTotal = BigDecimal.ZERO;
        List<TariffCalculationDetailResponse> detalhamento = new ArrayList<>();
        Integer consumoRestante = consumoTotal;

        for (FaixaConsumo faixa : faixasDaCategoria) {
            if (consumoRestante <= 0) {
                break;
            }

            Integer consumoNaFaixa;
            Integer capacidadeDaFaixaAtual;

            if (faixa.getInicio() == 0) {
                capacidadeDaFaixaAtual = faixa.getFim();
            } else {
                capacidadeDaFaixaAtual = (faixa.getFim() - faixa.getInicio()) + 1;
            }

            consumoNaFaixa = Math.min(consumoRestante, capacidadeDaFaixaAtual);

            if (consumoNaFaixa <= 0) {
                break;
            }

            BigDecimal subtotalDaFaixa = faixa.getValorUnitario().multiply(new BigDecimal(consumoNaFaixa));
            valorTotal = valorTotal.add(subtotalDaFaixa);
            consumoRestante -= consumoNaFaixa;

            detalhamento.add(new TariffCalculationDetailResponse(
                    new FaixaRangeDTO(faixa.getInicio(), faixa.getFim()),
                    consumoNaFaixa,
                    faixa.getValorUnitario(),
                    subtotalDaFaixa
            ));
        }
        return new CalculoResult(valorTotal, detalhamento);
    }

    public static class CalculoResult {
        private final BigDecimal valorTotal;
        private final List<TariffCalculationDetailResponse> detalhamento;

        public CalculoResult(BigDecimal valorTotal, List<TariffCalculationDetailResponse> detalhamento) {
            this.valorTotal = valorTotal;
            this.detalhamento = detalhamento;
        }

        public BigDecimal getValorTotal() {
            return valorTotal;
        }

        public List<TariffCalculationDetailResponse> getDetalhamento() {
            return detalhamento;
        }
    }
}
