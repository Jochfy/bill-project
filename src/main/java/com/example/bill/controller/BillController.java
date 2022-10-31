package com.example.bill.controller;

import com.example.bill.dto.ApiErrorDto;
import com.example.bill.dto.BillDto;
import com.example.bill.dto.ProductDto;
import com.example.bill.service.CalculateBillService;
import com.example.bill.service.PrintBillService;
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
import static com.example.bill.BillApplication.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Bill Api
 */
@RestController
@RequestMapping(BillController.API_PATH)
@Validated
@RequiredArgsConstructor
public class BillController {

    public static final String API_PATH = BASE_API_PATH+ "/api";
    private final CalculateBillService calculateBillService;
    private final PrintBillService printBillService;

    /**
     * Calculation and print of bill taxes on a shopping cart
     * @param productDtoList list of product on the shopping cart which the bill details
     * @return Returns writing bill listing each product and its price including VAT
     */
    @PostMapping("/print")
    @Operation(summary = "Calculation and print of bill taxes on a shopping cart , " +
                         "Returns writing bill listing each product and its price including VAT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = @Content(schema =  @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema =  @Schema(implementation = ApiErrorDto.class)))})
    ResponseEntity<String> CalculateAndPrintBill(@Valid @RequestBody List<ProductDto> productDtoList) {
        return new ResponseEntity<>(printBillService.printBill(calculateBillService.CalculateBill(productDtoList)),HttpStatus.OK);
    }

    /**
     * Calculation of bill taxes on a shopping cart
     * @param productDtoList list of product on the shopping cart which the bill details
     * @return Returns Object bill listing each product and its price including VAT
     */
    @PostMapping()
    @Operation(summary = "Calculation of taxes on a shopping cart , " +
            "Returns Object bill listing each product and its price including VAT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response",
                    content = @Content(schema =  @Schema(implementation = BillDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema =  @Schema(implementation = ApiErrorDto.class))) })
    ResponseEntity<BillDto> CalculateBill(@Valid @RequestBody @NotEmpty @NotNull List<ProductDto> productDtoList) {
        return new ResponseEntity<>(calculateBillService.CalculateBill(productDtoList),HttpStatus.OK);
    }

}
