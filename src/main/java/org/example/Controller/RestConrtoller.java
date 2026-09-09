package org.example.Controller;

import org.example.DTO.OutputDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestConrtoller {

  @GetMapping(path = "/some")
  public OutputDTO someMethod() {
    OutputDTO outputDTO = new OutputDTO();
    outputDTO.setValue("some value");
    outputDTO.setCount(15);
    return outputDTO;
  }

}
