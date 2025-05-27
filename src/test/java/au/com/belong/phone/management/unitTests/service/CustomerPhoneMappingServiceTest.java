package au.com.belong.phone.management.unitTests.service;

import au.com.belong.phone.management.exception.ResourceNotFoundException;
import au.com.belong.phone.management.model.CustomerDetails;
import au.com.belong.phone.management.model.CustomerPhoneNumberMapping;
import au.com.belong.phone.management.model.PhoneNumberDetails;
import au.com.belong.phone.management.model.PhoneNumberStatus;
import au.com.belong.phone.management.model.PhoneNumberType;
import au.com.belong.phone.management.repository.CustomerDetailsRepository;
import au.com.belong.phone.management.repository.CustomerPhoneNumberMappingRepository;
import au.com.belong.phone.management.repository.PhoneNumberDetailsRepository;
import au.com.belong.phone.management.service.CustomerPhoneMappingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_NOT_FOUND_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerPhoneMappingServiceTest {

    @Mock
    private CustomerPhoneNumberMappingRepository mockMappingRepository;

    @Mock
    private CustomerDetailsRepository mockCustomerDetailsRepository;

    @Mock
    private PhoneNumberDetailsRepository mockPhoneNumberDetailsRepository;

    @InjectMocks
    private CustomerPhoneMappingService mappingService;

    @Captor
    private ArgumentCaptor<CustomerPhoneNumberMapping> mappingCaptor;

    private CustomerDetails mockCustomerDetails;

    private PhoneNumberDetails mockPhoneNumberDetails;

    private void initializeValidCustomerDetails() {
        mockCustomerDetails = new CustomerDetails.Builder()
                .customerDetailsId(1L)
                .customerName("Test User")
                .customerEmailId("test.user@example.com")
                .build();
    }

    private void initializeValidCustomerAndPhoneNumberDetails() {
        initializeValidCustomerDetails();

        mockPhoneNumberDetails = new PhoneNumberDetails.Builder()
                .phoneNumberDetailsId(2L)
                .phoneNumber("0412345678")
                .phoneNumberType(PhoneNumberType.MOBILE)
                .phoneNumberStatus(PhoneNumberStatus.ASSIGNED)
                .active(true)
                .build();
    }

    @Test
    void shouldNotDoAnything_WhenMappingAlreadyExists() {
        when(mockMappingRepository.customerPhoneNumberMappingExists(anyLong(), anyLong())).thenReturn(true);

        mappingService.createCustomerAndPhoneMappingIfNotExists(1L, 2L);

        verify(mockMappingRepository).customerPhoneNumberMappingExists(anyLong(), anyLong());
        verify(mockCustomerDetailsRepository, never()).findById(anyLong());
        verify(mockPhoneNumberDetailsRepository, never()).findById(anyLong());
        verify(mockMappingRepository, never()).save(any());
    }

    @Test
    void throwException_WhenMappingNotExists_CustomerDetailsNotFound() {
        when(mockMappingRepository.customerPhoneNumberMappingExists(anyLong(), anyLong())).thenReturn(false);
        when(mockCustomerDetailsRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                mappingService.createCustomerAndPhoneMappingIfNotExists(
                        1L, 2L));

        assertEquals(RESOURCE_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(CUSTOMER_NOT_FOUND_MESSAGE, exception.getReason());

        verify(mockMappingRepository).customerPhoneNumberMappingExists(anyLong(), anyLong());
        verify(mockCustomerDetailsRepository).findById(anyLong());
        verify(mockPhoneNumberDetailsRepository, never()).findById(anyLong());
        verify(mockMappingRepository, never()).save(any());
    }

    @Test
    void throwException_WhenMappingNotExists_PhoneNumberDetailsNotFound() {
        initializeValidCustomerDetails();

        when(mockMappingRepository.customerPhoneNumberMappingExists(anyLong(), anyLong())).thenReturn(false);
        when(mockCustomerDetailsRepository.findById(anyLong())).thenReturn(Optional.of(mockCustomerDetails));
        when(mockPhoneNumberDetailsRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                mappingService.createCustomerAndPhoneMappingIfNotExists(
                        1L, 2L));

        assertEquals(RESOURCE_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(PHONE_NUMBER_NOT_FOUND_MESSAGE, exception.getReason());

        verify(mockMappingRepository).customerPhoneNumberMappingExists(anyLong(), anyLong());
        verify(mockCustomerDetailsRepository).findById(anyLong());
        verify(mockPhoneNumberDetailsRepository).findById(anyLong());
        verify(mockMappingRepository, never()).save(any());
    }

    @Test
    void createMapping_WhenMappingNotExists_Customer_PhoneNumber_DetailsFound() {
        initializeValidCustomerAndPhoneNumberDetails();

        when(mockMappingRepository.customerPhoneNumberMappingExists(anyLong(), anyLong())).thenReturn(false);
        when(mockCustomerDetailsRepository.findById(anyLong())).thenReturn(Optional.of(mockCustomerDetails));
        when(mockPhoneNumberDetailsRepository.findById(anyLong())).thenReturn(Optional.of(mockPhoneNumberDetails));

        mappingService.createCustomerAndPhoneMappingIfNotExists(
                1L, 2L);

        verify(mockMappingRepository).customerPhoneNumberMappingExists(anyLong(), anyLong());
        verify(mockCustomerDetailsRepository).findById(anyLong());
        verify(mockPhoneNumberDetailsRepository).findById(anyLong());
        verify(mockMappingRepository).save(mappingCaptor.capture());

        CustomerPhoneNumberMapping savedMapping = mappingCaptor.getValue();
        assertEquals(mockCustomerDetails, savedMapping.getCustomerDetails());
        assertEquals(mockPhoneNumberDetails, savedMapping.getPhoneNumberDetails());
    }
}
