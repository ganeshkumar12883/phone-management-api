package au.com.belong.phone.management.service;

import au.com.belong.phone.management.exception.ResourceNotFoundException;
import au.com.belong.phone.management.model.CustomerDetails;
import au.com.belong.phone.management.model.CustomerPhoneNumberMapping;
import au.com.belong.phone.management.model.PhoneNumberDetails;
import au.com.belong.phone.management.repository.CustomerDetailsRepository;
import au.com.belong.phone.management.repository.CustomerPhoneNumberMappingRepository;
import au.com.belong.phone.management.repository.PhoneNumberDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_NOT_FOUND_CODE;

@Service
public class CustomerPhoneMappingService {

    private static final Logger log = LoggerFactory.getLogger(CustomerPhoneMappingService.class);

    private final CustomerPhoneNumberMappingRepository customerPhoneNumberMappingRepository;
    private final CustomerDetailsRepository customerDetailsRepository;
    private final PhoneNumberDetailsRepository phoneNumberDetailsRepository;

    public CustomerPhoneMappingService(CustomerPhoneNumberMappingRepository customerPhoneNumberMappingRepository,
                                       CustomerDetailsRepository customerDetailsRepository,
                                       PhoneNumberDetailsRepository phoneNumberDetailsRepository) {
        this.customerPhoneNumberMappingRepository = customerPhoneNumberMappingRepository;
        this.customerDetailsRepository = customerDetailsRepository;
        this.phoneNumberDetailsRepository = phoneNumberDetailsRepository;
    }

    @Transactional
    public void createCustomerAndPhoneMappingIfNotExists(Long customerId, Long phoneNumberId) {
        if (customerPhoneNumberMappingRepository.customerPhoneNumberMappingExists(customerId, phoneNumberId)) {
            log.info("Mapping already exists between customer {} and phone number {}. No action taken.",
                    customerId, phoneNumberId);
            return;
        }

        CustomerDetails customerDetails = customerDetailsRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        RESOURCE_NOT_FOUND_CODE, CUSTOMER_NOT_FOUND_MESSAGE));

        PhoneNumberDetails phoneNumberDetails = phoneNumberDetailsRepository.findById(phoneNumberId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        RESOURCE_NOT_FOUND_CODE, PHONE_NUMBER_NOT_FOUND_MESSAGE));

        customerPhoneNumberMappingRepository.save(
                buildMapping(customerDetails, phoneNumberDetails));
    }

    private CustomerPhoneNumberMapping buildMapping(CustomerDetails customer,
                                                    PhoneNumberDetails phoneNumber) {
        return new CustomerPhoneNumberMapping.Builder()
                .customerDetails(customer)
                .phoneNumberDetails(phoneNumber)
                .build();
    }
}