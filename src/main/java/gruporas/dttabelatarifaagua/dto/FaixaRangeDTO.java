package gruporas.dttabelatarifaagua.dto;

public class FaixaRangeDTO {
    public FaixaRangeDTO() {
    }

    public FaixaRangeDTO(Integer inicio, Integer fim) {
        this.inicio = inicio;
        this.fim = fim;
    }
    public Integer getInicio() {
        return inicio;
    }

    public void setInicio(Integer inicio) {
        this.inicio = inicio;
    }

    public Integer getFim() {
        return fim;
    }

    public void setFim(Integer fim) {
        this.fim = fim;
    }

    private Integer inicio;
    private Integer fim;
}
