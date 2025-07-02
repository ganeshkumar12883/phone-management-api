package au.com.belong.phone.management.dto.response;

import java.util.List;

public class CustomerPhoneNumbersResponse {

    private String customerName;

    private String customerEmailId;

    private List<PhoneNumbersResponse> phoneNumbersResponseList;

    public CustomerPhoneNumbersResponse(String customerName,
                                        String customerEmailId,
                                        List<PhoneNumbersResponse> phoneNumbersResponseList) {
        this.customerName = customerName;
        this.customerEmailId = customerEmailId;
        this.phoneNumbersResponseList = phoneNumbersResponseList;
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

    public List<PhoneNumbersResponse> getPhoneNumbersResponseList() {
        return phoneNumbersResponseList;
    }

    public void setPhoneNumbersResponseList(List<PhoneNumbersResponse> phoneNumbersResponseList) {
        this.phoneNumbersResponseList = phoneNumbersResponseList;
    }
}
