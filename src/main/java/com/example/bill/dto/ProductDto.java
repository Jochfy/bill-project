package com.example.bill.dto;

import com.example.bill.domain.ProductTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class ProductDto {

    public String name;
    @NotNull
    @Min(1)
    public Integer quantity;
    public ProductTypeEnum type;
    @NotNull
    @DecimalMin("0.01")
    public BigDecimal price;
    @NotNull
    public Boolean imported;

    @JsonIgnore
    public BigDecimal ttcPrice;

    @JsonIgnore
    public BigDecimal taxAmount;

}
