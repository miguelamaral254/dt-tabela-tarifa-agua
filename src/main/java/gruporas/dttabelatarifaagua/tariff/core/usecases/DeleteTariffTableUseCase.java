package gruporas.dttabelatarifaagua.tariff.core.usecases;

import gruporas.dttabelatarifaagua.tariff.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.shared.exception.EntityNotFoundException;
import gruporas.dttabelatarifaagua.shared.usecase.UnitUseCase;
import gruporas.dttabelatarifaagua.shared.utils.ObjectUtils;
import gruporas.dttabelatarifaagua.tariff.web.dto.TariffTableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DeleteTariffTableUseCase implements UnitUseCase<UUID> {

    private final TariffTableRepository tariffTableRepository;

    @Transactional
    @Override
    public TariffTableResponse execute(UUID id) {
        ObjectUtils.requireNonNull(id, "tariffTable.id.notNull");
        if (!tariffTableRepository.existsById(id)) {
            throw new EntityNotFoundException("tariffTable.notFound");
        }
        tariffTableRepository.deleteById(id);
        return null;
    }
}
