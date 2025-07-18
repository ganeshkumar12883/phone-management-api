package au.com.belong.phone.management.integrationTests.service;

import au.com.belong.phone.management.dto.response.CustomerPhoneNumbersResponse;
import au.com.belong.phone.management.dto.response.PhoneNumbersResponse;
import au.com.belong.phone.management.exception.ValidInputRequiredException;
import au.com.belong.phone.management.model.PhoneNumberStatus;
import au.com.belong.phone.management.model.PhoneNumberType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_EMAIL_ID_FORMAT_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_NAME_FORMAT_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.VALID_CUSTOMER_NAME_EMAIL_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.VALID_INPUT_REQUIRED_CODE;
import static au.com.belong.phone.management.model.PhoneNumberStatus.ASSIGNED;
import static au.com.belong.phone.management.model.PhoneNumberType.MOBILE;
import static au.com.belong.phone.management.model.PhoneNumberType.WORK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class PhoneNumberRetrievalIntegrationTest extends BasePhoneNumbersServiceIntegrationTest {

    @Test
    void testGetAllPhoneNumbers() {
        Pageable pageable = PageRequest.of(0
                , 2
                , Sort.by("phoneNumber")
                        .ascending());
        Page<PhoneNumbersResponse> phoneNumbersPage = phoneNumbersService.getAllPhoneNumbers(pageable);

        assertTrue(phoneNumbersPage.hasContent());
        assertEquals(pageable.getPageSize(), phoneNumbersPage.getContent().size());
        assertEquals(pageable.getPageNumber(), phoneNumbersPage.getNumber());
        assertEquals("0411111111", phoneNumbersPage.getContent().get(0).getPhoneNumber());
        assertEquals(MOBILE.name(), phoneNumbersPage.getContent().get(0).getPhoneNumberType());
        assertEquals(ASSIGNED.name(), phoneNumbersPage.getContent().get(0).getPhoneNumberStatus());
        assertTrue(phoneNumbersPage.getContent().get(0).getPhoneNumberActive());
        assertEquals("0422222222", phoneNumbersPage.getContent().get(1).getPhoneNumber());
        assertEquals(WORK.name(), phoneNumbersPage.getContent().get(1).getPhoneNumberType());
        assertEquals(ASSIGNED.name(), phoneNumbersPage.getContent().get(1).getPhoneNumberStatus());
        assertTrue(phoneNumbersPage.getContent().get(1).getPhoneNumberActive());
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
                MOBILE);
        assertPhoneNumberDetailsExists(phoneNumberResponse,
                2,
                "0422222222",
                WORK);
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
                MOBILE);
        assertPhoneNumberDetailsExists(phoneNumberResponse,
                2,
                "0422222222",
                WORK);
    }

    @Test
    void testGetPhoneNumbersForCustomer_WithPartialName() {
        // Arrange: Add phone numbers and customer details with mapping
        createCustomersAndMapping();

        // Act: Retrieve phone numbers for the customer email id
        List<CustomerPhoneNumbersResponse> customerPhoneNumbersResponseList =
                phoneNumbersService.getPhoneNumbersForCustomer("Jane");

        // Assert: Check list size
        assertEquals(2, customerPhoneNumbersResponseList.size());

        for (CustomerPhoneNumbersResponse customerPhoneNumbersResponse : customerPhoneNumbersResponseList) {
            switch(customerPhoneNumbersResponse.getCustomerName()) {
                case "Jane Doe":
                    assertCustomerDetailsExists(customerPhoneNumbersResponse,
                            "Jane Doe",
                            "jane.doe@example.com");
                    assertPhoneNumberDetailsExists(customerPhoneNumbersResponse,
                            2,
                            "0411111111",
                            MOBILE);
                    assertPhoneNumberDetailsExists(customerPhoneNumbersResponse,
                            2,
                            "0422222222",
                            WORK);
                    break;
                case "Jane Citizen":
                    assertCustomerDetailsExists(customerPhoneNumbersResponse,
                            "Jane Citizen",
                            "jane.citizen@example.com");
                    assertPhoneNumberDetailsExists(customerPhoneNumbersResponse,
                            1,
                            "0433333333",
                            MOBILE);
                    break;
                default:
                    fail("Unexpected customer name: " + customerPhoneNumbersResponse.getCustomerName());
            }
        }
    }

    // Helper methods for asserting the results
    private void assertCustomerDetailsExists(CustomerPhoneNumbersResponse response,
                                    String expectedName,
                                    String expectedEmail) {
        assertEquals(expectedName, response.getCustomerName());
        assertEquals(expectedEmail, response.getCustomerEmailId());
    }

    private void assertPhoneNumberDetailsExists(List<PhoneNumbersResponse> responseList,
                                         int expectedPhoneCount,
                                         String expectedNumber,
                                         PhoneNumberStatus expectedStatus,
                                         PhoneNumberType expectedType) {
        assertEquals(expectedPhoneCount, responseList.size());

        boolean assertionSuccess = false;
        for (PhoneNumbersResponse response : responseList) {
            if (expectedNumber.equals(response.getPhoneNumber())) {
                assertEquals(expectedType.name(), response.getPhoneNumberType());
                assertEquals(expectedStatus.name(), response.getPhoneNumberStatus());
                assertTrue(response.getPhoneNumberActive());
                assertionSuccess = true;
                break;
            }
        }
        if (!assertionSuccess) {
            fail("Phone number not found: " + expectedNumber);
        }
    }
}