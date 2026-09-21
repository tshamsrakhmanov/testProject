package org.example.Controller;

import java.util.ArrayList;
import java.util.List;

import org.example.DTO.DequeBulkDTO;
import org.example.DTO.PutCacheDTO;
import org.example.InternalDataBase.DataBaseEntity;
import org.example.InternalDataBase.DataBaseInterface;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Cache, based on H2", description = "Operations with H2 databse")
@RequestMapping(path = "h2")
public class InternalDatabaseController {

  private final DataBaseInterface dataBaseInterface;

  @Operation(summary = "Store value", description = "Put string value into storage with auto id and datetime stamp")
  @PutMapping(path = "/store")
  public void putValue(@RequestBody PutCacheDTO requestDTO) {
    log.info("Incoming: {}", requestDTO);
    DataBaseEntity entity = new DataBaseEntity();
    entity.setContent(requestDTO.getValue());
    dataBaseInterface.save(entity);
  }

  @Operation(summary = "Retirive all", description = "Return list of all entries in H2 internal database")
  @GetMapping(path = "/all")
  public DequeBulkDTO getAll() {
    List<String> list = new ArrayList<>();
    List<DataBaseEntity> entitesList = dataBaseInterface.findAll();
    for (DataBaseEntity entity : entitesList) {
      list.add(entity.getContent());
    }
    DequeBulkDTO dequeBulkDTO = new DequeBulkDTO();
    dequeBulkDTO.setEntries(list);
    return dequeBulkDTO;
  }
}
