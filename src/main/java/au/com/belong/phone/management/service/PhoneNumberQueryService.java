package au.com.belong.phone.management.service;

import au.com.belong.phone.management.dto.data.CustomerPhoneNumbersResultSet;
import au.com.belong.phone.management.dto.response.CustomerPhoneNumbersResponse;
import au.com.belong.phone.management.dto.response.PhoneNumbersResponse;
import au.com.belong.phone.management.exception.ResourceNotFoundException;
import au.com.belong.phone.management.model.CustomerDetails;
import au.com.belong.phone.management.model.PhoneNumberDetails;
import au.com.belong.phone.management.repository.CustomerDetailsRepository;
import au.com.belong.phone.management.repository.CustomerPhoneNumberMappingRepository;
import au.com.belong.phone.management.repository.PhoneNumberDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_NOT_FOUND_FOR_CUSTOMER_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_NOT_FOUND_CODE;
import static au.com.belong.phone.management.utils.ValidationUtils.isInputCustomerPartialName;
import static au.com.belong.phone.management.utils.ValidationUtils.isInputEmailFormat;
import static au.com.belong.phone.management.utils.ValidationUtils.validateCustomerEmailIdFormat;
import static au.com.belong.phone.management.utils.ValidationUtils.validateCustomerNameFormat;

@Service
public class PhoneNumberQueryService {

    private static final Logger log = LoggerFactory.getLogger(PhoneNumberQueryService.class);

    private final PhoneNumberDetailsRepository phoneNumberDetailsRepository;
    private final CustomerDetailsRepository customerDetailsRepository;
    private final CustomerPhoneNumberMappingRepository customerPhoneNumberMappingRepository;

    public PhoneNumberQueryService(PhoneNumberDetailsRepository phoneNumberDetailsRepository,
                                   CustomerDetailsRepository customerDetailsRepository,
                                   CustomerPhoneNumberMappingRepository customerPhoneNumberMappingRepository) {
        this.phoneNumberDetailsRepository = phoneNumberDetailsRepository;
        this.customerDetailsRepository = customerDetailsRepository;
        this.customerPhoneNumberMappingRepository = customerPhoneNumberMappingRepository;
    }

    public Page<PhoneNumbersResponse> getAllPhoneNumbers(Pageable pageable) {
        log.info("Retrieving all available phone numbers...");
        Page<PhoneNumbersResponse> phoneNumbersList = phoneNumberDetailsRepository.findAll(pageable)
                .map(this::mapToPhoneNumbersResponse);

        if (phoneNumbersList.isEmpty()) {
            log.warn("No phone numbers found");
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND_CODE,
                    PHONE_NUMBER_NOT_FOUND_MESSAGE);
        }

        return phoneNumbersList;
    }

    private PhoneNumbersResponse mapToPhoneNumbersResponse(PhoneNumberDetails phoneNumberDetails) {
        return new PhoneNumbersResponse(
                phoneNumberDetails.getPhoneNumber(),
                phoneNumberDetails.getPhoneNumberType(),
                phoneNumberDetails.getPhoneNumberStatus(),
                phoneNumberDetails.isActive());
    }

    public Page<CustomerPhoneNumbersResponse> getPhoneNumbersForCustomer(
            String customerNameOrEmailId, Pageable pageable) {
        log.info("Fetching phone numbers for customer : {}", customerNameOrEmailId);

        if (isInputCustomerPartialName(customerNameOrEmailId)) {
            validateCustomerNameFormat(customerNameOrEmailId);
            // Fetch the response via the NativeQuery based logic
            return getPhoneNumbersForCustomerPartialName(customerNameOrEmailId, pageable);
        }

        // Fetch the response via the Java based logic
        return getPhoneNumbersForCustomerFullNameOrEmailId(customerNameOrEmailId, pageable);
    }

    private Page<CustomerPhoneNumbersResponse> getPhoneNumbersForCustomerPartialName(
            String customerName, Pageable pageable) {

        if (customerDetailsRepository.
                findByCustomerNameContainingIgnoreCase(
                        customerName, pageable).isEmpty()) {
            log.warn("No customers found matching the partial name: '{}'", customerName);
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND_CODE,
                    CUSTOMER_NOT_FOUND_MESSAGE);
        }

        Page<CustomerPhoneNumbersResultSet> cpnResultSets =
                findAllPhoneNumbersMatchingCustomersAsResultSet(customerName, pageable);

        if (cpnResultSets.isEmpty()) {
            log.warn("Customers exist but no phone numbers found for name: '{}'", customerName);
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND_CODE,
                    PHONE_NUMBER_NOT_FOUND_FOR_CUSTOMER_MESSAGE);
        }

        List<CustomerPhoneNumbersResponse> cpnResponseList =
                buildCustomerPhoneNumbersResponseFromResultSet(cpnResultSets.getContent());

        return new PageImpl<>(cpnResponseList, pageable, cpnResultSets.getTotalElements());
    }

    private Page<CustomerPhoneNumbersResultSet> findAllPhoneNumbersMatchingCustomersAsResultSet(
            String customerName, Pageable pageable) {

        Page<CustomerPhoneNumbersResultSet> customerPhoneNumbersResultSet =
                customerDetailsRepository.findCustomerPhoneNumbersResultSet(
                        customerName, pageable);

        if (customerPhoneNumbersResultSet.isEmpty()) {
            log.warn("No phone numbers found for the customer: '{}'", customerName);
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND_CODE, PHONE_NUMBER_NOT_FOUND_MESSAGE);
        }

        return customerPhoneNumbersResultSet;
    }

    private List<CustomerPhoneNumbersResponse> buildCustomerPhoneNumbersResponseFromResultSet(
            List<CustomerPhoneNumbersResultSet> cpnResultsList) {

        Map<String, CustomerPhoneNumbersResponse> cpnResponseMap = new LinkedHashMap<>();

        for (CustomerPhoneNumbersResultSet eachResultSet : cpnResultsList) {
            String cpnEntryKey = eachResultSet.getCustomerName() + "|" + eachResultSet.getCustomerEmailId();

            PhoneNumbersResponse pnResponse = new PhoneNumbersResponse(
                    eachResultSet.getPhoneNumber(),
                    eachResultSet.getPhoneNumberType(),
                    eachResultSet.getPhoneNumberStatus(),
                    eachResultSet.getPhoneNumberActive());

            if (cpnResponseMap.containsKey(cpnEntryKey)) {
                CustomerPhoneNumbersResponse cpnResponse = cpnResponseMap.get(cpnEntryKey);
                cpnResponse.getPhoneNumbersResponseList().add(pnResponse);
            } else {
                List<PhoneNumbersResponse> pnResponseList = new ArrayList<>();
                pnResponseList.add(pnResponse);

                CustomerPhoneNumbersResponse cpnResponse = new CustomerPhoneNumbersResponse(
                        eachResultSet.getCustomerName(),
                        eachResultSet.getCustomerEmailId(),
                        pnResponseList);

                cpnResponseMap.put(cpnEntryKey, cpnResponse);
            }
        }

        return new ArrayList<>(cpnResponseMap.values());
    }

    private Page<CustomerPhoneNumbersResponse> getPhoneNumbersForCustomerFullNameOrEmailId(
            String customerNameOrEmailId, Pageable pageable) {
        List<CustomerDetails> matchedCustomers = findMatchingCustomers(customerNameOrEmailId, pageable);

        List<CustomerPhoneNumbersResponse> responseList = new ArrayList<>();
        for (CustomerDetails customer : matchedCustomers) {
            List<PhoneNumbersResponse> pnResponseList = fetchPhoneNumberResponseForCustomer(
                    customer.getCustomerDetailsId(), customer.getCustomerName());

            responseList.add(new CustomerPhoneNumbersResponse(
                    customer.getCustomerName(),
                    customer.getCustomerEmailId(), pnResponseList));
        }

        return new PageImpl<>(responseList, pageable, matchedCustomers.size());
    }

    private List<CustomerDetails> findMatchingCustomers(String customerNameOrEmailId, Pageable pageable) {
        if (isInputEmailFormat(customerNameOrEmailId)) {
            return findMatchingCustomersByEmailId(customerNameOrEmailId, pageable);
        } else {
            return findMatchingCustomersByName(customerNameOrEmailId, pageable);
        }
    }

    private List<CustomerDetails> findMatchingCustomersByEmailId(String customerEmailId, Pageable pageable) {
        log.debug("Detected email id is the input. Validating the format...");
        validateCustomerEmailIdFormat(customerEmailId);

        Page<CustomerDetails> customerDetailsPage =
                customerDetailsRepository.findByCustomerEmailIdIgnoreCase(customerEmailId, pageable);
        if (customerDetailsPage.isEmpty()) {
            log.warn("No customers found matching email id: '{}'", customerEmailId);
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND_CODE, CUSTOMER_NOT_FOUND_MESSAGE);
        }

        return customerDetailsPage.getContent();
    }

    private List<CustomerDetails> findMatchingCustomersByName(String customerName, Pageable pageable) {
        log.debug("Detected name is the input. Validating the format...");
        validateCustomerNameFormat(customerName);

        Page<CustomerDetails> customerDetailsList =
                customerDetailsRepository.findByCustomerNameContainingIgnoreCase(customerName, pageable);
        if (customerDetailsList.isEmpty()) {
            log.warn("No customers found matching name: '{}'", customerName);
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND_CODE, CUSTOMER_NOT_FOUND_MESSAGE);
        }

        return customerDetailsList.getContent();
    }

    private List<PhoneNumbersResponse> fetchPhoneNumberResponseForCustomer(
            Long customerDetailsId, String customerName) {

        List<PhoneNumbersResponse> pnResponseList =
                customerPhoneNumberMappingRepository.findPhoneNumbersResponseByCustomerId(customerDetailsId);
        if (pnResponseList.isEmpty()) {
            log.warn("No Phone Numbers found for the customer: '{}'", customerName);
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND_CODE,
                    PHONE_NUMBER_NOT_FOUND_FOR_CUSTOMER_MESSAGE);
        }
        return pnResponseList;
    }
}