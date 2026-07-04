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
class GetTariffTableByIdUseCaseTest {

    @Mock
    private TariffTableRepository tariffTableRepository;

    @InjectMocks
    private GetTariffTableByIdUseCase getTariffTableByIdUseCase;

    @Test
    @DisplayName("Should return tariff table when found")
    void shouldReturnTariffTable() {
        UUID id = UUID.randomUUID();
        TariffTableFullProjection p = mock(TariffTableFullProjection.class);
        when(p.getTableId()).thenReturn(id);
        when(p.getTableName()).thenReturn("Tabela A");
        when(p.getTableEffectiveDate()).thenReturn(LocalDate.now());
        when(p.getUserId()).thenReturn(UUID.randomUUID());

        when(tariffTableRepository.findByIdProjected(id)).thenReturn(List.of(p));

        TariffTableResponse response = getTariffTableByIdUseCase.execute(id);

        assertNotNull(response);
        assertEquals("Tabela A", response.name());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when table not found")
    void shouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(tariffTableRepository.findByIdProjected(id)).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> getTariffTableByIdUseCase.execute(id));
    }
}
