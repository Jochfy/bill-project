package com.example.bill.controller;

import com.example.bill.dto.ProductDto;
import com.example.bill.utils.UtilBuilderProductDtoList;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * BillControllerIT - Integration test for BillController class
 * Test BillController rest api
 */
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
class BillControllerIT {

    @Autowired
    MockMvc mockMvc;

    public static String productDtoListNoneValidTypeRequest;
    public static String productDtoListNoneValidePriceRequest;
    public static String productDtoListNoneValideQuantityRequest;

    static {
        try {
            productDtoListNoneValidTypeRequest = UtilBuilderProductDtoList.buildProductDtoListNoNeValideTypeAndWriteItAsString();
            productDtoListNoneValidePriceRequest = UtilBuilderProductDtoList.buildProductDtoListNoNeValidePriceAndWriteItAsString();
            productDtoListNoneValideQuantityRequest = UtilBuilderProductDtoList.buildProductDtoListNoNeValideQuantityAndWriteItAsString();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * test of rest api which calculate bill should return OK
     */
    @Test
    void calculate_bill_with_productDto_list_should_return_ok() throws Exception {
        String requestJson = UtilBuilderProductDtoList.buildProductDtoList1AndWriteItAsString();
        mockMvc.perform(post(BillController.API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    /**
     * test of rest api which calculate and print bill should return OK
     */
    @Test
     void calculate_bill_and_print_with_productDto_list_should_return_ok() throws Exception {
        String requestJson = UtilBuilderProductDtoList.buildProductDtoList1AndWriteItAsString();
        mockMvc.perform(post(BillController.API_PATH + BillController.CALCULATING_AND_PRINT_BILL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    /**
     * test none valid productList for rest api which calculate bill should return bad request
     */
    @ParameterizedTest
    @MethodSource("list_of_non_valid_productDtoList")
     void calculate_bill_and_print_with_none_valid_productDto_list_should_return_bad_request(String requestJson, String message1, String message2) throws Exception {
        mockMvc.perform(post(BillController.API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> Assertions.assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(message1)))
                .andExpect(result -> Assertions.assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(message2)));
    }

    /**
     * list of none valid ProductDto
     * @return ProductDto as string
     */
    private static Stream<Arguments> list_of_non_valid_productDtoList() {
        return Stream.of(
                Arguments.of(productDtoListNoneValidePriceRequest, "CalculateBill.productDtoList[0].price", "must be greater than or equal to " + ProductDto.MIN_PRICE),
                Arguments.of(productDtoListNoneValideQuantityRequest, "CalculateBill.productDtoList[0].quantity", "must be greater than or equal to " + ProductDto.MIN_QUANTITY),
                Arguments.of(productDtoListNoneValidTypeRequest, "CalculateBill.productDtoList[0].type", "must not be null")
        );
    }

}
