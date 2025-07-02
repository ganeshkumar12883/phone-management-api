package au.com.belong.phone.management.unitTests.controller;

import au.com.belong.phone.management.controller.PhoneNumbersController;
import au.com.belong.phone.management.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_NOT_FOUND_CODE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhoneNumbersController.class)
class PhoneNumbersControllerGetAllPhoneNumbersTest extends BasePhoneNumbersControllerTest {

    @Test
    void getAllPhoneNumbers_returnsAllPhoneNumbers() throws Exception {

        when(mockPhoneNumbersService.getAllPhoneNumbers(pnPageable)).thenReturn(pnResponsePage);

        mockMvc.perform(get(BASE_ENDPOINT)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "phoneNumber,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(pnPageable.getPageSize()))
                .andExpect(jsonPath("$[0].phoneNumber").value("0411111111"))
                .andExpect(jsonPath("$[0].phoneNumberType").value("MOBILE"))
                .andExpect(jsonPath("$[1].phoneNumber").value("0422222222"))
                .andExpect(jsonPath("$[1].phoneNumberType").value("WORK"));
    }

    @Test
    void getAllPhoneNumbers_ErrorResponse_OnNoPhoneNumbers() throws Exception {
        given(mockPhoneNumbersService.getAllPhoneNumbers(pnPageable))
                .willThrow(new ResourceNotFoundException(
                        RESOURCE_NOT_FOUND_CODE, PHONE_NUMBER_NOT_FOUND_MESSAGE));

        mockMvc.perform(get(BASE_ENDPOINT))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(RESOURCE_NOT_FOUND_CODE))
                .andExpect(jsonPath("$.reason").value(PHONE_NUMBER_NOT_FOUND_MESSAGE))
                .andExpect(jsonPath("$.timestamp").exists());
    }

}
