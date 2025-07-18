package au.com.belong.phone.management.service;

import au.com.belong.phone.management.exception.ResourceConflictException;
import au.com.belong.phone.management.exception.ResourceNotFoundException;
import au.com.belong.phone.management.model.CustomerDetails;
import au.com.belong.phone.management.repository.CustomerDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.EMAIL_ALREADY_ASSOCIATED_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_CONFLICT_CODE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_NOT_FOUND_CODE;

@Service
public class CustomerDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomerDetailsService.class);

    private final CustomerDetailsRepository customerDetailsRepository;

    public CustomerDetailsService(CustomerDetailsRepository customerDetailsRepository) {
        this.customerDetailsRepository = customerDetailsRepository;
    }

    @Transactional
    public Long getCustomerDetailsForNameAndEmailId(String customerName, String customerEmailId) {
        log.info("Processing customer details: customerName='{}', customerEmailId='{}'",
                customerName, customerEmailId);

        // Step 1: Check by Name + Email Id - the unique value
        Optional<CustomerDetails> optCustomerDetails = customerDetailsRepository
                .findByCustomerNameAndEmailIdIgnoreCase(customerName, customerEmailId);

        if (optCustomerDetails.isEmpty()) {
            log.warn("Customer with name {} and email id {} does not exist", customerName, customerEmailId);
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND_CODE, CUSTOMER_NOT_FOUND_MESSAGE);
        }

        return optCustomerDetails.get().getCustomerDetailsId();
    }
}