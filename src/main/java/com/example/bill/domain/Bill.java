package com.example.bill.domain;

import com.example.bill.dto.ProductDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class Bill {

    List<ProductDto> productDtoList;
    BigDecimal taxAmount;
    BigDecimal totalPrice;
}
