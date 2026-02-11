package gruporas.dttabelatarifaagua.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TabelaTarifariaCreateUpdateDTO {
    private String nome;
    private LocalDate dataVigencia;
    private List<FaixaConsumoCreateUpdateDTO> faixasConsumo;

    public TabelaTarifariaCreateUpdateDTO() {
    }

    public TabelaTarifariaCreateUpdateDTO(String nome, LocalDate dataVigencia, List<FaixaConsumoCreateUpdateDTO> faixasConsumo) {
        this.nome = nome;
        this.dataVigencia = dataVigencia;
        this.faixasConsumo = faixasConsumo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataVigencia() {
        return dataVigencia;
    }

    public void setDataVigencia(LocalDate dataVigencia) {
        this.dataVigencia = dataVigencia;
    }

    public List<FaixaConsumoCreateUpdateDTO> getFaixasConsumo() {
        return faixasConsumo;
    }

    public void setFaixasConsumo(List<FaixaConsumoCreateUpdateDTO> faixasConsumo) {
        this.faixasConsumo = faixasConsumo;
    }
}
