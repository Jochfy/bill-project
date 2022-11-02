package com.example.bill.service;

import com.example.bill.dto.BillDto;
import com.example.bill.dto.ProductDto;
import com.example.bill.utils.UtilBuilderProductDtoList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;

/**
 * test class for printBill service
 */
@SpringBootTest
public class PrintBillServiceTest {

    @Autowired
    PrintBillService printBillService;

    @Autowired
    CalculateBillService calculateBillService;

    public static List<ProductDto> productDtoList1 = UtilBuilderProductDtoList.buildProductDtoList1();


    /**
     * test should print bill
     */
    @Test
    public void should_print_bill() {
        BillDto billDto = calculateBillService.CalculateBill(productDtoList1);
        String printBill = printBillService.printBill(billDto);
        ProductDto productDto0 = Objects.requireNonNull(productDtoList1.get(0));
        StringBuilder lineOfBillPrinted = PrintBillService.buildStringForProducts(new StringBuilder(),productDto0);
        Assertions.assertTrue(printBill.contains(String.valueOf(lineOfBillPrinted)));
    }


}
