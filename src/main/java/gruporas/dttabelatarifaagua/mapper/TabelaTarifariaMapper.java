package gruporas.dttabelatarifaagua.mapper;

import gruporas.dttabelatarifaagua.dto.CategoriaConsumidorDTO;
import gruporas.dttabelatarifaagua.dto.FaixaConsumoResponseDTO;
import gruporas.dttabelatarifaagua.dto.TabelaTarifariaCreateUpdateDTO;
import gruporas.dttabelatarifaagua.dto.FaixaConsumoCreateUpdateDTO;
import gruporas.dttabelatarifaagua.dto.TabelaTarifariaResponseDTO;
import gruporas.dttabelatarifaagua.model.TabelaTarifaria;
import gruporas.dttabelatarifaagua.model.FaixaConsumo;
import gruporas.dttabelatarifaagua.model.CategoriaConsumidor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TabelaTarifariaMapper {

    public TabelaTarifariaResponseDTO convertToResponseDTO(TabelaTarifaria tabelaTarifaria) {
        List<FaixaConsumoResponseDTO> faixasConsumoDTO = tabelaTarifaria.getFaixasConsumo().stream()
                .map(faixa -> new FaixaConsumoResponseDTO(
                        faixa.getId(),
                        new CategoriaConsumidorDTO(faixa.getCategoriaConsumidor().getId(), faixa.getCategoriaConsumidor().getNome()),
                        faixa.getInicio(),
                        faixa.getFim(),
                        faixa.getValorUnitario()
                ))
                .collect(Collectors.toList());

        return new TabelaTarifariaResponseDTO(
                tabelaTarifaria.getId(),
                tabelaTarifaria.getNome(),
                tabelaTarifaria.getDataVigencia(),
                faixasConsumoDTO
        );
    }

    public TabelaTarifaria toEntity(TabelaTarifariaCreateUpdateDTO tabelaTarifariaRequestDTO) {
        if (tabelaTarifariaRequestDTO == null) {
            return null;
        }

        TabelaTarifaria tabelaTarifaria = new TabelaTarifaria();
        tabelaTarifaria.setNome(tabelaTarifariaRequestDTO.getNome());
        tabelaTarifaria.setDataVigencia(tabelaTarifariaRequestDTO.getDataVigencia());

        if (tabelaTarifariaRequestDTO.getFaixasConsumo() != null) {
            List<FaixaConsumo> faixasConsumo = tabelaTarifariaRequestDTO.getFaixasConsumo().stream()
                    .map(this::toFaixaConsumoEntity)
                    .collect(Collectors.toList());
            tabelaTarifaria.setFaixasConsumo(faixasConsumo);
            faixasConsumo.forEach(faixa -> faixa.setTabelaTarifaria(tabelaTarifaria));
        }

        return tabelaTarifaria;
    }

    private FaixaConsumo toFaixaConsumoEntity(FaixaConsumoCreateUpdateDTO faixaRequestDTO) {
        if (faixaRequestDTO == null) {
            return null;
        }

        FaixaConsumo faixaConsumo = new FaixaConsumo();
        faixaConsumo.setInicio(faixaRequestDTO.getInicio());
        faixaConsumo.setFim(faixaRequestDTO.getFim());
        faixaConsumo.setValorUnitario(faixaRequestDTO.getValorUnitario());

        if (faixaRequestDTO.getCategoriaConsumidor() != null) {
            CategoriaConsumidor categoriaConsumidor = new CategoriaConsumidor();
            categoriaConsumidor.setId(faixaRequestDTO.getCategoriaConsumidor().getId());
            categoriaConsumidor.setNome(faixaRequestDTO.getCategoriaConsumidor().getNome());
            faixaConsumo.setCategoriaConsumidor(categoriaConsumidor);
        }

        return faixaConsumo;
    }
}
