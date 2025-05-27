package au.com.belong.phone.management.unitTests.controller;

import au.com.belong.phone.management.controller.PhoneNumbersController;
import au.com.belong.phone.management.model.PhoneNumberDetails;
import au.com.belong.phone.management.model.PhoneNumberStatus;
import au.com.belong.phone.management.model.PhoneNumberType;
import au.com.belong.phone.management.service.PhoneNumbersService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(PhoneNumbersController.class)
class BasePhoneNumbersControllerTest {

    protected static final String BASE_ENDPOINT = "/phone-api/phone-numbers";

    protected static final String GET_USER_NUMBER_ENDPOINT = BASE_ENDPOINT + "/customer/";

    protected static final String ACTIVATE_ENDPOINT = BASE_ENDPOINT + "/activate";

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected PhoneNumbersService mockPhoneNumbersService;

    protected List<PhoneNumberDetails> phoneNumberDetailsList;

    @BeforeEach
    void setUp() {
        PhoneNumberDetails phoneNumber1 = new PhoneNumberDetails.Builder()
                .phoneNumber("0411111111")
                .phoneNumberType(PhoneNumberType.MOBILE)
                .phoneNumberStatus(PhoneNumberStatus.ASSIGNED)
                .active(true)
                .build();
        PhoneNumberDetails phoneNumber2 = new PhoneNumberDetails.Builder()
                .phoneNumber("0422222222")
                .phoneNumberType(PhoneNumberType.WORK)
                .phoneNumberStatus(PhoneNumberStatus.ASSIGNED)
                .active(true)
                .build();

        phoneNumberDetailsList = List.of(phoneNumber1, phoneNumber2);
    }

}
