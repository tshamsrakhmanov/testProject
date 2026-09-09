package org.example.Controller;

import java.util.UUID;

import org.example.DTO.OutputDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RestConrtoller {

  @GetMapping(path = "/some")
  public OutputDTO someMethod(HttpServletRequest httpServletRequest) {
    String traceId_string = UUID.randomUUID().toString().replace("-", "");

    log.info("{} | {}", traceId_string, httpServletRequest.getMethod());

    OutputDTO outputDTO = new OutputDTO();
    outputDTO.setValue("some value");
    outputDTO.setCount(15);

    return outputDTO;
  }

}
