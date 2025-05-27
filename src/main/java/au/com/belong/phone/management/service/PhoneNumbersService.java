package au.com.belong.phone.management.service;

import au.com.belong.phone.management.dto.PhoneActivationRequest;
import au.com.belong.phone.management.dto.PhoneNumbersResponse;
import au.com.belong.phone.management.model.PhoneNumberDetails;

import java.util.List;

public interface PhoneNumbersService {

    List<PhoneNumberDetails> getAllPhoneNumbers();

    List<PhoneNumbersResponse> getPhoneNumbersForCustomer(String customerNameOrEmailId);

    void activatePhoneNumberForCustomer(PhoneActivationRequest phoneActivationRequest);
}
