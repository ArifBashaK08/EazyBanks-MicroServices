package com.udemy.eazybanks.messageq.function;

import com.udemy.eazybanks.messageq.dto.AccountMsgDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;


@Configuration
public class MessageFunctions {
    private static final Logger logger = LoggerFactory.getLogger(MessageFunctions.class);

    @Bean
    public Function<AccountMsgDTO, AccountMsgDTO> email(){
        return accountMsgDTO -> {
            logger.info("Sending email with the details : {}", accountMsgDTO.toString());
            return accountMsgDTO;
        };
    }

    @Bean
    public Function<AccountMsgDTO, Long> sms(){
        return accountMsgDTO -> {
            logger.info("Sending sms with the details : {}", accountMsgDTO.toString());
            return accountMsgDTO.accountNumber();
        };
    }
}
