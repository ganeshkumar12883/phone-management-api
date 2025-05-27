package au.com.belong.phone.management.service;

import au.com.belong.phone.management.dto.PhoneActivationRequest;
import au.com.belong.phone.management.dto.PhoneNumbersResponse;
import au.com.belong.phone.management.model.PhoneNumberDetails;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneNumbersServiceImpl implements PhoneNumbersService {

    private final PhoneNumberActivationService phoneNumberActivationService;
    private final CustomerDetailsService customerDetailsService;
    private final CustomerPhoneMappingService cpMappingService;
    private final PhoneNumberQueryService phoneNumberQueryService;

    public PhoneNumbersServiceImpl(PhoneNumberActivationService phoneNumberActivationService,
                                   CustomerDetailsService customerDetailsService,
                                   CustomerPhoneMappingService cpMappingService,
                                   PhoneNumberQueryService phoneNumberQueryService) {
        this.phoneNumberActivationService = phoneNumberActivationService;
        this.customerDetailsService = customerDetailsService;
        this.cpMappingService = cpMappingService;
        this.phoneNumberQueryService = phoneNumberQueryService;
    }

    @Override
    public List<PhoneNumberDetails> getAllPhoneNumbers() {
        return phoneNumberQueryService.getAllPhoneNumbers();
    }

    @Override
    public List<PhoneNumbersResponse> getPhoneNumbersForCustomer(String customerNameOrEmailId) {
        return phoneNumberQueryService.getPhoneNumbersForCustomer(customerNameOrEmailId);
    }

    @Override
    @Transactional
    public void activatePhoneNumberForCustomer(PhoneActivationRequest phoneActivationRequest) {
        // Step 1: Check if phone Number exist and available and update the details
        Long phoneNumberId = phoneNumberActivationService.activatePhoneNumber(
                phoneActivationRequest.getPhoneNumber(), phoneActivationRequest.getPhoneNumberType());

        // Step 2: Customer can be an existing or new. So fetch or create the customer with name
        Long customerId = customerDetailsService.getOrCreateCustomerDetails(
                phoneActivationRequest.getCustomerName(),
                phoneActivationRequest.getCustomerEmailId());

        // Step 3: For the updated phone number and created/fetched customer, create a new mapping
        cpMappingService.createCustomerAndPhoneMappingIfNotExists(
                customerId, phoneNumberId);
    }
}
