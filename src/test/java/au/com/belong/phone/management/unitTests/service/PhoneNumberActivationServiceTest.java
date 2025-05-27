package au.com.belong.phone.management.unitTests.service;

import au.com.belong.phone.management.exception.ResourceConflictException;
import au.com.belong.phone.management.exception.ResourceNotFoundException;
import au.com.belong.phone.management.model.PhoneNumberDetails;
import au.com.belong.phone.management.model.PhoneNumberStatus;
import au.com.belong.phone.management.model.PhoneNumberType;
import au.com.belong.phone.management.repository.PhoneNumberDetailsRepository;
import au.com.belong.phone.management.service.PhoneNumberActivationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_ASSIGNED_ACTIVE_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_ASSIGNED_INACTIVE_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_NOT_FOUND_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_CONFLICT_CODE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.RESOURCE_NOT_FOUND_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhoneNumberActivationServiceTest {

    @Mock
    private PhoneNumberDetailsRepository mockRepository;

    @InjectMocks
    private PhoneNumberActivationService activationService;

    private static final String PHONE_NUMBER_TO_TEST = "0412356789";

    @Test
    void shouldThrowException_WhenPhoneNumberIsNotExisting() {
        // Arrange
        when(mockRepository.findPhoneDetailsByPhoneNumber(anyString())).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()->
                activationService.activatePhoneNumber(
                        PHONE_NUMBER_TO_TEST, PhoneNumberType.MOBILE.name()));

        // Assert
        assertEquals(RESOURCE_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals(PHONE_NUMBER_NOT_FOUND_MESSAGE, exception.getReason());
    }

    @Test
    void shouldThrowException_WhenPhoneNumberIsAssignedAndActive() {
        PhoneNumberDetails phoneNumberDetails = new PhoneNumberDetails.Builder()
                .phoneNumber(PHONE_NUMBER_TO_TEST)
                .phoneNumberType(PhoneNumberType.MOBILE)
                .phoneNumberStatus(PhoneNumberStatus.ASSIGNED)
                .active(true)
                .build();

        // Arrange
        when(mockRepository.findPhoneDetailsByPhoneNumber(anyString())).
                thenReturn(Optional.of(phoneNumberDetails));

        // Act
        ResourceConflictException exception = assertThrows(ResourceConflictException.class, ()->
                activationService.activatePhoneNumber(
                        PHONE_NUMBER_TO_TEST, PhoneNumberType.MOBILE.name()));

        // Assert
        assertEquals(RESOURCE_CONFLICT_CODE, exception.getErrorCode());
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals(PHONE_NUMBER_ASSIGNED_ACTIVE_MESSAGE, exception.getReason());
    }

    @Test
    void shouldThrowException_WhenPhoneNumberIsAssignedAndInActive() {
        PhoneNumberDetails phoneNumberDetails = new PhoneNumberDetails.Builder()
                .phoneNumber(PHONE_NUMBER_TO_TEST)
                .phoneNumberType(PhoneNumberType.MOBILE)
                .phoneNumberStatus(PhoneNumberStatus.ASSIGNED)
                .active(false)
                .build();

        // Arrange
        when(mockRepository.findPhoneDetailsByPhoneNumber(anyString())).
                thenReturn(Optional.of(phoneNumberDetails));

        // Act
        ResourceConflictException exception = assertThrows(ResourceConflictException.class, ()->
                activationService.activatePhoneNumber(
                        PHONE_NUMBER_TO_TEST, PhoneNumberType.MOBILE.name()));

        // Assert
        assertEquals(RESOURCE_CONFLICT_CODE, exception.getErrorCode());
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals(PHONE_NUMBER_ASSIGNED_INACTIVE_MESSAGE, exception.getReason());
    }

    @Test
    void shouldUpdatePhoneNumberDetailsCorrectly() {
        PhoneNumberDetails phoneNumberDetails = new PhoneNumberDetails.Builder()
                .phoneNumber(PHONE_NUMBER_TO_TEST)
                .phoneNumberStatus(PhoneNumberStatus.AVAILABLE)
                .active(false)
                .build();

        PhoneNumberDetails updated = serviceTestAccess_getUpdatedPhoneNumberDetails(
                phoneNumberDetails, PhoneNumberType.MOBILE.name());

        assertTrue(updated.isActive());
        assertEquals(PhoneNumberStatus.ASSIGNED, updated.getPhoneNumberStatus());
        assertEquals(PhoneNumberType.MOBILE, updated.getPhoneNumberType());
    }

    private PhoneNumberDetails serviceTestAccess_getUpdatedPhoneNumberDetails(PhoneNumberDetails details, String type) {
        try {
            var method = activationService.getClass().getDeclaredMethod(
                    "getUpdatedPhoneNumberDetails", PhoneNumberDetails.class, String.class);
            method.setAccessible(true);
            return (PhoneNumberDetails) method.invoke(activationService, details, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke getUpdatedPhoneNumberDetails", e);
        }
    }
}