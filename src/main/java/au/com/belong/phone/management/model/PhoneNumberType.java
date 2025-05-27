package au.com.belong.phone.management.model;

import au.com.belong.phone.management.exception.ValidInputRequiredException;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.*;
import static au.com.belong.phone.management.utils.ValidationUtils.validateNotNullOrEmpty;

public enum PhoneNumberType {
    WORK, MOBILE, HOME, OTHER;

    public static void validatePhoneNumberType(String phoneTypeValue) {
        validateNotNullOrEmpty(phoneTypeValue, NULL_PHONE_NUMBER_TYPE_MESSAGE);

        boolean isValid = false;
        for (PhoneNumberType phoneNumberType : PhoneNumberType.values()) {
            if (phoneNumberType.name().equalsIgnoreCase(phoneTypeValue.trim())) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new ValidInputRequiredException(
                    VALID_INPUT_REQUIRED_CODE, VALID_PHONE_NUMBER_TYPE_MESSAGE);
        }
    }
}
