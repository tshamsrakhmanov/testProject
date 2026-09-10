package org.example.Controller;

import java.util.UUID;

import org.example.DTO.CommonMessageDTO;
import org.example.DTO.OutputDTO;
import org.example.DTO.RestMessageDTO;
import org.example.KafkaProducerConfig.KafkaSender;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${custom.kafka_topic_write}")
  private String TOPIC;

  private final KafkaSender kafkaSender;

  @GetMapping(path = "/test")
  public OutputDTO someMethod(HttpServletRequest httpServletRequest) {

    log.info("Test handler invoked, {}", httpServletRequest.getMethod());

    OutputDTO outputDTO = new OutputDTO();
    outputDTO.setValue("some value");
    outputDTO.setCount(15);

    return outputDTO;
  }

  @PostMapping(path = "/business_logic_v1")
  public CommonMessageDTO sendKafkaMessageSync(@RequestBody RestMessageDTO requestDTO,
      HttpServletRequest httpServletRequest) {

    String traceId_string = UUID.randomUUID().toString().replace("-", "");

    log.info("HTTP request");
    log.info("method:{}, path: {}",
        httpServletRequest.getMethod(),
        httpServletRequest.getRequestURI());
    log.info("Request mapped: {}", requestDTO);

    kafkaSender.sendMessage(
        requestDTO.getMessageBody(),
        requestDTO.getMessageKey(),
        requestDTO.getMessageHeaders(),
        TOPIC);

    return new CommonMessageDTO("Success!", traceId_string);

  }

}
