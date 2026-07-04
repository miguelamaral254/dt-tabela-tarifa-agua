package gruporas.dttabelatarifaagua.tariff.core.usecases;

import gruporas.dttabelatarifaagua.shared.exception.ResourceNotFoundException;
import gruporas.dttabelatarifaagua.tariff.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffCalculationRequest;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffCalculationResponse;
import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffCalculationProjection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

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
        
        TariffCalculationProjection p1 = mock(TariffCalculationProjection.class);
        when(p1.getStart()).thenReturn(0);
        when(p1.getEnd()).thenReturn(10);
        when(p1.getConsumedM3()).thenReturn(10);
        when(p1.getUnitValue()).thenReturn(new BigDecimal("1.00"));
        when(p1.getSubtotal()).thenReturn(new BigDecimal("10.00"));

        TariffCalculationProjection p2 = mock(TariffCalculationProjection.class);
        when(p2.getStart()).thenReturn(11);
        when(p2.getEnd()).thenReturn(20);
        when(p2.getConsumedM3()).thenReturn(5);
        when(p2.getUnitValue()).thenReturn(new BigDecimal("2.00"));
        when(p2.getSubtotal()).thenReturn(new BigDecimal("10.00"));

        when(tariffTableRepository.calculateTariffDetails("Industrial", 15)).thenReturn(List.of(p1, p2));

        TariffCalculationResponse response = calculateWaterTariffUseCase.execute(request);

        assertEquals(new BigDecimal("20.00"), response.totalValue());
        assertEquals(2, response.details().size());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when no details found")
    void shouldThrowExceptionWhenNotFound() {
        when(tariffTableRepository.calculateTariffDetails(anyString(), anyInt())).thenReturn(List.of());
        assertThrows(ResourceNotFoundException.class, () -> calculateWaterTariffUseCase.execute(new TariffCalculationRequest("Test", 10)));
    }
}
