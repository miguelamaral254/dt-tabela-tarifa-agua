package gruporas.dttabelatarifaagua.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "categoria_consumidor")
public class CategoriaConsumidor {

    public CategoriaConsumidor() {
    }

    public CategoriaConsumidor(String nome) {
        this.nome = nome;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nome;

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

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoriaConsumidor that = (CategoriaConsumidor) o;
        if (id != null && that.id != null) {
            return id.equals(that.id);
        }
        return nome.equals(that.nome);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        return nome.hashCode();
    }
}
