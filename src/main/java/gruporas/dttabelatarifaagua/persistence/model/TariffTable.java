package gruporas.dttabelatarifaagua.persistence.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tariff_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TariffTable {

    @Id
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @OneToMany(mappedBy = "tariffTable", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ConsumptionRange> consumptionRanges;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = Generators.randomUuid();
        }
    }
}
