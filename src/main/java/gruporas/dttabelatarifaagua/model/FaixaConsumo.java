package gruporas.dttabelatarifaagua.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "faixa_consumo")
public class FaixaConsumo {

    public FaixaConsumo() {
    }

    public FaixaConsumo(TabelaTarifaria tabelaTarifaria, CategoriaConsumidor categoriaConsumidor, Integer inicio, Integer fim, BigDecimal valorUnitario) {
        this.tabelaTarifaria = tabelaTarifaria;
        this.categoriaConsumidor = categoriaConsumidor;
        this.inicio = inicio;
        this.fim = fim;
        this.valorUnitario = valorUnitario;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tabela_tarifaria_id", nullable = false)
    @JsonBackReference
    private TabelaTarifaria tabelaTarifaria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_consumidor_id", nullable = false)
    private CategoriaConsumidor categoriaConsumidor;

    @Column(nullable = false)
    private Integer inicio;

    @Column(nullable = false)
    private Integer fim;

    @Column(name = "valor_unitario", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorUnitario;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TabelaTarifaria getTabelaTarifaria() {
        return tabelaTarifaria;
    }

    public void setTabelaTarifaria(TabelaTarifaria tabelaTarifaria) {
        this.tabelaTarifaria = tabelaTarifaria;
    }

    public CategoriaConsumidor getCategoriaConsumidor() {
        return categoriaConsumidor;
    }

    public void setCategoriaConsumidor(CategoriaConsumidor categoriaConsumidor) {
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

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
