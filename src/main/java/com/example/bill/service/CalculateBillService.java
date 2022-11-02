package com.example.bill.service;

import static com.example.bill.helpers.BillHelper.*;

import com.example.bill.domain.ProductTypeEnum;
import com.example.bill.dto.BillDto;
import com.example.bill.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * Service which provide calculate bill functionalities for the application
 */
@Service
@RequiredArgsConstructor
public class CalculateBillService {

    private static final  BigDecimal defaultTax = new BigDecimal("0");
    private static final  BigDecimal taxForImportedProduct = new BigDecimal("0.05");
    private static final  BigDecimal taxOnBooks = new BigDecimal("0.1");
    private static final  BigDecimal taxOnOtherProduct = new BigDecimal("0.2");


    /**
     * Calculate the bill
     *
     * @param productDtoList : - list of product on the shopping cart
     * @return Object BillDto details the bill listing each product and its price including VAT
     */
    public BillDto calculateBill(@NotNull @NotEmpty List<ProductDto> productDtoList) {
        productDtoList.forEach(this::calculatePriceTTC);
        return buildBill(productDtoList);
    }

    /**
     * build bill object from list of productDto aftercalculating all tax included price and tax amount.
     *
     * @param productDtoList list of products after calculating all tax included price and tax amount.
     * @return bill object that contains teh list of products, the total price and the total tax amount
     */
    private static BillDto buildBill(List<ProductDto> productDtoList) {
        return BillDto.builder()
                .productDtoList(productDtoList)
                .totalPrice(productDtoList.stream().map(ProductDto::getTtcPrice).reduce(BigDecimal.ZERO, BigDecimal::add))
                .taxAmount(productDtoList.stream().map(ProductDto::getTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }

    /**
     * Calculates the price all taxes included and tax amount of product
     *
     * @param productDto product of shoping cart
     */
    private void calculatePriceTTC(ProductDto productDto) {
        if (Objects.nonNull(productDto)) {
            BigDecimal taxAmount = getTaxAmount(productDto);
            // price without taxe
            BigDecimal htprice = productDto.getPrice().multiply(BigDecimal.valueOf(productDto.quantity));
            productDto.setTtcPrice(htprice.add(taxAmount));
            productDto.setTaxAmount(taxAmount);
        }
    }

    /**
     * tax amount included value added and import tax
     *
     * @param productDto product which tax amount is calculated
     * @return value of taxAmount rounded to 5 cent
     */
    private BigDecimal getTaxAmount(ProductDto productDto) {
        BigDecimal priceTVARoundedTo5Cent = getTVARoundedTo5Cent(productDto.price, productDto.type);
        BigDecimal priceTIRoundedTo5Cent = getTIRoundedTo5Cent(Boolean.TRUE.equals(productDto.imported), productDto.price);
        return priceTVARoundedTo5Cent.add(priceTIRoundedTo5Cent).multiply(BigDecimal.valueOf(productDto.quantity));
    }

    /**
     * value added tax applied on price of product and rounded to 5 cent
     *
     * @param productType indicate teh product type
     * @param price       price of the product which the tax is calculated
     * @return value of import tax applied on price of product rounded to 5 cent
     */
    private BigDecimal getTVARoundedTo5Cent(BigDecimal price, ProductTypeEnum productType) {
        BigDecimal priceTVARoundedTo5Cent = defaultTax;
        if ((ProductTypeEnum.BOOK.equals(productType) || ProductTypeEnum.OTHER.equals(productType))) {
            priceTVARoundedTo5Cent = roundTo5Cent(calculateTVA(price, productType));
        }
        return priceTVARoundedTo5Cent;
    }

    /**
     * Import tax applied on price of product and rounded to 5 cent
     *
     * @param imported indicate whether the product which the tax is calculated is imported
     * @param price    price of the product which the tax is calculated
     * @return value of import tax applied on price of product
     */
    private BigDecimal getTIRoundedTo5Cent(boolean imported, BigDecimal price) {
        BigDecimal priceTIRoundedTo5Cent = defaultTax;
        if (imported) {
            priceTIRoundedTo5Cent = roundTo5Cent(calculateTI(price));
        }
        return priceTIRoundedTo5Cent;
    }

    /**
     * Calculates the value added tax of product according to his type
     *
     * @param price       price of the product which the tax is calculated
     * @param productType indicate the product type
     * @return the value added tax
     */
    private static BigDecimal calculateTVA(BigDecimal price, ProductTypeEnum productType) {
        return switch (productType) {
            case BOOK -> getTaxValue(price, taxOnBooks);
            case OTHER -> getTaxValue(price, taxOnOtherProduct);
            default -> defaultTax;
        };
    }

    /**
     * Calculates the import tax applied on price
     *
     * @param price the price of rpoduct
     * @return the value of import tax
     */
    private static BigDecimal calculateTI(BigDecimal price) {
        return getTaxValue(price, taxForImportedProduct);
    }

    /**
     * Round up taxes to the next 5 cents
     *
     * @param value the value to round
     * @return the value rounded to the next 5 cents
     */
    private BigDecimal roundTo5Cent(BigDecimal value) {
        return round(value, new BigDecimal("0.05"), RoundingMode.UP);
    }

    private static BigDecimal getTaxValue(BigDecimal price, BigDecimal tax) {
        return price.multiply(tax);
    }
}
