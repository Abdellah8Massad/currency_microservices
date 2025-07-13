package com.message.message.message;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.function.Function;
import com.message.message.dto.AccountsMsgDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class MessageFunctions {

    Logger logger = LoggerFactory.getLogger(MessageFunctions.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public Function<String, AccountsMsgDto> mail() {
        return message -> {
            try {
                AccountsMsgDto dto = objectMapper.readValue(message, AccountsMsgDto.class);
                logger.info("EMAIL Converting String to AccountsMsgDto : {}", dto);
                return dto;
            } catch (Exception e) {
                logger.error("Failed to convert message to AccountsMsgDto", e);
                return null;
            }
        };
    }

    @Bean
    public Function<String, Long> sms() {
        return message -> {
            try {
                AccountsMsgDto dto = objectMapper.readValue(message, AccountsMsgDto.class);
                logger.info("SMS Converting String to Long ID : {}", dto.accountId());
                return dto.accountId();
            } catch (Exception e) {
                logger.error("Failed to convert message to AccountsMsgDto", e);
                return null;
            }
        };
    }
}