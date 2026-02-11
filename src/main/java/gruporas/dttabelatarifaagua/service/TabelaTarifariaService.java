package gruporas.dttabelatarifaagua.service;

import gruporas.dttabelatarifaagua.exception.RangeConflictException;
import gruporas.dttabelatarifaagua.exception.ResourceNotFoundException;
import gruporas.dttabelatarifaagua.model.CategoriaConsumidor;
import gruporas.dttabelatarifaagua.model.FaixaConsumo;
import gruporas.dttabelatarifaagua.model.TabelaTarifaria;
import gruporas.dttabelatarifaagua.repository.CategoriaConsumidorRepository;
import gruporas.dttabelatarifaagua.repository.TabelaTarifariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TabelaTarifariaService {

    @Autowired
    private TabelaTarifariaRepository tabelaTarifariaRepository;

    @Autowired
    private CategoriaConsumidorRepository categoriaConsumidorRepository;

    public TabelaTarifaria createTabelaTarifaria(TabelaTarifaria tabelaTarifaria) {
        if (tabelaTarifaria.getFaixasConsumo() != null) {
            validateFaixasConsumo(tabelaTarifaria.getFaixasConsumo());

            tabelaTarifaria.getFaixasConsumo().forEach(faixa -> {
                faixa.setTabelaTarifaria(tabelaTarifaria);

                CategoriaConsumidor categoriaConsumidor = faixa.getCategoriaConsumidor();
                if (categoriaConsumidor != null) {
                    if (categoriaConsumidor.getId() != null) {
                        CategoriaConsumidor existingCategoria = categoriaConsumidorRepository.findById(categoriaConsumidor.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Categoria de Consumidor não encontrada com id: " + categoriaConsumidor.getId()));
                        faixa.setCategoriaConsumidor(existingCategoria);
                    } else if (categoriaConsumidor.getNome() != null) {
                        CategoriaConsumidor existingCategoria = categoriaConsumidorRepository.findByNome(categoriaConsumidor.getNome())
                                .orElseGet(() -> categoriaConsumidorRepository.save(new CategoriaConsumidor(categoriaConsumidor.getNome())));
                        faixa.setCategoriaConsumidor(existingCategoria);
                    } else {
                        throw new IllegalArgumentException("CategoriaConsumidor deve ter id ou nome informados.");
                    }
                } else {
                    throw new IllegalArgumentException("CategoriaConsumidor é obrigatória para FaixaConsumo.");
                }
            });
        }
        return tabelaTarifariaRepository.save(tabelaTarifaria);
    }

    public Page<TabelaTarifaria> getAllTabelasTarifarias(Pageable pageable) {
        return tabelaTarifariaRepository.findAll(pageable);
    }

    public TabelaTarifaria getTabelaTarifariaById(UUID id) {
        return tabelaTarifariaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tabela Tarifária não encontrada com o id: " + id));
    }

    public TabelaTarifaria updateTabelaTarifaria(UUID id, TabelaTarifaria tabelaTarifariaDetails) {
        TabelaTarifaria tabelaTarifaria = tabelaTarifariaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tabela Tarifária não encontrada com o id: " + id));

        tabelaTarifaria.setNome(tabelaTarifariaDetails.getNome());
        tabelaTarifaria.setDataVigencia(tabelaTarifariaDetails.getDataVigencia());
        return tabelaTarifariaRepository.save(tabelaTarifaria);
    }

    public void deleteTabelaTarifaria(UUID id) {
        TabelaTarifaria tabelaTarifaria = tabelaTarifariaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tabela Tarifária não encontrada com o id: " + id));
        tabelaTarifariaRepository.delete(tabelaTarifaria);
    }

    private void validateFaixasConsumo(List<FaixaConsumo> faixasConsumo) {
        if (faixasConsumo == null || faixasConsumo.isEmpty()) {
            throw new IllegalArgumentException("A tabela tarifária deve conter pelo menos uma faixa de consumo.");
        }

        Map<CategoriaConsumidor, List<FaixaConsumo>> faixasPorCategoria = faixasConsumo.stream()
                .collect(Collectors.groupingBy(FaixaConsumo::getCategoriaConsumidor));

        for (Map.Entry<CategoriaConsumidor, List<FaixaConsumo>> entry : faixasPorCategoria.entrySet()) {
            List<FaixaConsumo> faixasDaCategoria = entry.getValue();
            faixasDaCategoria.sort(Comparator.comparing(FaixaConsumo::getInicio));

            validateValidOrder(faixasDaCategoria);
            validateCompleteCoverageStart(faixasDaCategoria);
            validateNoOverlap(faixasDaCategoria);
            validateSufficientCoverage(faixasDaCategoria);
            validateLastFaixaFim(faixasDaCategoria);
        }
    }

    private void validateValidOrder(List<FaixaConsumo> faixasConsumo) {
        for (FaixaConsumo faixa : faixasConsumo) {
            if (faixa.getInicio() >= faixa.getFim()) {
                throw new IllegalArgumentException("O valor inicial da faixa (" + faixa.getInicio() + ") deve ser menor que o valor final (" + faixa.getFim() + ").");
            }
        }
    }

    private void validateLastFaixaFim(List<FaixaConsumo> faixasConsumo) {
        if (faixasConsumo.isEmpty()) {
            return;
        }
        FaixaConsumo lastFaixa = faixasConsumo.get(faixasConsumo.size() - 1);
        if (lastFaixa.getFim() < 99999) {
            throw new IllegalArgumentException("A última faixa de consumo para a categoria " + lastFaixa.getCategoriaConsumidor().getNome() + " deve terminar em um valor alto (e.g., 99999 ou mais) para garantir cobertura completa. Fim atual: " + lastFaixa.getFim());
        }
    }

    private void validateCompleteCoverageStart(List<FaixaConsumo> faixasConsumo) {
        if (faixasConsumo.get(0).getInicio() != 0) {
            throw new IllegalArgumentException("A primeira faixa de consumo deve iniciar em 0 m³.");
        }
    }

    private void validateNoOverlap(List<FaixaConsumo> faixasConsumo) {
        for (int i = 0; i < faixasConsumo.size() - 1; i++) {
            FaixaConsumo current = faixasConsumo.get(i);
            FaixaConsumo next = faixasConsumo.get(i + 1);

            if (current.getFim() >= next.getInicio()) {
                throw new RangeConflictException("As faixas de consumo se sobrepõem ou estão fora de ordem. Faixa " + current.getInicio() + "-" + current.getFim() + " e faixa " + next.getInicio() + "-" + next.getFim());
            }
        }
    }

    private void validateSufficientCoverage(List<FaixaConsumo> faixasConsumo) {
        for (int i = 0; i < faixasConsumo.size() - 1; i++) {
            FaixaConsumo current = faixasConsumo.get(i);
            FaixaConsumo next = faixasConsumo.get(i + 1);

            if (current.getFim() + 1 != next.getInicio()) {
                throw new RangeConflictException("Há uma lacuna entre as faixas de consumo. Faixa " + current.getInicio() + "-" + current.getFim() + " e faixa " + next.getInicio() + "-" + next.getFim());
            }
        }
    }
}
