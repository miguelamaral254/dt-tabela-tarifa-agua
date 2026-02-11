package gruporas.dttabelatarifaagua.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class FaixaConsumoCreateUpdateDTO {
    private CategoriaConsumidorDTO categoriaConsumidor;
    private Integer inicio;
    private Integer fim;
    private BigDecimal valorUnitario;

    public FaixaConsumoCreateUpdateDTO() {
    }

    public FaixaConsumoCreateUpdateDTO(CategoriaConsumidorDTO categoriaConsumidor, Integer inicio, Integer fim, BigDecimal valorUnitario) {
        this.categoriaConsumidor = categoriaConsumidor;
        this.inicio = inicio;
        this.fim = fim;
        this.valorUnitario = valorUnitario;
    }

    public CategoriaConsumidorDTO getCategoriaConsumidor() {
        return categoriaConsumidor;
    }

    public void setCategoriaConsumidor(CategoriaConsumidorDTO categoriaConsumidor) {
        this.categoriaConsumidor = categoriaConsumidor;
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

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
}
