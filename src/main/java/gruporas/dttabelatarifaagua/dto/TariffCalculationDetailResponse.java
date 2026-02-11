package gruporas.dttabelatarifaagua.dto;

import java.math.BigDecimal;

public class TariffCalculationDetailResponse {

    public TariffCalculationDetailResponse() {
    }

    public TariffCalculationDetailResponse(FaixaRangeDTO faixa, Integer m3Cobrados, BigDecimal valorUnitario, BigDecimal subtotal) {
        this.faixa = faixa;
        this.m3Cobrados = m3Cobrados;
        this.valorUnitario = valorUnitario;
        this.subtotal = subtotal;
    }
    public FaixaRangeDTO getFaixa() {
        return faixa;
    }

    public void setFaixa(FaixaRangeDTO faixa) {
        this.faixa = faixa;
    }

    public Integer getM3Cobrados() {
        return m3Cobrados;
    }

    public void setM3Cobrados(Integer m3Cobrados) {
        this.m3Cobrados = m3Cobrados;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    private FaixaRangeDTO faixa;
    private Integer m3Cobrados;
    private BigDecimal valorUnitario;
    private BigDecimal subtotal;
}
