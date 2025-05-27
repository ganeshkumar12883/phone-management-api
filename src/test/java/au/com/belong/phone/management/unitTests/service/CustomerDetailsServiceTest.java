package au.com.belong.phone.management.unitTests.service;

import au.com.belong.phone.management.exception.ResourceConflictException;
import au.com.belong.phone.management.model.CustomerDetails;
import au.com.belong.phone.management.repository.CustomerDetailsRepository;
import au.com.belong.phone.management.service.CustomerDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.EMAIL_ALREADY_ASSOCIATED_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_CONFLICT_CODE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Unit Tests for CustomerDetailsService")
@ExtendWith(MockitoExtension.class)
class CustomerDetailsServiceTest {

    private static final String CUSTOMER_EMAIL_ID = "john.smith@example.com";
    private static final String CUSTOMER_NAME = "John Smith";
    private static final String DIFFERENT_CUSTOMER_NAME = "John Red Smith";
    private static final String NEW_CUSTOMER_NAME = "New Customer";
    private static final String NEW_CUSTOMER_EMAIL_ID = "new.customer@example.com";

    @Mock
    private CustomerDetailsRepository customerDetailsRepository;

    @InjectMocks
    private CustomerDetailsService customerDetailsService;

    @Test
    @DisplayName("Returns the existing customer ID when email and name matches")
    void returnsExistingCustomerId_WhenCustomerDetailsByEmailId_EmailAndNameMatches() {
        CustomerDetails existingCustomerDetails = new CustomerDetails.Builder()
                .customerDetailsId(1L)
                .customerEmailId(CUSTOMER_EMAIL_ID)
                .customerName(CUSTOMER_NAME).build();

        when(customerDetailsRepository.findByCustomerEmailIdIgnoreCase(CUSTOMER_EMAIL_ID))
                .thenReturn(Optional.of(existingCustomerDetails));

        Long resultCustomerDetailsId = customerDetailsService.getOrCreateCustomerDetails(
                CUSTOMER_NAME, CUSTOMER_EMAIL_ID);

        assertThat(resultCustomerDetailsId).isEqualTo(1L);
        verify(customerDetailsRepository).findByCustomerEmailIdIgnoreCase(CUSTOMER_EMAIL_ID);
        verify(customerDetailsRepository, never()).findByCustomerNameIgnoreCase(anyString());
        verify(customerDetailsRepository, never()).save(any());
    }

    @Test
    @DisplayName("Throws conflict exception when email exists but associated to a different name")
    void throwsConflictException_WhenCustomerDetailsByEmailId_NameNotMatches() {
        CustomerDetails existingCustomerDetails = new CustomerDetails.Builder()
                .customerDetailsId(1L)
                .customerEmailId(CUSTOMER_EMAIL_ID)
                .customerName(DIFFERENT_CUSTOMER_NAME).build();

        when(customerDetailsRepository.findByCustomerEmailIdIgnoreCase(CUSTOMER_EMAIL_ID))
                .thenReturn(Optional.of(existingCustomerDetails));

        ResourceConflictException exception = assertThrows(ResourceConflictException.class, () ->
                customerDetailsService.getOrCreateCustomerDetails(
                        CUSTOMER_NAME, CUSTOMER_EMAIL_ID));

        assertEquals(RESOURCE_CONFLICT_CODE, exception.getErrorCode());
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals(EMAIL_ALREADY_ASSOCIATED_MESSAGE, exception.getReason());

        verify(customerDetailsRepository).findByCustomerEmailIdIgnoreCase(CUSTOMER_EMAIL_ID);
        verify(customerDetailsRepository, never()).findByCustomerNameIgnoreCase(CUSTOMER_NAME);
        verify(customerDetailsRepository, never()).save(any());
    }

    @Test
    @DisplayName("Returns customer ID after updating email id for the customer when name exists but email is new")
    void returnsCustomerDetailsId_WhenCustomerDetailsByName_NoEmailId() {
        CustomerDetails existingCustomerDetails = new CustomerDetails.Builder()
                .customerDetailsId(1L)
                .customerName(CUSTOMER_NAME).build();

        when(customerDetailsRepository.findByCustomerEmailIdIgnoreCase(CUSTOMER_EMAIL_ID))
                .thenReturn(Optional.empty());
        when(customerDetailsRepository.findByCustomerNameIgnoreCase(CUSTOMER_NAME))
                .thenReturn(Optional.of(existingCustomerDetails));
        when(customerDetailsRepository.save(existingCustomerDetails)).thenAnswer(invocation -> {
            existingCustomerDetails.setCustomerEmailId(CUSTOMER_EMAIL_ID);
            return existingCustomerDetails;
        });

        Long resultCustomerDetailsId = customerDetailsService.getOrCreateCustomerDetails(
                CUSTOMER_NAME, CUSTOMER_EMAIL_ID);

        assertThat(resultCustomerDetailsId).isEqualTo(1L);
        assertEquals(CUSTOMER_EMAIL_ID, existingCustomerDetails.getCustomerEmailId());

        verify(customerDetailsRepository).findByCustomerEmailIdIgnoreCase(CUSTOMER_EMAIL_ID);
        verify(customerDetailsRepository).findByCustomerNameIgnoreCase(CUSTOMER_NAME);
        verify(customerDetailsRepository).save(any(CustomerDetails.class));
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "  " })
    @DisplayName("Returns customer ID after updating email id for the customer when name exists but email is new")
    void returnsCustomerDetailsId_WhenCustomerDetailsByName_NoEmailId(String customerEmailId) {
        CustomerDetails existingCustomerDetails = new CustomerDetails.Builder()
                .customerDetailsId(1L)
                .customerEmailId(customerEmailId)
                .customerName(CUSTOMER_NAME).build();

        when(customerDetailsRepository.findByCustomerEmailIdIgnoreCase(CUSTOMER_EMAIL_ID))
                .thenReturn(Optional.empty());
        when(customerDetailsRepository.findByCustomerNameIgnoreCase(CUSTOMER_NAME))
                .thenReturn(Optional.of(existingCustomerDetails));
        when(customerDetailsRepository.save(existingCustomerDetails)).thenAnswer(invocation -> {
            existingCustomerDetails.setCustomerEmailId(CUSTOMER_EMAIL_ID);
            return existingCustomerDetails;
        });

        Long resultCustomerDetailsId = customerDetailsService.getOrCreateCustomerDetails(
                CUSTOMER_NAME, CUSTOMER_EMAIL_ID);

        assertThat(resultCustomerDetailsId).isEqualTo(1L);
        assertEquals(CUSTOMER_EMAIL_ID, existingCustomerDetails.getCustomerEmailId());

        verify(customerDetailsRepository).findByCustomerEmailIdIgnoreCase(CUSTOMER_EMAIL_ID);
        verify(customerDetailsRepository).findByCustomerNameIgnoreCase(CUSTOMER_NAME);
        verify(customerDetailsRepository).save(any(CustomerDetails.class));
    }

    @Test
    @DisplayName("Creates a new customer when both email and name do not exist")
    void createsNewCustomer_WhenEmailAndNameDoNotMatchAny() {
        when(customerDetailsRepository.findByCustomerEmailIdIgnoreCase(NEW_CUSTOMER_EMAIL_ID))
                .thenReturn(Optional.empty());
        when(customerDetailsRepository.findByCustomerNameIgnoreCase(NEW_CUSTOMER_NAME))
                .thenReturn(Optional.empty());
        when(customerDetailsRepository.save(any(CustomerDetails.class)))
                .thenAnswer(invocation -> {
                    CustomerDetails toSave = invocation.getArgument(0);
                    toSave.setCustomerDetailsId(42L);
                    return toSave;
                });

        Long customerDetailsId = customerDetailsService.getOrCreateCustomerDetails(
                NEW_CUSTOMER_NAME, NEW_CUSTOMER_EMAIL_ID);

        assertThat(customerDetailsId).isNotNull();
        assertThat(customerDetailsId).isEqualTo(42L);

        ArgumentCaptor<CustomerDetails> captor = ArgumentCaptor.forClass(CustomerDetails.class);
        verify(customerDetailsRepository).save(captor.capture());

        CustomerDetails savedCustomer = captor.getValue();
        assertEquals(NEW_CUSTOMER_EMAIL_ID, savedCustomer.getCustomerEmailId());
        assertEquals(NEW_CUSTOMER_NAME, savedCustomer.getCustomerName());

        verify(customerDetailsRepository).findByCustomerEmailIdIgnoreCase(NEW_CUSTOMER_EMAIL_ID);
        verify(customerDetailsRepository).findByCustomerNameIgnoreCase(NEW_CUSTOMER_NAME);
    }
}