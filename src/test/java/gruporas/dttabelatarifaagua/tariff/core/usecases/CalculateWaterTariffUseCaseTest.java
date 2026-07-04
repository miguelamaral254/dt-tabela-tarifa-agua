package gruporas.dttabelatarifaagua.tariff.core.usecases;

import gruporas.dttabelatarifaagua.shared.exception.ResourceNotFoundException;
import gruporas.dttabelatarifaagua.tariff.persistence.model.ConsumerCategory;
import gruporas.dttabelatarifaagua.tariff.persistence.model.ConsumptionRange;
import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTable;
import gruporas.dttabelatarifaagua.tariff.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffCalculationRequest;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffCalculationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculateWaterTariffUseCaseTest {

    @Mock
    private TariffTableRepository tariffTableRepository;

    @InjectMocks
    private CalculateWaterTariffUseCase calculateWaterTariffUseCase;

    @Test
    @DisplayName("Should calculate tariff correctly")
    void shouldCalculateTariff() {
        TariffCalculationRequest request = new TariffCalculationRequest("Industrial", 15);
        
        ConsumerCategory category = new ConsumerCategory(UUID.randomUUID(), "Industrial");
        
        ConsumptionRange range1 = ConsumptionRange.builder()
                .start(0)
                .end(10)
                .unitValue(new BigDecimal("1.00"))
                .consumerCategory(category)
                .build();
                
        ConsumptionRange range2 = ConsumptionRange.builder()
                .start(11)
                .end(20)
                .unitValue(new BigDecimal("2.00"))
                .consumerCategory(category)
                .build();
        
        TariffTable tariffTable = TariffTable.builder()
                .consumptionRanges(List.of(range1, range2))
                .build();

        when(tariffTableRepository.findTopByOrderByEffectiveDateDesc()).thenReturn(tariffTable);

        TariffCalculationResponse response = calculateWaterTariffUseCase.execute(request);

        assertEquals(new BigDecimal("20.00"), response.totalValue().setScale(2));
        assertEquals(2, response.details().size());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when no tariff table found")
    void shouldThrowExceptionWhenNotFound() {
        when(tariffTableRepository.findTopByOrderByEffectiveDateDesc()).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> calculateWaterTariffUseCase.execute(new TariffCalculationRequest("Test", 10)));
    }
}
