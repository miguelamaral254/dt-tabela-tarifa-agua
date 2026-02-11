package gruporas.dttabelatarifaagua.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class FaixaConsumoResponseDTO {
    private UUID id;
    private CategoriaConsumidorDTO categoriaConsumidor;
    private Integer inicio;
    private Integer fim;
    private BigDecimal valorUnitario;

    public FaixaConsumoResponseDTO() {
    }

    public FaixaConsumoResponseDTO(UUID id, CategoriaConsumidorDTO categoriaConsumidor, Integer inicio, Integer fim, BigDecimal valorUnitario) {
        this.id = id;
        this.categoriaConsumidor = categoriaConsumidor;
        this.inicio = inicio;
        this.fim = fim;
        this.valorUnitario = valorUnitario;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
