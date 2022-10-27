package com.example.bill.dto;

import com.example.bill.dto.ProductDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class BillDto {

    @Schema(description = "Product list.")
    List<ProductDto> productDtoList;
    @Schema(description = "the sum of all the taxes rounded to 5 cents applied on the products.",example = "2.6")
    BigDecimal taxAmount;

    @Schema(description = "the sum of total price all the taxes included applied on the products.",example ="28.42")
    BigDecimal totalPrice;
}
