package org.example.Controller;

import org.example.CustomCache.CacheService;
import org.example.DTO.CommonMessageDTO;
import org.example.DTO.OutputDTO;
import org.example.DTO.PutCacheDTO;
import org.example.DTO.RestMessageDTO;
import org.example.KafkaProducerConfig.KafkaSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class RestConrtoller {

  @Value("${custom.kafka_topic_write}")
  private String TOPIC;

  private final KafkaSender kafkaSender;

  private final CacheService cacheService;

  @GetMapping(path = "/test")
  public OutputDTO someMethod() {

    OutputDTO outputDTO = new OutputDTO();
    outputDTO.setValue("some value");
    outputDTO.setCount(15);

    return outputDTO;
  }

  @PostMapping(path = "/business_logic_v1")
  public CommonMessageDTO businessLogicV1(@RequestBody RestMessageDTO requestDTO) {

    kafkaSender.sendMessage(
        requestDTO.getMessageBody(),
        requestDTO.getMessageKey(),
        requestDTO.getMessageHeaders(),
        TOPIC);

    return new CommonMessageDTO("Success!");

  }

  @PostMapping(path = "/business_logic_v2")
  public CommonMessageDTO businessLogicV2(@RequestBody RestMessageDTO requestDTO) {

    kafkaSender.sendMessageAsync(
        requestDTO.getMessageBody(),
        requestDTO.getMessageKey(),
        requestDTO.getMessageHeaders(),
        TOPIC,
        3000L);

    return new CommonMessageDTO("Success!");

  }

  @PutMapping(path = "/cache")
  public CommonMessageDTO putInCache(@RequestBody PutCacheDTO requestDTO) {

    return new CommonMessageDTO(cacheService.put(requestDTO.getValue()));

  }

  @GetMapping(path = "/cache")
  public CommonMessageDTO getInCache(@RequestBody PutCacheDTO requestDTO) throws Exception {

    String result = cacheService.get(requestDTO.getValue());

    if (result != null) {
      return new CommonMessageDTO(result);
    } else {
      throw new Exception("Not found in cache");
    }

  }
}
