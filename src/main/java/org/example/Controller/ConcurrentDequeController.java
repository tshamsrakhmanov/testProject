package org.example.Controller;

import org.example.CustomCache.ConcurrentHashDequeService;
import org.example.DTO.CommonMessageDTO;
import org.example.DTO.DequeBulkDTO;
import org.example.DTO.DequeListDTO;
import org.example.DTO.DequeSingleInputDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "Cache group", description = "Group to work with Concurrent Deque")
@RequestMapping(path = "deque")
public class ConcurrentDequeController {

  private final ConcurrentHashDequeService cache;

  @Operation(summary = "Get size", description = "Return sum count of entries in cache")
  @GetMapping(path = "/size")
  public CommonMessageDTO size() {

    CommonMessageDTO messageDTO = new CommonMessageDTO();
    messageDTO.setResult(String.valueOf(cache.getLength()));

    return messageDTO;

  }

  @Operation(summary = "Add entry at head", description = "Add value to head of deque")
  @PutMapping(path = "/entry")
  public ResponseEntity<?> put(@RequestBody DequeSingleInputDTO request) {

    log.info(" >>> Income: {}", request.toString());

    cache.insertToHead(request.getEntry());

    return ResponseEntity.status(HttpStatus.OK).build();

  }

  @Operation(summary = "Get entry from tail", description = "Delete and return entry from tail of deque")
  @GetMapping(path = "/entry")
  public ResponseEntity<?> get() throws Exception {

    String result = cache.getFromTail();
    if (result != null) {

      CommonMessageDTO messageDTO = new CommonMessageDTO();
      messageDTO.setResult(result);
      log.info("Response: {}", messageDTO);
      return ResponseEntity.status(HttpStatus.OK).body(messageDTO);
    } else {
      log.error("Deque is empty");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Deque is empty");
    }

  }

  @Operation(summary = "Preview of deque", description = "Return entries of all deque, no cleanup")
  @GetMapping(path = "/preview")
  public DequeListDTO getAll() {
    DequeListDTO list = new DequeListDTO();

    list.setDeque(cache.getAll());

    return list;

  }

  @Operation(summary = "Preview of deque", description = "Return entries of all deque, no cleanup")
  @PutMapping(path = "/bulk")
  public ResponseEntity<?> putBulk(@RequestBody DequeBulkDTO request) {

    cache.insertBulk(request.getEntries());

    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
