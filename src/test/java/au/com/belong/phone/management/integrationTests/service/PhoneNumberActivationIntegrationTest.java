package au.com.belong.phone.management.integrationTests.service;

import au.com.belong.phone.management.dto.request.PhoneActivationRequest;
import au.com.belong.phone.management.dto.response.PhoneNumbersResponse;
import au.com.belong.phone.management.exception.ResourceConflictException;
import au.com.belong.phone.management.exception.ResourceNotFoundException;
import au.com.belong.phone.management.model.PhoneNumberDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;

import java.util.List;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_ASSIGNED_ACTIVE_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_ASSIGNED_INACTIVE_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_CONFLICT_CODE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_NOT_FOUND_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class PhoneNumberActivationIntegrationTest extends BasePhoneNumbersServiceIntegrationTest {

    private enum ErrorType {
        NOT_FOUND,
        ASSIGNED_ACTIVE,
        ASSIGNED_INACTIVE
    }

    /*
    Test cases for validation of the availability of resources - phone number, customer and mapping
     */
    @DisplayName("Should Throw relevant exception - RESOURCE_NOT_FOUND_CODE" +
            " when phone number provided is not found and RESOURCE_CONFLICT_CODE" +
            " when phone number provided is already assigned active/inactive")
    @ParameterizedTest
    @CsvSource({
            "'0423456789', NOT_FOUND",
            "'0422222222', ASSIGNED_ACTIVE",
            "'0444444444', ASSIGNED_INACTIVE"
    })
    void testActivatePhoneNumberForCustomer_WithConflictingPhoneNumbers(
            String conflictPhoneNumber, ErrorType errorType) {
        PhoneActivationRequest phoneActivationRequest = new PhoneActivationRequest(
                conflictPhoneNumber,
                "MOBILE",
                "John Smith",
                "john.smith@example.com");

        switch (errorType) {
            case NOT_FOUND:
                ResourceNotFoundException nfException = assertThrows(ResourceNotFoundException.class, ()->
                        phoneNumbersService.activatePhoneNumberForCustomer(phoneActivationRequest));

                assertEquals(HttpStatus.NOT_FOUND, nfException.getStatusCode());
                assertEquals(RESOURCE_NOT_FOUND_CODE, nfException.getErrorCode());
                assertEquals(PHONE_NUMBER_NOT_FOUND_MESSAGE, nfException.getReason());
                break;

            case ASSIGNED_ACTIVE:
                ResourceConflictException aaException = assertThrows(ResourceConflictException.class, ()->
                        phoneNumbersService.activatePhoneNumberForCustomer(phoneActivationRequest));

                assertEquals(HttpStatus.CONFLICT, aaException.getStatusCode());
                assertEquals(RESOURCE_CONFLICT_CODE, aaException.getErrorCode());
                assertEquals(PHONE_NUMBER_ASSIGNED_ACTIVE_MESSAGE, aaException.getReason());
                break;

            case ASSIGNED_INACTIVE:
                ResourceConflictException aiException = assertThrows(ResourceConflictException.class, ()->
                        phoneNumbersService.activatePhoneNumberForCustomer(phoneActivationRequest));

                assertEquals(HttpStatus.CONFLICT, aiException.getStatusCode());
                assertEquals(RESOURCE_CONFLICT_CODE, aiException.getErrorCode());
                assertEquals(PHONE_NUMBER_ASSIGNED_INACTIVE_MESSAGE, aiException.getReason());
                break;

            default:
                fail("Unexpected errorType: " + errorType);
        }
    }

    @Test
    @DisplayName("Should activate phone number successfully for valid request")
    void testActivatePhoneNumberForCustomer_ValidRequestExistingCustomer() {
        PhoneActivationRequest phoneActivationRequest = new PhoneActivationRequest(
                "0455555555",
                "MOBILE",
                "John Smith",
                "john.smith@example.com");

        // Act
        phoneNumbersService.activatePhoneNumberForCustomer(phoneActivationRequest);

        // Assert
        List<PhoneNumbersResponse> phoneNumbersResponses =
                phoneNumbersService.getPhoneNumbersForCustomer("John Smith");

        assertActivatedPhoneNumberResponse(phoneActivationRequest, phoneNumbersResponses.get(0));
    }

    @Test
    @DisplayName("Should activate phone number successfully for valid request")
    void testActivatePhoneNumberForCustomer_ValidRequestNewCustomer() {
        PhoneActivationRequest phoneActivationRequest = new PhoneActivationRequest(
                "0466666666",
                "MOBILE",
                "New User",
                "new.user@example.com");

        // Act
        phoneNumbersService.activatePhoneNumberForCustomer(phoneActivationRequest);

        // Assert
        List<PhoneNumbersResponse> phoneNumbersResponses =
                phoneNumbersService.getPhoneNumbersForCustomer("New User");

        assertActivatedPhoneNumberResponse(phoneActivationRequest, phoneNumbersResponses.get(0));
    }

    private void assertActivatedPhoneNumberResponse(PhoneActivationRequest phoneActivationRequest,
                                                    PhoneNumbersResponse phoneNumbersResponse) {
        assertEquals(phoneActivationRequest.getCustomerName(), phoneNumbersResponse.getCustomerName());
        assertEquals(phoneActivationRequest.getCustomerEmailId(), phoneNumbersResponse.getCustomerEmailId());

        List<PhoneNumberDetails> phoneNumberDetailsList = phoneNumbersResponse.getPhoneNumberDetails();
        PhoneNumberDetails phoneNumberDetails = phoneNumberDetailsList.get(0);

        assertEquals(phoneActivationRequest.getPhoneNumber(), phoneNumberDetails.getPhoneNumber());
        assertEquals(phoneActivationRequest.getPhoneNumberType(), phoneNumberDetails.getPhoneNumberType().name());
        assertEquals("ASSIGNED", phoneNumberDetails.getPhoneNumberStatus().name());
        assertTrue(phoneNumberDetails.isActive());
    }
}