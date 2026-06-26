package gruporas.dttabelatarifaagua.persistence.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "consumption_range")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumptionRange {

    @Id
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_table_id", nullable = false)
    @JsonBackReference
    private TariffTable tariffTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_category_id", nullable = false)
    private ConsumerCategory consumerCategory;

    @Column(nullable = false)
    private Integer start;

    @Column(nullable = false)
    private Integer end;

    @Column(name = "unit_value", nullable = false, precision = 19, scale = 2)
    private BigDecimal unitValue;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = Generators.randomUuid();
        }
    }
}
