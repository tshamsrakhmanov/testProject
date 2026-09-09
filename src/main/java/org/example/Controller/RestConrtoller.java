package org.example.Controller;

import java.util.UUID;

import org.example.DTO.CommonMessageDTO;
import org.example.DTO.OutputDTO;
import org.example.DTO.RestMessageDTO;
import org.example.KafkaProducerConfig.KafkaSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class RestConrtoller {

  private final KafkaSender kafkaSender;

  @GetMapping(path = "/test")
  public OutputDTO someMethod(HttpServletRequest httpServletRequest) {
    String traceId_string = UUID.randomUUID().toString().replace("-", "");

    log.info("{} | {}", traceId_string, httpServletRequest.getMethod());

    OutputDTO outputDTO = new OutputDTO();
    outputDTO.setValue("some value");
    outputDTO.setCount(15);

    return outputDTO;
  }

  @PostMapping(path = "/send_message")
  public CommonMessageDTO sendKafkaMessageSync(@RequestBody RestMessageDTO requestDTO,
      HttpServletRequest httpServletRequest) {

    String traceId_string = UUID.randomUUID().toString().replace("-", "");

    log.info("{} | HTTP request", traceId_string);
    log.info("{} | method:{}, path: {}", traceId_string, httpServletRequest.getMethod(),
        httpServletRequest.getRequestURI());
    log.info("{} | Request mapped: {}", traceId_string, requestDTO);

    kafkaSender.sendMessage(
        requestDTO.getMessageBody(),
        requestDTO.getMessageKey(),
        requestDTO.getMessageHeaders(),
        requestDTO.getTopic(),
        traceId_string);

    return new CommonMessageDTO("Success!", traceId_string);

  }

}
