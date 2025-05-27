package au.com.belong.phone.management.unitTests.service;

import au.com.belong.phone.management.dto.PhoneNumbersResponse;
import au.com.belong.phone.management.exception.ResourceNotFoundException;
import au.com.belong.phone.management.model.CustomerDetails;
import au.com.belong.phone.management.model.PhoneNumberDetails;
import au.com.belong.phone.management.model.PhoneNumberStatus;
import au.com.belong.phone.management.model.PhoneNumberType;
import au.com.belong.phone.management.repository.CustomerDetailsRepository;
import au.com.belong.phone.management.repository.PhoneNumberDetailsRepository;
import au.com.belong.phone.management.service.PhoneNumberQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_NOT_FOUND_FOR_CUSTOMER_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_NOT_FOUND_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhoneNumberQueryServiceTest {

    @Mock
    private PhoneNumberDetailsRepository mockPhoneNumberDetailsRepository;

    @Mock
    private CustomerDetailsRepository mockCustomerDetailsRepository;

    @InjectMocks
    private PhoneNumberQueryService phoneNumberQueryService;

    private CustomerDetails mockCustomerDetails;

    private List<PhoneNumberDetails> mockPhoneNumberDetailsList;

    @BeforeEach
    void setup() {
        mockCustomerDetails = new CustomerDetails.Builder()
                .customerDetailsId(1L)
                .customerName("Test User")
                .customerEmailId("test.user@example.com")
                .build();

        PhoneNumberDetails phoneNumberDetails1 = new PhoneNumberDetails.Builder()
                .phoneNumber("0412345678")
                .phoneNumberType(PhoneNumberType.MOBILE)
                .phoneNumberStatus(PhoneNumberStatus.ASSIGNED)
                .active(true)
                .build();

        PhoneNumberDetails phoneNumberDetails2 = new PhoneNumberDetails.Builder()
                .phoneNumber("0487654321")
                .phoneNumberType(PhoneNumberType.MOBILE)
                .phoneNumberStatus(PhoneNumberStatus.ASSIGNED)
                .active(true)
                .build();

        mockPhoneNumberDetailsList = List.of(phoneNumberDetails1, phoneNumberDetails2);
    }

    @DisplayName("Test the getAllPhoneNumbers to throw exception when no phone numbers")
    @Test
    void getAllPhoneNumbers_ThrowsException_WhenNoPhoneNumbersFound() {
        // Arrange
        when (mockPhoneNumberDetailsRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> phoneNumberQueryService.getAllPhoneNumbers());

        assertEquals(RESOURCE_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(PHONE_NUMBER_NOT_FOUND_MESSAGE, exception.getReason());
    }

    @DisplayName("Test the getAllPhoneNumbers to return result when found")
    @Test
    void getAllPhoneNumbers_ShouldReturnAllPhoneNumbersPresent_ValidInput() {
        when (mockPhoneNumberDetailsRepository.findAll()).thenReturn(mockPhoneNumberDetailsList);

        List<PhoneNumberDetails> resultPhoneNumberDetails = phoneNumberQueryService.getAllPhoneNumbers();

        assertEquals(2, resultPhoneNumberDetails.size());

        verify(mockPhoneNumberDetailsRepository).findAll();
    }

    @DisplayName("Test the getPhoneNumbersForCustomer to throw exception when customer not found for name")
    @Test
    void getPhoneNumbersForCustomer_ThrowsException_WhenCustomerNotFoundForName() {
        when (mockCustomerDetailsRepository.findByCustomerNameContainingIgnoreCase(
                anyString())).thenReturn(new ArrayList<>());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                phoneNumberQueryService.getPhoneNumbersForCustomer("Thomas Train"));

        assertEquals(RESOURCE_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(CUSTOMER_NOT_FOUND_MESSAGE, exception.getReason());

        verify(mockCustomerDetailsRepository).findByCustomerNameContainingIgnoreCase(anyString());
    }

    @DisplayName("Test the getPhoneNumbersForCustomer to throw exception when customer not found for email id")
    @Test
    void getPhoneNumbersForCustomer_ThrowsExceptionWhenCustomerNotFoundForEmailId() {
        when (mockCustomerDetailsRepository.findByCustomerEmailIdIgnoreCase(
                anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                phoneNumberQueryService.getPhoneNumbersForCustomer("thomas.train@rails.com"));

        assertEquals(RESOURCE_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(CUSTOMER_NOT_FOUND_MESSAGE, exception.getReason());

        verify(mockCustomerDetailsRepository).findByCustomerEmailIdIgnoreCase(anyString());
    }

    @DisplayName("Test the getPhoneNumbersForCustomer to return values with customer full or partial name")
    @ParameterizedTest
    @ValueSource(strings = { "Test User", "Test" })
    void getPhoneNumbersForCustomer_ReturnsPhoneNumberDetails_WhenCustomerFoundForName(String customerName) {
        when (mockCustomerDetailsRepository.findByCustomerNameContainingIgnoreCase(
                anyString())).thenReturn(List.of(mockCustomerDetails));
        when (mockPhoneNumberDetailsRepository.findPhoneNumbersByCustomerId(
                anyLong())).thenReturn(mockPhoneNumberDetailsList);

        List<PhoneNumbersResponse> resultResponseList =
                phoneNumberQueryService.getPhoneNumbersForCustomer(customerName);

        assertEquals(1, resultResponseList.size());
        PhoneNumbersResponse resultResponse = resultResponseList.get(0);
        assertEquals("Test User", resultResponse.getCustomerName());
        assertEquals("test.user@example.com", resultResponse.getCustomerEmailId());
        assertEquals(2, resultResponse.getPhoneNumberDetails().size());

        verify(mockCustomerDetailsRepository).findByCustomerNameContainingIgnoreCase(anyString());
        verify(mockPhoneNumberDetailsRepository).findPhoneNumbersByCustomerId(anyLong());
    }

    @DisplayName("Test the getPhoneNumbersForCustomer to return values with customers found for partial name")
    @Test
    void getPhoneNumbersForCustomer_ReturnsPhoneNumberDetails_WhenMultipleCustomerFoundForName() {
        CustomerDetails customerDetails = new CustomerDetails.Builder()
                .customerDetailsId(2L)
                .customerName("Test Admin")
                .customerEmailId("test.admin@example.com")
                .build();

        when (mockCustomerDetailsRepository.findByCustomerNameContainingIgnoreCase(
                anyString())).thenReturn(List.of(mockCustomerDetails, customerDetails));
        when (mockPhoneNumberDetailsRepository.findPhoneNumbersByCustomerId(
                anyLong())).thenReturn(mockPhoneNumberDetailsList);

        List<PhoneNumbersResponse> resultResponseList =
                phoneNumberQueryService.getPhoneNumbersForCustomer("Test");

        assertEquals(2, resultResponseList.size());

        boolean testUserFound = false;
        boolean testAdminFound = false;
        for (PhoneNumbersResponse resultResponse : resultResponseList) {
            if (resultResponse.getCustomerName().equals(mockCustomerDetails.getCustomerName())) {
                testUserFound = true;
                assertEquals("test.user@example.com", resultResponse.getCustomerEmailId());
            } else if (resultResponse.getCustomerName().equals(customerDetails.getCustomerName())) {
                testAdminFound = true;
                assertEquals("test.admin@example.com", resultResponse.getCustomerEmailId());
            } else {
                fail("No valid Customer found");
            }
            assertEquals(2, resultResponse.getPhoneNumberDetails().size());
        }
        assertTrue(testUserFound);
        assertTrue(testAdminFound);

        verify(mockCustomerDetailsRepository).findByCustomerNameContainingIgnoreCase(anyString());
        verify(mockPhoneNumberDetailsRepository, times(2)).
                findPhoneNumbersByCustomerId(anyLong());
    }

    @DisplayName("Test the getPhoneNumbersForCustomer to return values with customer email id")
    @Test
    void getPhoneNumbersForCustomer_ReturnsPhoneNumberDetails_WhenCustomerFoundForEmailId() {
        when (mockCustomerDetailsRepository.findByCustomerEmailIdIgnoreCase(
                anyString())).thenReturn(Optional.of(mockCustomerDetails));
        when (mockPhoneNumberDetailsRepository.findPhoneNumbersByCustomerId(
                anyLong())).thenReturn(mockPhoneNumberDetailsList);

        List<PhoneNumbersResponse> resultResponseList =
                phoneNumberQueryService.getPhoneNumbersForCustomer("thomas.train@rails.com");

        assertEquals(1, resultResponseList.size());
        PhoneNumbersResponse resultResponse = resultResponseList.get(0);
        assertEquals("Test User", resultResponse.getCustomerName());
        assertEquals("test.user@example.com", resultResponse.getCustomerEmailId());
        assertEquals(2, resultResponse.getPhoneNumberDetails().size());

        verify(mockCustomerDetailsRepository).findByCustomerEmailIdIgnoreCase(anyString());
        verify(mockPhoneNumberDetailsRepository).findPhoneNumbersByCustomerId(anyLong());
    }

    @Test
    @DisplayName("Should return empty phone number list if customer has no assigned numbers")
    void getPhoneNumbersForCustomer_ReturnsEmptyList_WhenCustomerHasNoPhoneNumbers() {
        when(mockCustomerDetailsRepository.findByCustomerEmailIdIgnoreCase(anyString()))
                .thenReturn(Optional.of(mockCustomerDetails));
        when(mockPhoneNumberDetailsRepository.findPhoneNumbersByCustomerId(anyLong()))
                .thenReturn(List.of()); // No associated phone numbers

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                phoneNumberQueryService.getPhoneNumbersForCustomer("test.user@example.com"));

        assertEquals(RESOURCE_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(PHONE_NUMBER_NOT_FOUND_FOR_CUSTOMER_MESSAGE, exception.getReason());

        verify(mockCustomerDetailsRepository).findByCustomerEmailIdIgnoreCase(anyString());
        verify(mockPhoneNumberDetailsRepository).findPhoneNumbersByCustomerId(anyLong());
    }
}
