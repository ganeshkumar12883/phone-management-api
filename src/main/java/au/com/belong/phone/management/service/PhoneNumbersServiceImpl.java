package au.com.belong.phone.management.service;

import au.com.belong.phone.management.dto.request.PhoneActivationRequest;
import au.com.belong.phone.management.dto.response.CustomerPhoneNumbersResponse;
import au.com.belong.phone.management.dto.response.PhoneNumbersResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<PhoneNumbersResponse> getAllPhoneNumbers(Pageable pageable) {
        return phoneNumberQueryService.getAllPhoneNumbers(pageable);
    }

    @Override
    public Page<CustomerPhoneNumbersResponse> getPhoneNumbersForCustomer(
            String customerNameOrEmailId, Pageable pageable) {
        return phoneNumberQueryService.getPhoneNumbersForCustomer(customerNameOrEmailId, pageable);
    }

    @Override
    @Transactional
    public void activatePhoneNumberForCustomer(PhoneActivationRequest phoneActivationRequest) {
        // Step 1: Check if phone Number exist and available and update the details
        Long phoneNumberId = phoneNumberActivationService.activatePhoneNumber(
                phoneActivationRequest.getPhoneNumber(), phoneActivationRequest.getPhoneNumberType());

        // Step 2: Customer must be already in the records. So fetch the customer with name + email id
        Long customerId = customerDetailsService.getCustomerDetailsForNameAndEmailId(
                phoneActivationRequest.getCustomerName(),
                phoneActivationRequest.getCustomerEmailId());

        // Step 3: For the updated phone number and created/fetched customer, create a new mapping
        cpMappingService.createCustomerAndPhoneMappingIfNotExists(
                customerId, phoneNumberId);
    }
}
