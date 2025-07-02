package au.com.belong.phone.management.unitTests.controller;

import au.com.belong.phone.management.controller.PhoneNumbersController;
import au.com.belong.phone.management.dto.response.PhoneNumbersResponse;
import au.com.belong.phone.management.exception.ValidInputRequiredException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.List;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_EMAIL_ID_FORMAT_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_NAME_FORMAT_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_NOT_FOUND_CODE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.VALID_CUSTOMER_NAME_EMAIL_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.VALID_INPUT_REQUIRED_CODE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhoneNumbersController.class)
class PhoneNumbersControllerGetCustomerPhoneNumbersTest extends BasePhoneNumbersControllerTest {

    @Test
    void getPhoneNumberDetailsForCustomer_returnsErrorResponse_EmptyCustomerName() throws Exception {
        given(mockPhoneNumbersService.getPhoneNumbersForCustomer("%20%20%20", cpnPageable))
                .willThrow(new ValidInputRequiredException(
                        VALID_INPUT_REQUIRED_CODE, VALID_CUSTOMER_NAME_EMAIL_MESSAGE));

        mockMvc.perform(get(GET_USER_NUMBER_ENDPOINT + "%20%20%20"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(VALID_INPUT_REQUIRED_CODE))
                .andExpect(jsonPath("$.reason").value(VALID_CUSTOMER_NAME_EMAIL_MESSAGE))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getPhoneNumberDetailsForCustomer_returnsErrorResponse_InvalidFormatCustomerName() throws Exception {
        given(mockPhoneNumbersService.getPhoneNumbersForCustomer("12John", cpnPageable))
                .willThrow(new ValidInputRequiredException(
                        VALID_INPUT_REQUIRED_CODE, CUSTOMER_NAME_FORMAT_MESSAGE));

        mockMvc.perform(get(GET_USER_NUMBER_ENDPOINT + "12John"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(VALID_INPUT_REQUIRED_CODE))
                .andExpect(jsonPath("$.reason").value(CUSTOMER_NAME_FORMAT_MESSAGE))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @ParameterizedTest
    @ValueSource(strings = {".com@tempid", "tempId@com", "tempId@@@@com"})
    void getPhoneNumberDetailsForCustomer_returnsErrorResponse_InvalidFormatCustomerEmailId(
            String customerEmailId) throws Exception {
        given(mockPhoneNumbersService.getPhoneNumbersForCustomer(customerEmailId, cpnPageable))
                .willThrow(new ValidInputRequiredException(
                        VALID_INPUT_REQUIRED_CODE, CUSTOMER_EMAIL_ID_FORMAT_MESSAGE));

        mockMvc.perform(get(GET_USER_NUMBER_ENDPOINT + "{customerEmailId}", customerEmailId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(VALID_INPUT_REQUIRED_CODE))
                .andExpect(jsonPath("$.reason").value(CUSTOMER_EMAIL_ID_FORMAT_MESSAGE))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getPhoneNumberDetailsForCustomer_returnsErrorResponse_CustomerNotFoundForName() throws Exception {
        given(mockPhoneNumbersService.getPhoneNumbersForCustomer("New User", cpnPageable))
                .willThrow(new ValidInputRequiredException(
                        RESOURCE_NOT_FOUND_CODE, CUSTOMER_NOT_FOUND_MESSAGE));

        mockMvc.perform(get(GET_USER_NUMBER_ENDPOINT + "New User"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(RESOURCE_NOT_FOUND_CODE))
                .andExpect(jsonPath("$.reason").value(CUSTOMER_NOT_FOUND_MESSAGE))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getPhoneNumberDetailsForCustomer_returnsErrorResponse_CustomerNotFoundForEmailId() throws Exception {
        given(mockPhoneNumbersService.getPhoneNumbersForCustomer(
                "new.user@example.com", cpnPageable))
                .willThrow(new ValidInputRequiredException(
                        RESOURCE_NOT_FOUND_CODE, CUSTOMER_NOT_FOUND_MESSAGE));

        mockMvc.perform(get(GET_USER_NUMBER_ENDPOINT + "new.user@example.com"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(RESOURCE_NOT_FOUND_CODE))
                .andExpect(jsonPath("$.reason").value(CUSTOMER_NOT_FOUND_MESSAGE))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @ParameterizedTest
    @ValueSource(strings = { "John Smith", "john.smith@example.com" })
    void getPhoneNumberDetailsForCustomerFullName_returnsPhoneNumbersResponse(String nameOrEmailId) throws Exception {

        when(mockPhoneNumbersService.getPhoneNumbersForCustomer(
                nameOrEmailId, cpnPageable)).thenReturn(cpnResponsePage);

        mockMvc.perform(get(GET_USER_NUMBER_ENDPOINT + nameOrEmailId)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "customerName,asc")
                        .param("sort", "phoneNumber,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()")
                        .value(cpnPageable.getPageSize()))
                .andExpect(jsonPath("$[0].customerName")
                        .value("John Smith"))
                .andExpect(jsonPath("$[0].customerEmailId")
                        .value("john.smith@example.com"))
                .andExpect(jsonPath("$[0].phoneNumbersResponse.length()")
                        .value(2))
                .andExpect(jsonPath("$[0].phoneNumbersResponse[0].phoneNumber")
                        .value("0411111111"))
                .andExpect(jsonPath("$[0].phoneNumbersResponse[0].phoneNumberType")
                        .value("MOBILE"))
                .andExpect(jsonPath("$[0].phoneNumbersResponse[0].phoneNumberStatus")
                        .value("ASSIGNED"))
                .andExpect(jsonPath("$[0].phoneNumbersResponse[0].phoneNumberActive")
                        .value(Boolean.TRUE))
                .andExpect(jsonPath("$[0].phoneNumbersResponse[1].phoneNumber")
                        .value("0422222222"))
                .andExpect(jsonPath("$[0].phoneNumbersResponse[1].phoneNumberType")
                        .value("WORK"))
                .andExpect(jsonPath("$[0].phoneNumbersResponse[1].phoneNumberStatus")
                        .value("ASSIGNED"))
                .andExpect(jsonPath("$[0].phoneNumbersResponse[1].phoneNumberActive")
                        .value(Boolean.TRUE));
    }

    @Test
    void getPhoneNumberDetailsForCustomerPartialName_returnsPhoneNumbersResponse() throws Exception {

        when(mockPhoneNumbersService.getPhoneNumbersForCustomer(
                "John", cpnPageable)).thenReturn(cpnResponsePage);

        mockMvc.perform(get(GET_USER_NUMBER_ENDPOINT + "John")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "customerName,asc")
                        .param("sort", "phoneNumber,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()")
                        .value(cpnPageable.getPageSize()))
                .andExpect(jsonPath("$[0].customerName")
                        .value("John Smith"))
                .andExpect(jsonPath("$[0].customerEmailId")
                        .value("john.smith@example.com"))
                .andExpect(jsonPath("$[0].phoneNumbersResponse.length()")
                        .value(2))
                .andExpect(jsonPath("$[0].phoneNumbersResponse[0].phoneNumber")
                        .value("0411111111"))
                .andExpect(jsonPath("$[0].phoneNumbersResponse[0].phoneNumberType")
                        .value("MOBILE"))
                .andExpect(jsonPath("$[0].phoneNumbersResponse[0].phoneNumberStatus")
                        .value("ASSIGNED"))
                .andExpect(jsonPath("$[0].phoneNumbersResponse[0].phoneNumberActive")
                        .value(Boolean.TRUE))
                .andExpect(jsonPath("$[0].phoneNumbersResponse[1].phoneNumber")
                        .value("0422222222"))
                .andExpect(jsonPath("$[0].phoneNumbersResponse[1].phoneNumberType")
                        .value("WORK"))
                .andExpect(jsonPath("$[0].phoneNumbersResponse[1].phoneNumberStatus")
                        .value("ASSIGNED"))
                .andExpect(jsonPath("$[0].phoneNumbersResponse[1].phoneNumberActive")
                        .value(Boolean.TRUE))
                .andExpect(jsonPath("$[1].customerName")
                        .value("John Williams"))
                .andExpect(jsonPath("$[1].customerEmailId")
                        .value("john.williams@example.com"))
                .andExpect(jsonPath("$[1].phoneNumbersResponse.length()")
                        .value(1))
                .andExpect(jsonPath("$[1].phoneNumbersResponse[0].phoneNumber")
                        .value("0411111114"))
                .andExpect(jsonPath("$[1].phoneNumbersResponse[0].phoneNumberType")
                        .value("WORK"))
                .andExpect(jsonPath("$[1].phoneNumbersResponse[0].phoneNumberStatus")
                        .value("ASSIGNED"))
                .andExpect(jsonPath("$[1].phoneNumbersResponse[0].phoneNumberActive")
                        .value(Boolean.TRUE));
    }

}
