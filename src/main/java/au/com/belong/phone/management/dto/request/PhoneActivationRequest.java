package au.com.belong.phone.management.dto.request;

import au.com.belong.phone.management.validators.ValidFormatCustomerName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_EMAIL_ID_BLANK_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_EMAIL_ID_FORMAT_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_EMAIL_ID_FORMAT_REGEX;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_BLANK_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_FORMAT_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_FORMAT_REGEX;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_TYPE_BLANK_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_TYPE_FORMAT_REGEX;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.PHONE_NUMBER_TYPE_INVALID_MESSAGE;

@Schema(description = "Request to activate a phone number for a customer")
public class PhoneActivationRequest {

    @NotBlank(message = PHONE_NUMBER_BLANK_MESSAGE)
    @Pattern(
            regexp = PHONE_NUMBER_FORMAT_REGEX,
            message = PHONE_NUMBER_FORMAT_MESSAGE
    )
    @Schema(description = "Phone number to activate. " +
            "Must be a valid format: 10-digit starting with 0 or 8-digit not starting with 0."
            , example = "0123456789", pattern = "")
    private String phoneNumber;

    @NotBlank(message = PHONE_NUMBER_TYPE_BLANK_MESSAGE)
    @Pattern(
            regexp = PHONE_NUMBER_TYPE_FORMAT_REGEX,
            message = PHONE_NUMBER_TYPE_INVALID_MESSAGE
    )
    @Schema(description = "Type of phone number. " +
            "Allowed values: WORK, MOBILE, OTHER, HOME"
            , example = "MOBILE", pattern = "")
    private String phoneNumberType;

    @ValidFormatCustomerName
    @Schema(description = "Full name of the customer. " +
            "Must be a valid format: Contain only letters, spaces, hyphens (-), and periods (.)"
            , example = "John Doe or John-William Doe or John William Doe")
    private String customerName;

    @NotBlank(message = CUSTOMER_EMAIL_ID_BLANK_MESSAGE)
    @Pattern(
            regexp = CUSTOMER_EMAIL_ID_FORMAT_REGEX,
            message = CUSTOMER_EMAIL_ID_FORMAT_MESSAGE
    )
    @Schema(description = "Customer email address. " +
            "Must be a valid email with proper domain."
            , example = "john.doe@example.com", pattern = "")
    private String customerEmailId;

    public PhoneActivationRequest(String phoneNumber,
                                  String phoneNumberType,
                                  String customerName,
                                  String customerEmailId) {
        this.phoneNumber = phoneNumber;
        this.phoneNumberType = phoneNumberType;
        this.customerName = customerName;
        this.customerEmailId = customerEmailId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhoneNumberType() {
        return phoneNumberType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmailId() {
        return customerEmailId;
    }

}
