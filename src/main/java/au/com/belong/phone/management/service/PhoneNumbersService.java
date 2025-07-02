package au.com.belong.phone.management.service;

import au.com.belong.phone.management.dto.request.PhoneActivationRequest;
import au.com.belong.phone.management.dto.response.CustomerPhoneNumbersResponse;
import au.com.belong.phone.management.dto.response.PhoneNumbersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhoneNumbersService {

    Page<PhoneNumbersResponse> getAllPhoneNumbers(Pageable pageable);

    Page<CustomerPhoneNumbersResponse> getPhoneNumbersForCustomer(String customerNameOrEmailId, Pageable pageable);

    void activatePhoneNumberForCustomer(PhoneActivationRequest phoneActivationRequest);
}
