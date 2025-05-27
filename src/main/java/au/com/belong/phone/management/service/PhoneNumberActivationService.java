package au.com.belong.phone.management.service;

import au.com.belong.phone.management.exception.ResourceConflictException;
import au.com.belong.phone.management.exception.ResourceNotFoundException;
import au.com.belong.phone.management.model.PhoneNumberDetails;
import au.com.belong.phone.management.model.PhoneNumberStatus;
import au.com.belong.phone.management.model.PhoneNumberType;
import au.com.belong.phone.management.repository.PhoneNumberDetailsRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_ASSIGNED_ACTIVE_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_ASSIGNED_INACTIVE_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_CONFLICT_CODE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_NOT_FOUND_CODE;

@Service
public class PhoneNumberActivationService {

    private static final Logger log = LoggerFactory.getLogger(PhoneNumberActivationService.class);

    private final PhoneNumberDetailsRepository phoneNumberDetailsRepository;

    public PhoneNumberActivationService(PhoneNumberDetailsRepository repository) {
        this.phoneNumberDetailsRepository = repository;
    }

    @Transactional
    public Long activatePhoneNumber(String phoneNumber, String phoneNumberType) {
        log.debug("Activating phone number: {}, with type: {}", phoneNumber, phoneNumberType);
        Optional<PhoneNumberDetails> optPhoneNumberDetails = phoneNumberDetailsRepository.
                findPhoneDetailsByPhoneNumber(phoneNumber);
        if (optPhoneNumberDetails.isEmpty()) {
            log.warn("Phone number not found: {}", phoneNumber);
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND_CODE,
                    PHONE_NUMBER_NOT_FOUND_MESSAGE);
        }

        PhoneNumberDetails currentPhoneNumberDetails = optPhoneNumberDetails.get();

        // Validate the phone number and update only if available
        validatePhoneNumberAvailability(currentPhoneNumberDetails);

        // Prepare the PhoneNumberDetails with the new details
        PhoneNumberDetails updatedPhoneNumberDetails = getUpdatedPhoneNumberDetails(
                currentPhoneNumberDetails, phoneNumberType);

        log.debug("Current phone number status: {}, active: {}",
                currentPhoneNumberDetails.getPhoneNumberStatus(),
                currentPhoneNumberDetails.isActive());

        phoneNumberDetailsRepository.save(updatedPhoneNumberDetails);

        log.info("Phone number {} successfully activated. ID: {}",
                phoneNumber, updatedPhoneNumberDetails.getPhoneNumberDetailsId());

        return updatedPhoneNumberDetails.getPhoneNumberDetailsId();
    }

    private PhoneNumberDetails getUpdatedPhoneNumberDetails(
            PhoneNumberDetails phoneNumberDetails,
            String phoneNumberType) {
        phoneNumberDetails.setPhoneNumberType(PhoneNumberType.valueOf(phoneNumberType));
        phoneNumberDetails.setActive(true);
        phoneNumberDetails.setPhoneNumberStatus(PhoneNumberStatus.ASSIGNED);
        return phoneNumberDetails;
    }

    private void validatePhoneNumberAvailability(PhoneNumberDetails phoneNumberDetails) {
        String phoneNumber = phoneNumberDetails.getPhoneNumber();

        if (phoneNumberDetails.getPhoneNumberStatus().equals(PhoneNumberStatus.ASSIGNED)) {
            if (phoneNumberDetails.isActive()) {
                log.warn("Phone number already active and assigned: {}", phoneNumber);
                throw new ResourceConflictException(RESOURCE_CONFLICT_CODE,
                        PHONE_NUMBER_ASSIGNED_ACTIVE_MESSAGE);
            } else {
                log.warn("Phone number assigned but inactive: {}", phoneNumber);
                throw new ResourceConflictException(RESOURCE_CONFLICT_CODE,
                        PHONE_NUMBER_ASSIGNED_INACTIVE_MESSAGE);
            }
        }
    }
}