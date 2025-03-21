package com.udemy.eazybytes.cards.controllers;

import com.udemy.eazybytes.cards.constants.CardsConstants;
import com.udemy.eazybytes.cards.dto.CardsContactInfoDTO;
import com.udemy.eazybytes.cards.dto.CardsDTO;
import com.udemy.eazybytes.cards.dto.ErrorResponseDTO;
import com.udemy.eazybytes.cards.dto.ResponseDTO;
import com.udemy.eazybytes.cards.services.ICardsServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST APIs for Cards in EazyBank",
        description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE card details"
)
@RestController
@RequestMapping(
        path = "/api/cards",
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
@RequiredArgsConstructor
@Validated
public class CardsController {

    private final ICardsServices iCardsServices;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private CardsContactInfoDTO cardsContactInfoDTO;

    @Operation(
            summary = "Create Card REST API",
            description = "REST API to create new Card inside EazyBank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    }
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createNewCard(@RequestParam
                                                     @Pattern(regexp = "^[0-9]{10}$",
                                                             message = "Mobile number must have 10 digits")
                                                     String mobileNumber) {
        iCardsServices.createCard(mobileNumber);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Fetch Card Details REST API",
            description = "REST API to fetch Card details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    }
    )
    @GetMapping("/fetch")
    public ResponseEntity<CardsDTO> fetchCardDetails(@RequestParam
                                                     @Pattern(
                                                             regexp = "^[0-9]{10}$",
                                                             message = "Mobile number should be 10 digits") String mobileNumber) {
        CardsDTO cardDetails = iCardsServices.fetchCardDetails(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(cardDetails);
    }

    @Operation(
            summary = "Update Card Details REST API",
            description = "REST API to update Card details based on a Card number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    }
    )
    @PatchMapping("/update")
    public ResponseEntity<ResponseDTO> updateCardDetails(@Valid @RequestBody CardsDTO cardsDTO) {
        boolean isUpdated = iCardsServices.updateCard(cardsDTO);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(CardsConstants.STATUS_200,
                            CardsConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDTO(CardsConstants.STATUS_417,
                            CardsConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(
            summary = "Delete Card Details REST API",
            description = "REST API to delete Card details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            )
    }
    )
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteCardDetails(@RequestParam
                                                         @Pattern(regexp = "^[0-9]{10}$",
                                                                 message = "Mobile number should be 10 digits") String mobileNumber) {
        boolean isDeleted = iCardsServices.deleteCard(mobileNumber);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(CardsConstants.STATUS_200,
                            CardsConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDTO(CardsConstants.STATUS_417,
                            CardsConstants.MESSAGE_417_DELETE));
        }
    }

    //======================= Profiling Demo =======================//
    @Operation(
            summary = "Gives build Details",
            description = "REST API to get build details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    @Operation(
            summary = "Gives Java version",
            description = "REST API to get Java version"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }

    @Operation(
            summary = "Gives Java version",
            description = "REST API to get Java version"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/contact-info")
    public ResponseEntity<CardsContactInfoDTO> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cardsContactInfoDTO);
    }

}
