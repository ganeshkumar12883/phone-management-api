package au.com.belong.phone.management.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_NAME_FORMAT_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_NAME_FORMAT_REGEX;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_NAME_FULL_NAME_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.WHITE_SPACE_REGEX;

public class CustomerNameFormatValidator implements
        ConstraintValidator<ValidFormatCustomerName, String> {

    @Override
    public boolean isValid(String customerName, ConstraintValidatorContext context) {
        if (customerName == null || customerName.trim().isEmpty()) {
            return buildViolationConstraint(context, CUSTOMER_NAME_FULL_NAME_MESSAGE);
        }

        String trimmedCustomerName = customerName.trim();

        // Step 2: Check if only allowed characters
        if (!trimmedCustomerName.matches(CUSTOMER_NAME_FORMAT_REGEX)) {
            return buildViolationConstraint(context, CUSTOMER_NAME_FORMAT_MESSAGE);
        }

        // Step 3: Validate full name structure
        String[] parts = trimmedCustomerName.split(WHITE_SPACE_REGEX);
        if (parts.length < 2 || parts.length > 3) {
            return buildViolationConstraint(context, CUSTOMER_NAME_FULL_NAME_MESSAGE);
        }

        return true;
    }

    private boolean buildViolationConstraint(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        return false;
    }
}
