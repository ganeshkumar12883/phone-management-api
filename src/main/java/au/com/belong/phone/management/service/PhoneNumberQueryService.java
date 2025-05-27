package au.com.belong.phone.management.service;

import au.com.belong.phone.management.dto.PhoneNumbersResponse;
import au.com.belong.phone.management.exception.ResourceNotFoundException;
import au.com.belong.phone.management.model.CustomerDetails;
import au.com.belong.phone.management.model.PhoneNumberDetails;
import au.com.belong.phone.management.repository.CustomerDetailsRepository;
import au.com.belong.phone.management.repository.PhoneNumberDetailsRepository;
import jakarta.persistence.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_NOT_FOUND_FOR_CUSTOMER_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_NOT_FOUND_CODE;
import static au.com.belong.phone.management.utils.ValidationUtils.isInputEmailFormat;
import static au.com.belong.phone.management.utils.ValidationUtils.validateCustomerEmailIdFormat;
import static au.com.belong.phone.management.utils.ValidationUtils.validateCustomerNameFormat;

@Service
public class PhoneNumberQueryService {

    private static final Logger log = LoggerFactory.getLogger(PhoneNumberQueryService.class);

    private final PhoneNumberDetailsRepository phoneNumberDetailsRepository;
    private final CustomerDetailsRepository customerDetailsRepository;

    public PhoneNumberQueryService(PhoneNumberDetailsRepository phoneNumberDetailsRepository,
                                   CustomerDetailsRepository customerDetailsRepository) {
        this.phoneNumberDetailsRepository = phoneNumberDetailsRepository;
        this.customerDetailsRepository = customerDetailsRepository;
    }

    public List<PhoneNumberDetails> getAllPhoneNumbers() {
        log.info("Retrieving all available phone numbers...");
        List<PhoneNumberDetails> phoneNumbersList = phoneNumberDetailsRepository.findAll();

        if (phoneNumbersList.isEmpty()) {
            log.warn("No phone numbers found");
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND_CODE,
                    PHONE_NUMBER_NOT_FOUND_MESSAGE);
        }

        return phoneNumbersList;
    }

    public List<PhoneNumbersResponse> getPhoneNumbersForCustomer(String customerNameOrEmailId) {
        log.info("Fetching phone numbers for customer : {}", customerNameOrEmailId);

        List<CustomerDetails> matchedCustomers = findMatchingCustomers(customerNameOrEmailId);

        List<PhoneNumbersResponse> responseList = new ArrayList<>();
        for (CustomerDetails customer : matchedCustomers) {
            PhoneNumbersResponse response = buildPhoneNumberResponse(customer);
            responseList.add(response);
        }

        return responseList;
    }

    private List<CustomerDetails> findMatchingCustomers(String customerNameOrEmailId) {
        if (isInputEmailFormat(customerNameOrEmailId)) {
            return findMatchingCustomersByEmailId(customerNameOrEmailId);
        } else {
            return findMatchingCustomersByName(customerNameOrEmailId);
        }
    }

    private List<CustomerDetails> findMatchingCustomersByEmailId(String customerEmailId) {
        log.debug("Detected email id is the input. Validating the format...");
        validateCustomerEmailIdFormat(customerEmailId);

        Optional<CustomerDetails> optCustomerDetails =
                customerDetailsRepository.findByCustomerEmailIdIgnoreCase(customerEmailId);
        if (optCustomerDetails.isEmpty()) {
            log.warn("No customers found matching email id: '{}'", customerEmailId);
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND_CODE, CUSTOMER_NOT_FOUND_MESSAGE);
        }

        return List.of(optCustomerDetails.get());
    }

    private List<CustomerDetails> findMatchingCustomersByName(String customerName) {
        log.debug("Detected name is the input. Validating the format...");
        validateCustomerNameFormat(customerName);

        List<CustomerDetails> customerDetailsList =
                customerDetailsRepository.findByCustomerNameContainingIgnoreCase(customerName);
        if (customerDetailsList.isEmpty()) {
            log.warn("No customers found matching name: '{}'", customerName);
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND_CODE, CUSTOMER_NOT_FOUND_MESSAGE);
        }

        return customerDetailsList;
    }

    private PhoneNumbersResponse buildPhoneNumberResponse(CustomerDetails customerDetails) {
        List<PhoneNumberDetails> phoneNumbersList =
                phoneNumberDetailsRepository.findPhoneNumbersByCustomerId(
                        customerDetails.getCustomerDetailsId());
        if (phoneNumbersList.isEmpty()) {
            log.warn("No Phone Numbers found for the customer: '{}'",
                    customerDetails.getCustomerName());
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND_CODE,
                    PHONE_NUMBER_NOT_FOUND_FOR_CUSTOMER_MESSAGE);
        }

        log.info("Customer '{}' has {} phone number(s)",
                customerDetails.getCustomerName(), phoneNumbersList.size());

        return new PhoneNumbersResponse(
                customerDetails.getCustomerName(),
                customerDetails.getCustomerEmailId(),
                phoneNumbersList
        );
    }
}