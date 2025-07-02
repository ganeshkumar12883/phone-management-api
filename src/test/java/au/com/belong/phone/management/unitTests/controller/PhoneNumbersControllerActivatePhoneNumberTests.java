package au.com.belong.phone.management.unitTests.controller;

import au.com.belong.phone.management.controller.PhoneNumbersController;
import au.com.belong.phone.management.dto.request.PhoneActivationRequest;
import au.com.belong.phone.management.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.EMAIL_ALREADY_ASSOCIATED_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_ASSIGNED_ACTIVE_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_ASSIGNED_INACTIVE_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_CONFLICT_CODE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_NOT_FOUND_CODE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhoneNumbersController.class)
public class PhoneNumbersControllerActivatePhoneNumberTests extends BasePhoneNumbersControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    private PhoneActivationRequest validRequest() {
        return new PhoneActivationRequest(
                "0422222222",
                "MOBILE",
                "John Smith",
                "john.smith@example.com"
        );
    }

    @Test
    void shouldReturnNotFound_whenPhoneNumberDoesNotExist() throws Exception {
        doThrow(new ResourceNotFoundException(RESOURCE_NOT_FOUND_CODE, PHONE_NUMBER_NOT_FOUND_MESSAGE))
                .when(mockPhoneNumbersService).activatePhoneNumberForCustomer(any());

        mockMvc.perform(post(ACTIVATE_ENDPOINT).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(RESOURCE_NOT_FOUND_CODE))
                .andExpect(jsonPath("$.reason").value(PHONE_NUMBER_NOT_FOUND_MESSAGE))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnNotFound_whenPhoneNumberAlreadyAssignedActive() throws Exception {
        doThrow(new ResourceNotFoundException(RESOURCE_CONFLICT_CODE, PHONE_NUMBER_ASSIGNED_ACTIVE_MESSAGE))
                .when(mockPhoneNumbersService).activatePhoneNumberForCustomer(any());

        mockMvc.perform(post(ACTIVATE_ENDPOINT).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(RESOURCE_CONFLICT_CODE))
                .andExpect(jsonPath("$.reason").value(PHONE_NUMBER_ASSIGNED_ACTIVE_MESSAGE))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnNotFound_whenPhoneNumberAlreadyAssignedInActive() throws Exception {
        doThrow(new ResourceNotFoundException(RESOURCE_CONFLICT_CODE, PHONE_NUMBER_ASSIGNED_INACTIVE_MESSAGE))
                .when(mockPhoneNumbersService).activatePhoneNumberForCustomer(any());

        mockMvc.perform(post(ACTIVATE_ENDPOINT).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(RESOURCE_CONFLICT_CODE))
                .andExpect(jsonPath("$.reason").value(PHONE_NUMBER_ASSIGNED_INACTIVE_MESSAGE))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnNotFound_whenCustomerEmailAlreadyAssociated() throws Exception {
        doThrow(new ResourceNotFoundException(RESOURCE_CONFLICT_CODE, EMAIL_ALREADY_ASSOCIATED_MESSAGE))
                .when(mockPhoneNumbersService).activatePhoneNumberForCustomer(any());

        mockMvc.perform(post(ACTIVATE_ENDPOINT).
                        contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(RESOURCE_CONFLICT_CODE))
                .andExpect(jsonPath("$.reason").value(EMAIL_ALREADY_ASSOCIATED_MESSAGE))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnCreated_whenPhoneNumberIsActivated() throws Exception {
        mockMvc.perform(post(ACTIVATE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isCreated());
    }
}
