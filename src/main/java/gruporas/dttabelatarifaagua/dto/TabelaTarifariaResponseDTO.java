package gruporas.dttabelatarifaagua.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TabelaTarifariaResponseDTO {
    private UUID id;
    private String nome;
    private LocalDate dataVigencia;
    private List<FaixaConsumoResponseDTO> faixasConsumo;

    public TabelaTarifariaResponseDTO() {
    }

    public TabelaTarifariaResponseDTO(UUID id, String nome, LocalDate dataVigencia, List<FaixaConsumoResponseDTO> faixasConsumo) {
        this.id = id;
        this.nome = nome;
        this.dataVigencia = dataVigencia;
        this.faixasConsumo = faixasConsumo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public List<FaixaConsumoResponseDTO> getFaixasConsumo() {
        return faixasConsumo;
    }

    public void setFaixasConsumo(List<FaixaConsumoResponseDTO> faixasConsumo) {
        this.faixasConsumo = faixasConsumo;
    }
}
