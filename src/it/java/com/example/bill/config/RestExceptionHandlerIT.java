package com.example.bill.config;

import com.example.bill.controller.BillController;
import com.example.bill.domain.ProductTypeEnum;
import com.example.bill.dto.ProductDto;
import static com.example.bill.config.RestExceptionHandler.*;
import com.example.bill.service.CalculateBillService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * RestExceptionHandlerIT -
 *
 */
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RestExceptionHandlerIT {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CalculateBillService calculateBillService;

    /**
     * test return bad request error on ConstraintViolationException
     * @throws Exception Checked exceptions throws clause
     */
    @Test
    public void should_return_bad_request_error_on_ConstraintViolationException() throws Exception {
        String requestJson = writeProductDtoListAsString(new ArrayList<>());
        mockMvc.perform(post( BillController.API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> Assertions.assertEquals("CalculateBill.productDtoList: must not be empty", Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }


    /**
     * Test return unsupported media Type error on HttpMediaTypeNotSupportedException
     * @throws Exception Checked exceptions throws clause
     */
    @Test
    public void should_return_unsupported_media_Type_error_on_httpMediaTypeNotSupportedException() throws Exception {
        mockMvc.perform(post(BillController.API_PATH)
                        .contentType(MediaType.APPLICATION_ATOM_XML)
                        .content("requestJson"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof HttpMediaTypeNotSupportedException))
                .andExpect(result -> Assertions.assertTrue(Objects.requireNonNull(result.getResponse().getContentAsString()).contains(HTTP_MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION_MESSAGE)));
    }

    /**
     * Test return internal server Type error on RuntimeException
     * @throws Exception Checked exceptions throws clause
     */
    @Test
    public void should_return_internal_server_error_on_exception() throws Exception {
        List<ProductDto> productDtoList = buildProductDtoListExample();
        Mockito.when(calculateBillService.CalculateBill(productDtoList)).thenThrow(new RuntimeException());
        mockMvc.perform(post( BillController.API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writeProductDtoListAsString(productDtoList)))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> Assertions.assertTrue(Objects.requireNonNull(result.getResponse().getContentAsString()).contains(EXCEPTION_MESSAGE)));
    }

    /**
     * build an example of productDto List for test
     * @return list of product Dto
     */
    private  List<ProductDto> buildProductDtoListExample() {
        List<ProductDto> productDtoList = new ArrayList<>();
        ProductDto productDto = ProductDto.builder()
                .name("flacons de parfum")
                .type(ProductTypeEnum.OTHER)
                .price(BigDecimal.valueOf(27.99))
                .imported(Boolean.TRUE)
                .quantity(2)
                .build();
        productDtoList.add(productDto);
        return productDtoList;
    }

    /**
     * write ProductDtoList as string
     * @param productDtoList the list of productDto
     * @return the value of ProductDtoList as string
     * @throws JsonProcessingException Intermediate base class for all problems encountered when processing (parsing, generating) JSON content that are not pure I/O problems.
     */
    private static String writeProductDtoListAsString(List<ProductDto> productDtoList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(productDtoList);
    }
}
