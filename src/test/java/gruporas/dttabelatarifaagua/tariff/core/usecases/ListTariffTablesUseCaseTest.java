package gruporas.dttabelatarifaagua.tariff.core.usecases;

import gruporas.dttabelatarifaagua.shared.pagination.Pageable;
import gruporas.dttabelatarifaagua.shared.pagination.PageResult;
import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTableSummaryProjection;
import gruporas.dttabelatarifaagua.tariff.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffTableSummaryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListTariffTablesUseCaseTest {

    @Mock
    private TariffTableRepository tariffTableRepository;

    @InjectMocks
    private ListTariffTablesUseCase listTariffTablesUseCase;

    @Test
    @DisplayName("Should return page result of tariff tables")
    void shouldReturnPageResult() {
        Pageable pageable = new Pageable(0, 10);
        
        TariffTableSummaryProjection p = mock(TariffTableSummaryProjection.class);
        when(p.getTableId()).thenReturn(UUID.randomUUID());
        when(p.getTableName()).thenReturn("Tabela 1");
        when(p.getTableEffectiveDate()).thenReturn(LocalDate.now());
        when(p.getUserId()).thenReturn(UUID.randomUUID());

        Page<TariffTableSummaryProjection> page = new PageImpl<>(List.of(p));
        when(tariffTableRepository.findAllSummaryProjected(any())).thenReturn(page);

        PageResult<TariffTableSummaryResponse> result = listTariffTablesUseCase.execute(pageable);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals("Tabela 1", result.content().get(0).name());
    }

    @Test
    @DisplayName("Should return empty page result")
    void shouldReturnEmptyPage() {
        Pageable pageable = new Pageable(0, 10);
        Page<TariffTableSummaryProjection> page = new PageImpl<>(List.of());
        when(tariffTableRepository.findAllSummaryProjected(any())).thenReturn(page);

        PageResult<TariffTableSummaryResponse> result = listTariffTablesUseCase.execute(pageable);

        assertTrue(result.content().isEmpty());
    }
}
