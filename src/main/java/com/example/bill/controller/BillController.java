package com.example.bill.controller;

import com.example.bill.dto.ProductDto;
import com.example.bill.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping("/bill")
    @Operation(summary = "Calculation of taxes on a shopping cart , " +
                         "Returns bill listing each product and its price including VAT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    ResponseEntity<String> CalculateBill (@Valid @RequestBody List<ProductDto> productDtoList) {
        return new ResponseEntity<>(billService.CalculateAndWriteBill(productDtoList),HttpStatus.OK);
    }

}
