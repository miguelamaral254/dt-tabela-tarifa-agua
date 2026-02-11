package gruporas.dttabelatarifaagua.dto;

public class TariffCalculationRequest {

    public TariffCalculationRequest() {
    }

    public TariffCalculationRequest(String categoria, Integer consumo) {
        this.categoria = categoria;
        this.consumo = consumo;
    }
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getConsumo() {
        return consumo;
    }

    public void setConsumo(Integer consumo) {
        this.consumo = consumo;
    }

    private String categoria;
    private Integer consumo;
}
