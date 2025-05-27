package au.com.belong.phone.management.constants;

public class PhoneManagementConstants {

    // --- Validation/Error Codes ---
    public static final String VALID_INPUT_REQUIRED_CODE = "VALID_INPUT_REQUIRED_CODE";
    public static final String RESOURCE_NOT_FOUND_CODE = "RESOURCE_NOT_FOUND_CODE";
    public static final String RESOURCE_CONFLICT_CODE = "RESOURCE_CONFLICT_CODE";

    // --- Customer Name Validation ---
    public static final String CUSTOMER_NAME_FULL_NAME_MESSAGE =
            "Customer name must not be blank and must be a full name";
    public static final String CUSTOMER_NAME_FORMAT_MESSAGE =
            "Customer name should contain only letters, spaces, hyphens (-), and periods (.)";
    public static final String CUSTOMER_NAME_FORMAT_REGEX = "^[A-Za-z.\\- ]+$";

    // --- Customer Email Validation ---
    public static final String CUSTOMER_EMAIL_ID_BLANK_MESSAGE =
            "Customer email id must not be blank";
    public static final String CUSTOMER_EMAIL_ID_FORMAT_MESSAGE =
            "Customer email id must be a valid email with proper domain. "
                    + "Email id can contain only letters, digits and ! # $ % & ' * + - / = ? ^ _ ` { | } ~ -";
    public static final String CUSTOMER_EMAIL_ID_FORMAT_REGEX =
            "^(?![.])[A-Za-z0-9+_.-]+(?<![.])@(?:(?!.*\\.\\.)[A-Za-z0-9-]+\\.)+[A-Za-z]{2,}$";

    public static final String VALID_CUSTOMER_NAME_EMAIL_MESSAGE =
            "Customer Name or Email Id is required";
    public static final String CUSTOMER_EMAIL_SEPARATOR = "@";

    // --- Phone Number Validation ---
    public static final String PHONE_NUMBER_BLANK_MESSAGE =
            "Phone Number must not be blank";
    public static final String PHONE_NUMBER_FORMAT_MESSAGE =
            "Phone Number can be a 10 digit mobile number starting with 0 "
                    + "or 8 digit landline number not starting with 0";
    public static final String PHONE_NUMBER_FORMAT_REGEX =
            "^(0\\d{9}|[1-9]\\d{7})$";

    // --- Phone Number Type Validation ---
    public static final String PHONE_NUMBER_TYPE_BLANK_MESSAGE =
            "Phone Number Type must not be blank";
    public static final String PHONE_NUMBER_TYPE_INVALID_MESSAGE =
            "Phone Number Type can be any of WORK, MOBILE, HOME, OTHER";
    public static final String PHONE_NUMBER_TYPE_FORMAT_REGEX =
            "^(WORK|MOBILE|OTHER|HOME)$";
    public static final String NULL_PHONE_NUMBER_TYPE_MESSAGE =
            "Phone Number Type is required";
    public static final String VALID_PHONE_NUMBER_TYPE_MESSAGE =
            "Valid Phone Number Type is required";

    // --- Phone Number Availability Messages ---
    public static final String PHONE_NUMBER_NOT_FOUND_MESSAGE =
            "No available phone number(s) found";
    public static final String PHONE_NUMBER_NOT_FOUND_FOR_CUSTOMER_MESSAGE =
            "No available phone number(s) found for the customer";
    public static final String PHONE_NUMBER_ASSIGNED_ACTIVE_MESSAGE =
            "Cannot use the Phone Number. It's already assigned and active";
    public static final String PHONE_NUMBER_ASSIGNED_INACTIVE_MESSAGE =
            "Cannot use the Phone Number. It's already assigned but inactive";

    // --- Other Customer Messages ---
    public static final String CUSTOMER_NOT_FOUND_MESSAGE = "Customer not found";
    public static final String EMAIL_ALREADY_ASSOCIATED_MESSAGE =
            "Email is already associated with a different name";

    // --- Common Utility Regex ---
    public static final String WHITE_SPACE_REGEX = "\\s+";

    private PhoneManagementConstants() {
        // Prevent instantiation
    }

}
