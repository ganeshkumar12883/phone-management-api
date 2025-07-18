package au.com.belong.phone.management.dto.response;

import au.com.belong.phone.management.model.PhoneNumberStatus;
import au.com.belong.phone.management.model.PhoneNumberType;

public class PhoneNumbersResponse {

    private String phoneNumber;

    private PhoneNumberType phoneNumberType;

    private PhoneNumberStatus phoneNumberStatus;

    private Boolean phoneNumberActive;

    public PhoneNumbersResponse(String phoneNumber,
                                PhoneNumberType phoneNumberType,
                                PhoneNumberStatus phoneNumberStatus,
                                Boolean phoneNumberActive) {
        this.phoneNumber = phoneNumber;
        this.phoneNumberType = phoneNumberType;
        this.phoneNumberStatus = phoneNumberStatus;
        this.phoneNumberActive = phoneNumberActive;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PhoneNumberType getPhoneNumberType() {
        return phoneNumberType;
    }

    public void setPhoneNumberType(PhoneNumberType phoneNumberType) {
        this.phoneNumberType = phoneNumberType;
    }

    public PhoneNumberStatus getPhoneNumberStatus() {
        return phoneNumberStatus;
    }

    public void setPhoneNumberStatus(PhoneNumberStatus phoneNumberStatus) {
        this.phoneNumberStatus = phoneNumberStatus;
    }

    public Boolean getPhoneNumberActive() {
        return phoneNumberActive;
    }

    public void setPhoneNumberActive(Boolean phoneNumberActive) {
        this.phoneNumberActive = phoneNumberActive;
    }
}
