package gruporas.dttabelatarifaagua.tariff.core.usecases;

import gruporas.dttabelatarifaagua.auth.core.usecases.GetAuthenticatedUserUseCase;
import gruporas.dttabelatarifaagua.shared.exception.ValidationException;
import gruporas.dttabelatarifaagua.tariff.persistence.model.ConsumerCategory;
import gruporas.dttabelatarifaagua.tariff.persistence.model.TariffTable;
import gruporas.dttabelatarifaagua.tariff.persistence.repository.ConsumerCategoryRepository;
import gruporas.dttabelatarifaagua.tariff.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.tariff.web.dto.CategoryRequest;
import gruporas.dttabelatarifaagua.tariff.web.dto.RangeRequest;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffTableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTariffTableUseCaseTest {

    @Mock
    private TariffTableRepository tariffTableRepository;
    @Mock
    private ConsumerCategoryRepository consumerCategoryRepository;
    @Mock
    private GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    @InjectMocks
    private CreateTariffTableUseCase createTariffTableUseCase;

    private TariffTableRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new TariffTableRequest(
                "Tabela Industrial",
                LocalDate.now(),
                List.of(new CategoryRequest("Industrial", List.of(
                        new RangeRequest(0, 10, new BigDecimal("1.00")),
                        new RangeRequest(11, 99999, new BigDecimal("2.00"))
                )))
        );
    }

    @Test
    @DisplayName("Should create tariff table successfully")
    void shouldCreateTariffTableSuccessfully() {
        when(tariffTableRepository.existsByEffectiveDate(any())).thenReturn(false);
        when(getAuthenticatedUserUseCase.execute()).thenReturn(new gruporas.dttabelatarifaagua.auth.core.model.UserBasic(UUID.randomUUID(), "user", "user@gruporas.com.br", "User", "Test"));
        when(consumerCategoryRepository.findByNameIn(any())).thenReturn(List.of(ConsumerCategory.builder().id(UUID.randomUUID()).name("Industrial").build()));
        when(tariffTableRepository.save(any())).thenAnswer(invocation -> {
            TariffTable tt = invocation.getArgument(0);
            tt.setId(UUID.randomUUID());
            return tt;
        });

        UUID result = createTariffTableUseCase.execute(validRequest);

        assertNotNull(result);
        verify(tariffTableRepository).save(any(TariffTable.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when effective date already exists")
    void shouldThrowExceptionWhenDateExists() {
        when(tariffTableRepository.existsByEffectiveDate(any())).thenReturn(true);
        assertThrows(ValidationException.class, () -> createTariffTableUseCase.execute(validRequest));
    }

    @Test
    @DisplayName("Should throw ValidationException when ranges overlap")
    void shouldThrowExceptionWhenRangesOverlap() {
        TariffTableRequest invalidRequest = new TariffTableRequest(
                "Tabela",
                LocalDate.now(),
                List.of(new CategoryRequest("Industrial", List.of(
                        new RangeRequest(0, 10, new BigDecimal("1.00")),
                        new RangeRequest(5, 15, new BigDecimal("2.00"))
                )))
        );
        when(tariffTableRepository.existsByEffectiveDate(any())).thenReturn(false);
        when(getAuthenticatedUserUseCase.execute()).thenReturn(new gruporas.dttabelatarifaagua.auth.core.model.UserBasic(UUID.randomUUID(), "user", "user@gruporas.com.br", "User", "Test"));
        when(consumerCategoryRepository.findByNameIn(any())).thenReturn(List.of(ConsumerCategory.builder().id(UUID.randomUUID()).name("Industrial").build()));

        assertThrows(ValidationException.class, () -> createTariffTableUseCase.execute(invalidRequest));
    }
}
