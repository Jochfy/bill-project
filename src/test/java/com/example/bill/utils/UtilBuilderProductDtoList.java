package com.example.bill.utils;

import com.example.bill.domain.ProductTypeEnum;
import com.example.bill.dto.BillDto;
import com.example.bill.dto.ProductDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UtilBuilderProductDtoList {

    /**
     * build an example of productDto List for test
     * @return list of product Dto
     */
    public static List<ProductDto> buildProductDtoList1() {
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList.add(getProductDto("flacons de parfum", ProductTypeEnum.OTHER, BigDecimal.valueOf(27.99),Boolean.TRUE,2));
        productDtoList.add(getProductDto("flacon de parfum",ProductTypeEnum.OTHER,BigDecimal.valueOf(18.99),Boolean.FALSE,1));
        productDtoList.add(getProductDto("boîtes de pilules contre la migraine",ProductTypeEnum.FIRST_NEED,BigDecimal.valueOf(9.75),Boolean.FALSE,3));
        productDtoList.add(getProductDto("boîtes de chocolats",ProductTypeEnum.FIRST_NEED,BigDecimal.valueOf(11.25),Boolean.TRUE,2));
        return productDtoList;
    }

    /**
     * build an example of imported productDto List for test
     * @return list of product Dto
     */
    public static List<ProductDto> buildImportedProductDtoList() {
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList.add(getProductDto("boîtes de chocolats", ProductTypeEnum.FIRST_NEED, BigDecimal.valueOf(10),Boolean.TRUE,2));
        productDtoList.add(getProductDto("flacons de parfum",ProductTypeEnum.OTHER,BigDecimal.valueOf(47.50),Boolean.TRUE,3));
        return productDtoList;
    }

    /**
     * build an example of none imported productDto List for test
     * @return list of product Dto
     */
    public static List<ProductDto> buildNoneImportedProductDtoList() {
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList.add(getProductDto("livres", ProductTypeEnum.BOOK, BigDecimal.valueOf(12.49),Boolean.FALSE,2));
        productDtoList.add(getProductDto("CD musical",ProductTypeEnum.OTHER,BigDecimal.valueOf(14.99),Boolean.FALSE,1));
        productDtoList.add(getProductDto("barres de chocolat",ProductTypeEnum.FIRST_NEED,BigDecimal.valueOf(0.85),Boolean.FALSE,3));
        return productDtoList;
    }

    /**
     * Build ProductDto
     * @return list of product Dto
     */
    public static ProductDto getProductDto(String name,ProductTypeEnum productTypeEnum,BigDecimal price,Boolean imported,Integer quantity) {
        return ProductDto.builder()
                .name(name)
                .type(productTypeEnum)
                .price(price)
                .imported(imported)
                .quantity(quantity)
                .build();
    }

    /**
     * build an example of productDto List none valide Type for test
     * @return list of product Dto
     */
    public static List<ProductDto> buildProductDtoListNoNeValideType() {
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList.add(getProductDto("boîtes de pilules contre la migraine",null,BigDecimal.valueOf(9.75),Boolean.FALSE,3));
        return productDtoList;
    }

    /**
     * build an example of productDto List none valide Price for test
     * @return list of product Dto
     */
    public static List<ProductDto> buildProductDtoListNoNeValidePrice() {
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList.add(getProductDto("boîtes de pilules contre la migraine",ProductTypeEnum.OTHER,BigDecimal.valueOf(0),Boolean.FALSE,3));
        return productDtoList;
    }

    /**
     * build an example of productDto List none valide Quantity for test
     * @return list of product Dto
     */
    public static List<ProductDto> buildProductDtoListNoNeValideQuantity() {
        List<ProductDto> productDtoList = new ArrayList<>();
        productDtoList.add(getProductDto("boîtes de pilules contre la migraine",ProductTypeEnum.OTHER,BigDecimal.valueOf(0.02),Boolean.FALSE,0));
        return productDtoList;
    }

    /**
     * write ProductDtoList as string
     * @param productDtoList the list of productDto
     * @return the value of ProductDtoList as string
     * @throws JsonProcessingException Intermediate base class for all problems encountered when processing (parsing, generating) JSON content that are not pure I/O problems.
     */
    public static String writeProductDtoListAsString(List<ProductDto> productDtoList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(Objects.requireNonNull(productDtoList));
    }

    /**
     * build an example of productDto List example as String  for test
     * @return list of product Dto asString
     */
    public static String buildProductDtoList1AndWriteItAsString() throws JsonProcessingException {
        return writeProductDtoListAsString(buildProductDtoList1());
    }

    /**
     * build an example of productDto List none valid type example as String  for test
     * @return list of product Dto asString
     */
    public static String buildProductDtoListNoNeValideTypeAndWriteItAsString() throws JsonProcessingException {
        return writeProductDtoListAsString(buildProductDtoListNoNeValideType());
    }

    /**
     * build an example of productDto List none valide Price example as String  for test
     * @return list of product Dto asString
     */
    public static String buildProductDtoListNoNeValidePriceAndWriteItAsString() throws JsonProcessingException {
        return writeProductDtoListAsString(buildProductDtoListNoNeValidePrice());
    }

    /**
     * build an example of productDto List none valid quantity example as String  for test
     * @return list of product Dto asString
     */
    public static String buildProductDtoListNoNeValideQuantityAndWriteItAsString() throws JsonProcessingException {
        return writeProductDtoListAsString(buildProductDtoListNoNeValideQuantity());
    }

}
