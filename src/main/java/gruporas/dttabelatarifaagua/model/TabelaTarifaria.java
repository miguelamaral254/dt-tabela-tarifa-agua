package gruporas.dttabelatarifaagua.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tabela_tarifaria")
public class TabelaTarifaria {

    public TabelaTarifaria() {
    }

    public TabelaTarifaria(String nome, LocalDate dataVigencia, List<FaixaConsumo> faixasConsumo) {
        this.nome = nome;
        this.dataVigencia = dataVigencia;
        this.faixasConsumo = faixasConsumo;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "data_vigencia", nullable = false)
    private LocalDate dataVigencia;

    @OneToMany(mappedBy = "tabelaTarifaria", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<FaixaConsumo> faixasConsumo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataVigencia() {
        return dataVigencia;
    }

    public void setDataVigencia(LocalDate dataVigencia) {
        this.dataVigencia = dataVigencia;
    }

    public List<FaixaConsumo> getFaixasConsumo() {
        return faixasConsumo;
    }

    public void setFaixasConsumo(List<FaixaConsumo> faixasConsumo) {
        this.faixasConsumo = faixasConsumo;
    }

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
