package au.com.belong.phone.management.utils;

import au.com.belong.phone.management.exception.ValidInputRequiredException;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_EMAIL_ID_FORMAT_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_EMAIL_ID_FORMAT_REGEX;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_EMAIL_SEPARATOR;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_NAME_FORMAT_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_NAME_FORMAT_REGEX;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_NAME_FULL_NAME_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.VALID_CUSTOMER_NAME_EMAIL_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.VALID_INPUT_REQUIRED_CODE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.WHITE_SPACE_REGEX;

/**
 * Utility class for input validation.
 */
public class ValidationUtils {

    /**
     * Validates that the input string is not null or empty.
     *
     * @param value        the input string to check
     * @param errorMessage the error message for invalid input
     * @throws ValidInputRequiredException if the value is null or empty
     */
    public static void validateNotNullOrEmpty(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidInputRequiredException(VALID_INPUT_REQUIRED_CODE, errorMessage);
        }
    }

    public static void validateCustomerNameFormat(String customerName) {
        if (customerName == null || !customerName.matches(CUSTOMER_NAME_FORMAT_REGEX)) {
            throw new ValidInputRequiredException(
                    VALID_INPUT_REQUIRED_CODE, CUSTOMER_NAME_FORMAT_MESSAGE);
        }
    }

    public static Boolean isInputCustomerPartialName(String customerName) {
        return customerName.split(WHITE_SPACE_REGEX).length < 2;
    }

    public static void validateCustomerEmailIdFormat(String customerEmailId) {
        if (customerEmailId == null ||
                !customerEmailId.trim().matches(CUSTOMER_EMAIL_ID_FORMAT_REGEX)) {
            throw new ValidInputRequiredException(
                    VALID_INPUT_REQUIRED_CODE, CUSTOMER_EMAIL_ID_FORMAT_MESSAGE);
        }
    }

    public static boolean isInputEmailFormat(String customerNameOrEmailId) {
        validateNotNullOrEmpty(customerNameOrEmailId, VALID_CUSTOMER_NAME_EMAIL_MESSAGE);
        return customerNameOrEmailId.contains(CUSTOMER_EMAIL_SEPARATOR);
    }
}
