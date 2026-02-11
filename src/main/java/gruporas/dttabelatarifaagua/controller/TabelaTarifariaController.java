package gruporas.dttabelatarifaagua.controller;

import gruporas.dttabelatarifaagua.dto.PageMetadataDTO;
import gruporas.dttabelatarifaagua.dto.PageableDTO;
import gruporas.dttabelatarifaagua.dto.TabelaTarifariaCreateUpdateDTO;
import gruporas.dttabelatarifaagua.dto.TabelaTarifariaResponseDTO;
import gruporas.dttabelatarifaagua.model.TabelaTarifaria;
import gruporas.dttabelatarifaagua.service.TabelaTarifariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import gruporas.dttabelatarifaagua.mapper.TabelaTarifariaMapper;

@RestController
@RequestMapping("/api/v1/tabelas-tarifarias")
public class TabelaTarifariaController {

    @Autowired
    private TabelaTarifariaService tabelaTarifariaService;

    @Autowired
    private TabelaTarifariaMapper tabelaTarifariaMapper;

    @PostMapping
    public ResponseEntity<TabelaTarifariaResponseDTO> createTabelaTarifaria(@RequestBody TabelaTarifariaCreateUpdateDTO tabelaTarifariaRequestDTO) {
        TabelaTarifaria tabelaTarifaria = tabelaTarifariaMapper.toEntity(tabelaTarifariaRequestDTO);
        TabelaTarifaria createdTabela = tabelaTarifariaService.createTabelaTarifaria(tabelaTarifaria);
        return new ResponseEntity<>(tabelaTarifariaMapper.convertToResponseDTO(createdTabela), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageableDTO<TabelaTarifariaResponseDTO>> getAllTabelasTarifarias(@PageableDefault(size = 10, page = 0, sort = "nome") Pageable pageable) {
        Page<TabelaTarifaria> tabelas = tabelaTarifariaService.getAllTabelasTarifarias(pageable);

        PageMetadataDTO pageMetadata = new PageMetadataDTO(
                tabelas.getNumber(),
                tabelas.getSize(),
                tabelas.getTotalElements(),
                tabelas.getTotalPages()
        );

        List<TabelaTarifariaResponseDTO> content = tabelas.map(tabelaTarifariaMapper::convertToResponseDTO).getContent();

        PageableDTO<TabelaTarifariaResponseDTO> customResponse = new PageableDTO<>(content, pageMetadata);

        return ResponseEntity.ok(customResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TabelaTarifariaResponseDTO> getTabelaTarifariaById(@PathVariable UUID id) {
        TabelaTarifaria tabelaTarifaria = tabelaTarifariaService.getTabelaTarifariaById(id);
        return ResponseEntity.ok(tabelaTarifariaMapper.convertToResponseDTO(tabelaTarifaria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TabelaTarifariaResponseDTO> updateTabelaTarifaria(@PathVariable UUID id, @RequestBody TabelaTarifariaCreateUpdateDTO tabelaTarifariaDetailsDTO) {
        TabelaTarifaria tabelaTarifaria = tabelaTarifariaMapper.toEntity(tabelaTarifariaDetailsDTO);
        TabelaTarifaria updatedTabela = tabelaTarifariaService.updateTabelaTarifaria(id, tabelaTarifaria);
        return ResponseEntity.ok(tabelaTarifariaMapper.convertToResponseDTO(updatedTabela));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTabelaTarifaria(@PathVariable UUID id) {
        tabelaTarifariaService.deleteTabelaTarifaria(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
