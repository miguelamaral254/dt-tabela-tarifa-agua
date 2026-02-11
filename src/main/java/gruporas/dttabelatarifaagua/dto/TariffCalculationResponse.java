package gruporas.dttabelatarifaagua.dto;

import java.math.BigDecimal;
import java.util.List;

public class TariffCalculationResponse {

    public TariffCalculationResponse() {
    }

    public TariffCalculationResponse(String categoria, Integer consumoTotal, BigDecimal valorTotal, List<TariffCalculationDetailResponse> detalhamento) {
        this.categoria = categoria;
        this.consumoTotal = consumoTotal;
        this.valorTotal = valorTotal;
        this.detalhamento = detalhamento;
    }
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getConsumoTotal() {
        return consumoTotal;
    }

    public void setConsumoTotal(Integer consumoTotal) {
        this.consumoTotal = consumoTotal;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<TariffCalculationDetailResponse> getDetalhamento() {
        return detalhamento;
    }

    public void setDetalhamento(List<TariffCalculationDetailResponse> detalhamento) {
        this.detalhamento = detalhamento;
    }

    private String categoria;
    private Integer consumoTotal;
    private BigDecimal valorTotal;
    private List<TariffCalculationDetailResponse> detalhamento;
}
