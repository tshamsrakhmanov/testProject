package org.example.Controller;

import org.example.CustomCache.CacheService;
import org.example.DTO.CommonMessageDTO;
import org.example.DTO.PutCacheDTO;
import org.example.DTO.TotalCacheDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Cache group", description = "Group to work with ConcurrentHashMap")
public class ConcurrentHashMapController {

  private final CacheService cacheService;

  @Operation(summary = "Store value", description = "Put string in cache, with return of entry's UUID - to fetch later")
  @PutMapping(path = "/cache")
  public CommonMessageDTO putInCache(@RequestBody PutCacheDTO requestDTO) {

    return new CommonMessageDTO(cacheService.put(requestDTO.getValue()));

  }

  @Operation(summary = "Get value by UUID", description = "Delete-return single entry by given UUID")
  @GetMapping(path = "/cache")
  public ResponseEntity<?> getInCache(@RequestBody PutCacheDTO requestDTO) {

    String result = cacheService.get(requestDTO.getValue());

    if (result != null) {
      return ResponseEntity.status(HttpStatus.OK).body(result);
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

  }

  @Operation(summary = "Cache list", description = "Retrieves full list of all entries in cache")
  @GetMapping(path = "/all_cache")
  public TotalCacheDTO getAllCache() {
    TotalCacheDTO result = new TotalCacheDTO();
    result.setValues(cacheService.getAll());
    return result;
  }

  @Operation(summary = "Clear cache", description = "Complete erase of cache")
  @GetMapping(path = "/clear")
  public ResponseEntity<?> clear() {
    cacheService.clear();
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
