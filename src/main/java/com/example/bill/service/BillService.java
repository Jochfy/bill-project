package com.example.bill.service;

import com.example.bill.domain.ProductTypeEnum;
import com.example.bill.domain.Bill;
import com.example.bill.dto.ProductDto;
import com.example.bill.helpers.BillHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BillService {

    BillHelper billHelper;

    /**
     *  Calculate and write the bill
     * @param productDtoList
     * @return
     * String details the bill
     */
    public String CalculateAndWriteBill (List<ProductDto> productDtoList) {
        if(!productDtoList.isEmpty()){
            productDtoList.stream().forEach((productDto) -> calculatePriceTTC(productDto));
            Bill bill = Bill.builder()
                    .productDtoList(productDtoList)
                    .totalPrice(productDtoList.stream().map(ProductDto::getTtcPrice).reduce(BigDecimal.ZERO, BigDecimal::add))
                    .taxAmount(productDtoList.stream().map(ProductDto::getTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
                    .build();
            return writeBill(bill);
        }else {
            return "La liste des produits ne doit pas être vide" ;
        }
    }

    /**
     * Calculates the price all taxes included
     * @param productDto
     */
    public void calculatePriceTTC (ProductDto productDto){
        if (Objects.nonNull(productDto)) {
            BigDecimal priceTVA = calculateTVA(productDto.price , productDto.type);
            BigDecimal priceTI  = calculateTI(productDto.price, productDto.imported);
            BigDecimal taxAmount = roundTo5Cent(priceTVA).add(roundTo5Cent(priceTI));
            BigDecimal TtcPrice = productDto.getPrice().add(taxAmount);
            productDto.setTtcPrice(TtcPrice.multiply(BigDecimal.valueOf(productDto.quantity)));
            productDto.setTaxAmount(taxAmount.multiply(BigDecimal.valueOf(productDto.quantity)));
        }
    }

    /**
     * Calculates the value added tax
     * @param price
     * @param productType
     * @return
     */
    private BigDecimal calculateTVA (BigDecimal price , ProductTypeEnum productType){
        return switch (productType) {
            case BOOK -> price.multiply(new BigDecimal("0.1"));
            case OTHER -> price.multiply(new BigDecimal("0.2"));
            default -> new BigDecimal("0") ;
        };
    }

    /**
     * Calculates the import tax
     * @param price
     * @param imported
     * @return
     */
    private BigDecimal calculateTI (BigDecimal price , Boolean imported){
        return Boolean.TRUE.equals(imported) ? price.multiply(new BigDecimal("0.05")) : new BigDecimal("0");
    }

    /**
     * Round up taxes to the next 5 cents
     * @param value
     * @return
     */
    public BigDecimal roundTo5Cent(BigDecimal value){
        return billHelper.round(value,new BigDecimal("0.05"),RoundingMode.UP);
    }

    /**
     * Write the bill listing each product as well as their price All Tax included .
     * @param billDto
     * @return
     */
    private static String writeBill(Bill billDto){
        StringBuilder b = new StringBuilder();
        b.append("**** Facture ****").append(System.getProperty("line.separator"));
        billDto.getProductDtoList().forEach(productDto -> {
            buildStringForProducts(b, productDto);
        });
        b.append("Montant des taxes : ").append(billDto.getTaxAmount().toString()).append("€").append(System.getProperty("line.separator"));
        b.append("Total : ").append(billDto.getTotalPrice().toString()).append("€");
        return b.toString();
    }

    /**
     * @param b
     * @param productDto
     * @return
     */
    private static StringBuilder buildStringForProducts(StringBuilder b, ProductDto productDto) {
        return b.append("* ").
                append(productDto.quantity).
                append(" ").
                append(productDto.name).
                append(" à ").
                append(productDto.price).
                append("€ HT : ").
                append(productDto.getTtcPrice()).
                append("€ TTC").
                append(System.getProperty("line.separator"));
    }

}
