package com.example.bill.service;

import com.example.bill.dto.BillDto;
import com.example.bill.dto.ProductDto;
import com.example.bill.utils.UtilBuilderProductDtoList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Stream;

/**
 * Test class for calculate bill service
 */
@SpringBootTest
class CalculateBillServiceTest {

    @Autowired
    CalculateBillService calculateBillService;

    public static List<ProductDto> productDtoList1 = UtilBuilderProductDtoList.buildProductDtoList1();
    public static List<ProductDto> importedProductDtoList = UtilBuilderProductDtoList.buildImportedProductDtoList();
    public static List<ProductDto> noneImportedProductDtoList = UtilBuilderProductDtoList.buildNoneImportedProductDtoList();

    /**
     * test parameterized to test CalculateBill for valid ProductDtosList
     * @param productDtoList - List of valid productDto
     * @param expectedTotalPrice - expected total Price
     * @param expectedTaxAmount - - expected tax amount
     */

    @ParameterizedTest
    @MethodSource("list_of_valid_productDtoList")
    void should_compute_bill_with_valid_productDtoList(List<ProductDto> productDtoList, BigDecimal expectedTotalPrice, BigDecimal expectedTaxAmount) {
        BillDto billDto = calculateBillService.calculateBill(productDtoList);
        Assertions.assertEquals(expectedTotalPrice, billDto.getTotalPrice());
        Assertions.assertEquals(expectedTaxAmount, billDto.getTaxAmount());
    }

    /**
     * list of valid productDto
     * @return should_compute_bill_with_valid_productDtoList argument :
     * productDtoList - List of valid productDto
     * expectedTotalPrice - expected total Price
     * expectedTaxAmount - - expected tax amount
     */
    private static Stream<Arguments> list_of_valid_productDtoList() {
        return Stream.of(
                Arguments.of(productDtoList1,BigDecimal.valueOf(145.72),BigDecimal.valueOf(19.00).setScale(2, RoundingMode.HALF_UP)),
                Arguments.of(importedProductDtoList,BigDecimal.valueOf(199.20).setScale(2,RoundingMode.HALF_UP),BigDecimal.valueOf(36.70).setScale(2,RoundingMode.HALF_UP)),
                Arguments.of(noneImportedProductDtoList,BigDecimal.valueOf(48.02),BigDecimal.valueOf(5.50).setScale(2,RoundingMode.HALF_UP))
        );
    }

}
