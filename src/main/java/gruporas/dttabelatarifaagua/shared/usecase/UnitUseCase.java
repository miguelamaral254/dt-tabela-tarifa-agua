package gruporas.dttabelatarifaagua.shared.usecase;


import gruporas.dttabelatarifaagua.tariff.web.dto.TariffTableResponse;

public interface UnitUseCase<I> {
    TariffTableResponse execute(I i);
}
