package au.com.belong.phone.management.unitTests.service;

import au.com.belong.phone.management.dto.PhoneNumbersResponse;
import au.com.belong.phone.management.model.PhoneNumberDetails;
import au.com.belong.phone.management.model.PhoneNumberStatus;
import au.com.belong.phone.management.model.PhoneNumberType;
import au.com.belong.phone.management.service.PhoneNumberActivationService;
import au.com.belong.phone.management.service.PhoneNumberQueryService;
import au.com.belong.phone.management.service.PhoneNumbersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhoneNumberServiceImplTest {

    @Mock
    private PhoneNumberQueryService mockPhoneNumberQueryService;

    @Mock
    private PhoneNumberActivationService mockPhoneNumberActivationService;

    @InjectMocks
    private PhoneNumbersServiceImpl phoneNumbersService;

    private List<PhoneNumberDetails> phoneNumbersList;

    List<PhoneNumbersResponse> phoneNumbersResponseList;

    @BeforeEach
    void setUp() {
        PhoneNumberDetails phone1 = new PhoneNumberDetails.Builder()
                .phoneNumber("0400123456")
                .phoneNumberType(PhoneNumberType.MOBILE)
                .phoneNumberStatus(PhoneNumberStatus.AVAILABLE)
                .active(true)
                .build();

        PhoneNumberDetails phone2 = new PhoneNumberDetails.Builder()
                .phoneNumber("0400123457")
                .phoneNumberStatus(PhoneNumberStatus.AVAILABLE)
                .active(false)
                .build();

        PhoneNumberDetails phone3 = new PhoneNumberDetails.Builder()
                .phoneNumber("0400123458")
                .phoneNumberType(PhoneNumberType.MOBILE)
                .phoneNumberStatus(PhoneNumberStatus.DORMANT)
                .active(true)
                .build();

        phoneNumbersList = List.of(phone1, phone2, phone3);

        PhoneNumbersResponse phoneNumbersResponse = new PhoneNumbersResponse(
                "John Smith",
                "john.smith@example.com",
                List.of(phone1)
        );

        phoneNumbersResponseList = List.of(phoneNumbersResponse);
    }

    @Test
    void testGetAllPhoneNumbers_WhenPhoneNumberExists_ReturnsPhoneNumbers() {

        when(mockPhoneNumberQueryService.getAllPhoneNumbers()).thenReturn(phoneNumbersList);

        List<PhoneNumberDetails> result = phoneNumbersService.getAllPhoneNumbers();

        assertEquals(phoneNumbersList, result);
        verify(mockPhoneNumberQueryService).getAllPhoneNumbers();
    }

    @Test
    void testGetPhoneNumbersForCustomer_ReturnsPhoneResponseList() {

        when(mockPhoneNumberQueryService.getPhoneNumbersForCustomer("John Smith"))
                .thenReturn(phoneNumbersResponseList);

        List<PhoneNumbersResponse> result = phoneNumbersService
                .getPhoneNumbersForCustomer("John Smith");

        assertEquals(phoneNumbersResponseList, result);
        verify(mockPhoneNumberQueryService).getPhoneNumbersForCustomer("John Smith");
    }

    @Test
    void testActivatePhoneNumbersForCustomer_ReturnsPhoneResponseList() {
        when(mockPhoneNumberActivationService.activatePhoneNumber(
                "0400123457", "MOBILE"))
                .thenReturn(1L);

        Long resultId = mockPhoneNumberActivationService.activatePhoneNumber(
                "0400123457", "MOBILE");

        assertEquals(1L, resultId);
        verify(mockPhoneNumberActivationService).activatePhoneNumber(
                "0400123457", "MOBILE");
    }
}
