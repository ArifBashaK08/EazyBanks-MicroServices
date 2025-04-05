package com.udemy.eazybytes.cards;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.udemy.eazybytes.cards.dto.CardsContactInfoDTO;


@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value={CardsContactInfoDTO.class})
//For Documentation purpose at "/swagger-ui.html" endpoint
@OpenAPIDefinition(
        info = @Info(
                title = "Cards micro-services REST API Documentation",
                description = "EazyBank Cards micro-services REST API Documentation",
                version = "v1", contact = @Contact(
                name = "Arif Basha K.",
                email = "arifbasha@gmail.com",
                url = "https://arifbashak.netlify.app"
        ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://licenceurl.here"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "EazyBank Cards micro-services REST API Documentation",
                url = "https://eazybank.go/swagger-ui.html"
        )
)
/// --------------------- ///
public class CardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardsApplication.class, args);
    }

}
