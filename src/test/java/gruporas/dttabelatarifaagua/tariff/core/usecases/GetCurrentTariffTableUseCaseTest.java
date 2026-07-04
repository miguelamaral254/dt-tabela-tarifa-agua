package gruporas.dttabelatarifaagua.tariff.core.usecases;

import gruporas.dttabelatarifaagua.shared.exception.EntityNotFoundException;
import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTableFullProjection;
import gruporas.dttabelatarifaagua.tariff.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCurrentTariffTableUseCaseTest {

    @Mock
    private TariffTableRepository tariffTableRepository;

    @InjectMocks
    private GetCurrentTariffTableUseCase getCurrentTariffTableUseCase;

    @Test
    @DisplayName("Should return current tariff table when found")
    void shouldReturnCurrentTariffTable() {
        TariffTableFullProjection p = mock(TariffTableFullProjection.class);
        when(p.getTableId()).thenReturn(UUID.randomUUID());
        when(p.getTableName()).thenReturn("Tabela Atual");
        when(p.getTableEffectiveDate()).thenReturn(LocalDate.now());
        when(p.getUserId()).thenReturn(UUID.randomUUID());

        when(tariffTableRepository.findCurrentProjected()).thenReturn(List.of(p));

        TariffTableResponse response = getCurrentTariffTableUseCase.execute();

        assertNotNull(response);
        assertEquals("Tabela Atual", response.name());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when no current table found")
    void shouldThrowExceptionWhenNotFound() {
        when(tariffTableRepository.findCurrentProjected()).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> getCurrentTariffTableUseCase.execute());
    }
}
