package com.example.bill.dto;

import com.example.bill.domain.ProductTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Builder
public class ProductDto {

    @Schema(description = "Name of the Product.", example = "Livres")
    public String name;

    @NotNull
    @Min(1)
    @Schema(description = "Quantity of the Product.Required field.Minimum value  is 1.", example = "2")
    public Integer quantity;

    @Schema(description = "Type of product, that can only be : \n" +
            "* FIRST_NEED => i.e. food and medicine ,No value added tax (VAT) is applied to the FIRST_NEED products \n" +
            " OR \n" +
            "* BOOK => A reduced Value Added Tax (VAT) of 10% is applied to the BOOK products \n" +
            " OR \n" +
            "* OTHER => Indicate other products , A normal Value Added Tax (VAT) of 20% is applied.", example = "BOOK")
    public ProductTypeEnum type;

    @NotNull
    @DecimalMin("0.01")
    @Schema(description = "Price of the Product.Minimum value is 1 cent.", example = "12.91")
    public BigDecimal price;

    @NotNull
    @Schema(description = "Indicate if the product is imported. An additional tax of 5% is applied on imported products, without exception.", example = "false")
    public Boolean imported;

    @JsonIgnore
    @Schema(description = "total price tax included for the Product. \n"+
            " Price ttc= Price ht + arrondi( Price ht * tva/100) + arrondi(Price ht *ti/100)\n")
    public BigDecimal ttcPrice;

    @JsonIgnore
    @Schema(description = "Tax Amount.")
    public BigDecimal taxAmount;

}
