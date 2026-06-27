package gruporas.dttabelatarifaagua.persistence.model;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;
import java.util.Objects;

@Entity
@Table(name = "consumer_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumerCategory {

    @Id
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UuidCreator.getRandomBased();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumerCategory that = (ConsumerCategory) o;
        return Objects.equals(id, that.id) || (id == null && Objects.equals(name, that.name));
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : Objects.hash(name);
    }
}
