package au.com.belong.phone.management.integrationTests.service;

import au.com.belong.phone.management.dto.PhoneNumbersResponse;
import au.com.belong.phone.management.exception.ValidInputRequiredException;
import au.com.belong.phone.management.model.PhoneNumberDetails;
import au.com.belong.phone.management.model.PhoneNumberType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import java.util.List;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_EMAIL_ID_FORMAT_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_NAME_FORMAT_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.VALID_CUSTOMER_NAME_EMAIL_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.VALID_INPUT_REQUIRED_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class PhoneNumberRetrievalIntegrationTest extends BasePhoneNumbersServiceIntegrationTest {

    @Test
    void testGetAllPhoneNumbers() {
        List<PhoneNumberDetails> phoneNumbers = phoneNumbersService.getAllPhoneNumbers();
        assertEquals(9, phoneNumbers.size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"  " })
    @NullAndEmptySource
    void testGetPhoneNumbersForCustomer_WithInvalidInputs(String invalidInput) {
        ValidInputRequiredException exception = assertThrows(ValidInputRequiredException.class,
                () -> phoneNumbersService.getPhoneNumbersForCustomer(invalidInput));

        assertEquals(VALID_INPUT_REQUIRED_CODE, exception.getErrorCode());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(VALID_CUSTOMER_NAME_EMAIL_MESSAGE, exception.getReason());
    }

    @Test
    void testGetPhoneNumbersForCustomer_WithInvalidFormatName() {
        ValidInputRequiredException exception = assertThrows(ValidInputRequiredException.class, () ->
            phoneNumbersService.getPhoneNumbersForCustomer("12ACTOR"));

        assertEquals(VALID_INPUT_REQUIRED_CODE, exception.getErrorCode());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(CUSTOMER_NAME_FORMAT_MESSAGE, exception.getReason());
    }

    @ParameterizedTest
    @ValueSource(strings = { ".com@tempid", "tempId@com", "tempId@@@@com" })
    void testGetPhoneNumbersForCustomer_WithInvalidEmailFormats(String invalidEmail) {
        ValidInputRequiredException exception = assertThrows(ValidInputRequiredException.class,
                () -> phoneNumbersService.getPhoneNumbersForCustomer(invalidEmail));

        assertEquals(VALID_INPUT_REQUIRED_CODE, exception.getErrorCode());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(CUSTOMER_EMAIL_ID_FORMAT_MESSAGE, exception.getReason());
    }

    @Test
    void testGetPhoneNumbersForCustomer_WithCustomerEmailId() {
        // Arrange: Add phone numbers and customer details with mapping
        createCustomersAndMapping();

        // Act: Retrieve phone numbers for the customer email id
        List<PhoneNumbersResponse> phoneNumbersResponseList =
                phoneNumbersService.getPhoneNumbersForCustomer("jane.doe@example.com");

        // Assert: Check list size
        assertEquals(1, phoneNumbersResponseList.size());

        PhoneNumbersResponse phoneNumberResponse = phoneNumbersResponseList.get(0);

        assertCustomerDetailsExists(phoneNumberResponse,
                "Jane Doe",
                "jane.doe@example.com");
        assertPhoneNumberDetailsExists(phoneNumberResponse,
                2,
                "0411111111",
                PhoneNumberType.MOBILE);
        assertPhoneNumberDetailsExists(phoneNumberResponse,
                2,
                "0422222222",
                PhoneNumberType.WORK);
    }

    @Test
    void testGetPhoneNumbersForCustomer_WithCustomerName() {
        // Arrange: Add phone numbers and customer details with mapping
        createCustomersAndMapping();

        // Act: Retrieve phone numbers for the customer email id
        List<PhoneNumbersResponse> phoneNumbersResponseList =
                phoneNumbersService.getPhoneNumbersForCustomer("Jane Doe");

        // Assert: Check list size
        assertEquals(1, phoneNumbersResponseList.size());

        PhoneNumbersResponse phoneNumberResponse = phoneNumbersResponseList.get(0);

        assertCustomerDetailsExists(phoneNumberResponse,
                "Jane Doe",
                "jane.doe@example.com");
        assertPhoneNumberDetailsExists(phoneNumberResponse,
                2,
                "0411111111",
                PhoneNumberType.MOBILE);
        assertPhoneNumberDetailsExists(phoneNumberResponse,
                2,
                "0422222222",
                PhoneNumberType.WORK);
    }

    @Test
    void testGetPhoneNumbersForCustomer_WithPartialName() {
        // Arrange: Add phone numbers and customer details with mapping
        createCustomersAndMapping();

        // Act: Retrieve phone numbers for the customer email id
        List<PhoneNumbersResponse> phoneNumbersResponseList =
                phoneNumbersService.getPhoneNumbersForCustomer("Jane");

        // Assert: Check list size
        assertEquals(2, phoneNumbersResponseList.size());

        for (PhoneNumbersResponse phoneNumberResponse : phoneNumbersResponseList) {
            switch(phoneNumberResponse.getCustomerName()) {
                case "Jane Doe":
                    assertCustomerDetailsExists(phoneNumberResponse,
                            "Jane Doe",
                            "jane.doe@example.com");
                    assertPhoneNumberDetailsExists(phoneNumberResponse,
                            2,
                            "0411111111",
                            PhoneNumberType.MOBILE);
                    assertPhoneNumberDetailsExists(phoneNumberResponse,
                            2,
                            "0422222222",
                            PhoneNumberType.WORK);
                    break;
                case "Jane Citizen":
                    assertCustomerDetailsExists(phoneNumberResponse,
                            "Jane Citizen",
                            "jane.citizen@example.com");
                    assertPhoneNumberDetailsExists(phoneNumberResponse,
                            1,
                            "0433333333",
                            PhoneNumberType.MOBILE);
                    break;
                default:
                    fail("Unexpected customer name: " + phoneNumberResponse.getCustomerName());
            }
        }
    }

    // Helper methods for asserting the results
    private void assertCustomerDetailsExists(PhoneNumbersResponse response,
                                    String expectedName,
                                    String expectedEmail) {
        assertEquals(expectedName, response.getCustomerName());
        assertEquals(expectedEmail, response.getCustomerEmailId());
    }

    private void assertPhoneNumberDetailsExists(PhoneNumbersResponse response,
                                         int expectedPhoneCount,
                                         String expectedNumber,
                                         PhoneNumberType expectedType) {
        assertNotNull(response.getPhoneNumberDetails());
        assertEquals(expectedPhoneCount, response.getPhoneNumberDetails().size());

        boolean assertionSuccess = false;
        for (PhoneNumberDetails phoneNumberDetails : response.getPhoneNumberDetails()) {
            if (expectedNumber.equals(phoneNumberDetails.getPhoneNumber())) {
                assertEquals(expectedType, phoneNumberDetails.getPhoneNumberType());
                assertTrue(phoneNumberDetails.isActive());
                assertionSuccess = true;
                break;
            }
        }
        if (!assertionSuccess) {
            fail("Phone number not found: " + expectedNumber);
        }
    }
}