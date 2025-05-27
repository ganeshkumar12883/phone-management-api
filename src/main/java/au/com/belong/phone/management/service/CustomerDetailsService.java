package au.com.belong.phone.management.service;

import au.com.belong.phone.management.exception.ResourceConflictException;
import au.com.belong.phone.management.model.CustomerDetails;
import au.com.belong.phone.management.repository.CustomerDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.EMAIL_ALREADY_ASSOCIATED_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_CONFLICT_CODE;

@Service
public class CustomerDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomerDetailsService.class);

    private final CustomerDetailsRepository customerDetailsRepository;

    public CustomerDetailsService(CustomerDetailsRepository customerDetailsRepository) {
        this.customerDetailsRepository = customerDetailsRepository;
    }

    @Transactional
    public Long getOrCreateCustomerDetails(String customerName, String customerEmailId) {
        log.info("Processing customer details: customerName='{}', customerEmailId='{}'",
                customerName, customerEmailId);

        // Step 1: Check by Email Id - the unique value
        Optional<CustomerDetails> customerDetailsByEmailId = customerDetailsRepository
                .findByCustomerEmailIdIgnoreCase(customerEmailId);
        if (customerDetailsByEmailId.isPresent()) {
            log.debug("Customer Details found by email id: {}", customerEmailId);
            return handleEmailIdMatchedCustomer(customerDetailsByEmailId.get(), customerName);
        }

        // Step 2: Check by name, update the email id if empty to the existing customer
        Optional<CustomerDetails> customerDetailsByName = customerDetailsRepository
                .findByCustomerNameIgnoreCase(customerName);
        if (customerDetailsByName.isPresent()) {
            log.debug("Customer Details found by name: {}", customerName);
            return handleNameMatchedCustomer(customerDetailsByName.get(), customerEmailId);
        }

        // Step 3: Create a new customer with name and email id
        log.info("Creating new customer with details: customerName='{}', customerEmailId='{}'",
                customerName, customerEmailId);
        return createNewCustomer(customerName, customerEmailId);
    }

    private Long handleEmailIdMatchedCustomer(CustomerDetails existingCustomer, String customerName) {
        if (!existingCustomer.getCustomerName().equalsIgnoreCase(customerName)) {
            log.warn("Conflict: Email Id '{}' is already associated with customer name '{}', not '{}'",
                    existingCustomer.getCustomerEmailId(),
                    existingCustomer.getCustomerName(),
                    customerName);

            throw new ResourceConflictException(RESOURCE_CONFLICT_CODE, EMAIL_ALREADY_ASSOCIATED_MESSAGE);
        }
        return existingCustomer.getCustomerDetailsId();
    }

    private Long handleNameMatchedCustomer(CustomerDetails existingCustomer,
                                           String customerEmailId) {
        if (existingCustomer.getCustomerEmailId() == null
                || existingCustomer.getCustomerEmailId().trim().isEmpty()) {
            existingCustomer.setCustomerEmailId(customerEmailId);
            customerDetailsRepository.save(existingCustomer);
            log.info("Email Id '{}' is now associated to the existing customer '{}'",
                    customerEmailId, existingCustomer.getCustomerName());
        }
        return existingCustomer.getCustomerDetailsId();
    }

    private Long createNewCustomer(String customerName, String customerEmailId) {
        CustomerDetails newCustomer = new CustomerDetails.Builder()
                .customerName(customerName)
                .customerEmailId(customerEmailId)
                .build();

        customerDetailsRepository.save(newCustomer);
        log.info("New customer created and saved with customerName='{}', customerEmailId='{}'",
                customerName, customerEmailId);
        return newCustomer.getCustomerDetailsId();
    }
}