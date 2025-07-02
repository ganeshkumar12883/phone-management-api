package au.com.belong.phone.management.dto.data;

import au.com.belong.phone.management.model.PhoneNumberStatus;
import au.com.belong.phone.management.model.PhoneNumberType;

public class CustomerPhoneNumbersResultSet {

    private String customerName;

    private String customerEmailId;

    private String phoneNumber;

    private PhoneNumberType phoneNumberType;

    private PhoneNumberStatus phoneNumberStatus;

    private Boolean phoneNumberActive;

    public CustomerPhoneNumbersResultSet(String customerName,
                                         String customerEmailId,
                                         String phoneNumber,
                                         PhoneNumberType phoneNumberType,
                                         PhoneNumberStatus phoneNumberStatus,
                                         Boolean phoneNumberActive) {
        this.customerName = customerName;
        this.customerEmailId = customerEmailId;
        this.phoneNumber = phoneNumber;
        this.phoneNumberType = phoneNumberType;
        this.phoneNumberStatus = phoneNumberStatus;
        this.phoneNumberActive = phoneNumberActive;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmailId() {
        return customerEmailId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public PhoneNumberType getPhoneNumberType() {
        return phoneNumberType;
    }

    public PhoneNumberStatus getPhoneNumberStatus() {
        return phoneNumberStatus;
    }

    public Boolean getPhoneNumberActive() {
        return phoneNumberActive;
    }
}
