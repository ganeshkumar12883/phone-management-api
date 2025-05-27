package au.com.belong.phone.management.dto;

import au.com.belong.phone.management.model.PhoneNumberDetails;

import java.util.List;

public class PhoneNumbersResponse {

    private String customerName;

    private String customerEmailId;

    private List<PhoneNumberDetails> phoneNumberDetails;

    public PhoneNumbersResponse(String customerName,
                                String customerEmailId,
                                List<PhoneNumberDetails> phoneNumberDetails) {
        this.customerName = customerName;
        this.customerEmailId = customerEmailId;
        this.phoneNumberDetails = phoneNumberDetails;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmailId() {
        return customerEmailId;
    }

    public void setCustomerEmailId(String customerEmailId) {
        this.customerEmailId = customerEmailId;
    }

    public List<PhoneNumberDetails> getPhoneNumberDetails() {
        return phoneNumberDetails;
    }

    public void setPhoneNumberDetails(List<PhoneNumberDetails> phoneNumberDetails) {
        this.phoneNumberDetails = phoneNumberDetails;
    }
}
