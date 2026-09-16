package org.example.Controller;

import org.example.DTO.CommonMessageDTO;
import org.example.DTO.RestMessageDTO;
import org.example.KafkaProducerConfig.KafkaSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class RestConrtoller {

  @Value("${custom.kafka_topic_out}")
  private String topic_out;

  private final KafkaSender kafkaSender;

  @Tag(name = "Kafka group", description = "Send message to OUT topic with no delay")
  @PostMapping(path = "/kafka_topic_out_no_delay")
  public CommonMessageDTO businessLogicV1(@RequestBody RestMessageDTO requestDTO) {

    kafkaSender.sendMessage(
        requestDTO.getMessageBody(),
        requestDTO.getMessageKey(),
        requestDTO.getMessageHeaders(),
        topic_out);

    return new CommonMessageDTO();

  }

  @Tag(name = "Kafka group", description = "Send message to OUT topic with delay")
  @PostMapping(path = "/kafka_topic_out_with_delay")
  public CommonMessageDTO businessLogicV2(@RequestBody RestMessageDTO requestDTO) {

    kafkaSender.sendMessageAsync(
        requestDTO.getMessageBody(),
        requestDTO.getMessageKey(),
        requestDTO.getMessageHeaders(),
        topic_out,
        3000L);

    return new CommonMessageDTO();

  }

}
