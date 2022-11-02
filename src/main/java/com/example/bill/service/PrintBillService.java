package com.example.bill.service;

import com.example.bill.dto.BillDto;
import com.example.bill.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

import static com.example.bill.helpers.LabelConstantHelper.*;

/**
 * Service which provide print bill functionalities for the application
 */
@Service
@RequiredArgsConstructor
public class PrintBillService {

    /**
     * Print the bill listing each product as well as their price All Tax included .
     * @param bill represent the list of product wich the total price and tax amount is calculated
     * @return string details and list each product as well as their price All Tax included .
     */
    public String printBill(@NotNull BillDto bill){
        StringBuilder b = new StringBuilder();
        //the head of the bill
        b.append(ASTERISK_SIGN).
                append(SPACE).
                append(HEAD_OF_BILL).
                append(ASTERISK_SIGN).
                append(System.getProperty(LINE_SEPARATOR));
        //Print list of products
        if(bill.getProductDtoList() != null && !bill.getProductDtoList().isEmpty()){
            bill.getProductDtoList().forEach(productDto ->
                buildStringForProducts(b, productDto));
        }
        //print the tax amount
        b.append(TAX_AMOUNT_LABEL).
                append(SPACE).
                append(COLON_SEPARATION_SIGN).
                append(SPACE).
                append(bill.getTaxAmount().toString()).append(CURRENCY_SIGN).append(System.getProperty(LINE_SEPARATOR));
        //print the total amount
        b.append(TOTAL_AMOUNT_LABEL).
                append(SPACE).
                append(COLON_SEPARATION_SIGN).
                append(SPACE).
                append(bill.getTotalPrice().toString()).append(CURRENCY_SIGN);
        return b.toString();
    }

    /**
     * build the sentence for a product
     * @param b stringbuilder to build
     * @param productDto the product to detail
     * @return a string detail a product
     */
    public static StringBuilder buildStringForProducts(StringBuilder b, @NotNull ProductDto productDto) {

         b.append(ASTERISK_SIGN).
                append(productDto.quantity).
                append(SPACE).
                append(productDto.name).
                append(SPACE);

        if (productDto.imported)
            b.append(IMPORTED).append(SPACE);

        return  b.append(À).
                append(SPACE).
                append(productDto.price).
                append(CURRENCY_SIGN).
                append(SPACE).
                append(EXCLUSIVE_OF_TAXES).
                append(SPACE).
                append(COLON_SEPARATION_SIGN).
                append(SPACE).
                append(productDto.getTtcPrice()).
                append(CURRENCY_SIGN).
                append(SPACE).
                append(INCLUSIVE_OF_TAXES).
                append(System.getProperty(LINE_SEPARATOR));
    }

}
