package gruporas.dttabelatarifaagua.core.usecases;

import gruporas.dttabelatarifaagua.persistence.repository.TariffTableRepository;
import gruporas.dttabelatarifaagua.shared.exception.EntityNotFoundException;
import gruporas.dttabelatarifaagua.shared.usecase.UnitUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DeleteTariffTableUseCase implements UnitUseCase<UUID, Void> {

    private final TariffTableRepository tabelaTarifariaRepository;

    @Transactional
    @Override
    public Void execute(UUID id) {
        ObjectUtils.requireNonNull(id, "tabelaTarifaria.id.notNull")
        if (!tabelaTarifariaRepository.existsById(id)) {
            throw new EntityNotFoundException("tabelaTarifaria.notFound");
        }
        tabelaTarifariaRepository.deleteById(id);
        return null;
    }
}
