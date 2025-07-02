package au.com.belong.phone.management.unitTests.controller;

import au.com.belong.phone.management.controller.PhoneNumbersController;
import au.com.belong.phone.management.dto.response.CustomerPhoneNumbersResponse;
import au.com.belong.phone.management.dto.response.PhoneNumbersResponse;
import au.com.belong.phone.management.model.PhoneNumberDetails;
import au.com.belong.phone.management.model.PhoneNumberStatus;
import au.com.belong.phone.management.model.PhoneNumberType;
import au.com.belong.phone.management.service.PhoneNumbersService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    protected Page<PhoneNumbersResponse> pnResponsePage;

    protected Page<CustomerPhoneNumbersResponse> cpnResponsePage;

    protected Pageable pnPageable = PageRequest.of(
            0, 10, Sort.by("phoneNumber").ascending());

    protected Pageable cpnPageable = PageRequest.of(
            0, 10, Sort.by("customerName").ascending().
                    and(Sort.by("phoneNumber").descending()));

    @BeforeEach
    void setUp() {
        PhoneNumbersResponse pnResponse1 = new PhoneNumbersResponse(
                "0411111111", PhoneNumberType.MOBILE, PhoneNumberStatus.ASSIGNED, true);
        PhoneNumbersResponse pnResponse2 = new PhoneNumbersResponse(
                "0411111112", PhoneNumberType.WORK, PhoneNumberStatus.ASSIGNED, true);
        PhoneNumbersResponse pnResponse3 = new PhoneNumbersResponse(
                "0411111113", PhoneNumberType.MOBILE, PhoneNumberStatus.ASSIGNED, true);
        PhoneNumbersResponse pnResponse4 = new PhoneNumbersResponse(
                "0411111114", PhoneNumberType.WORK, PhoneNumberStatus.ASSIGNED, true);
        PhoneNumbersResponse pnResponse5 = new PhoneNumbersResponse(
                "0411111115", PhoneNumberType.MOBILE, PhoneNumberStatus.ASSIGNED, false);
        PhoneNumbersResponse pnResponse6 = new PhoneNumbersResponse(
                "0411111116", PhoneNumberType.MOBILE, PhoneNumberStatus.ASSIGNED, false);
        PhoneNumbersResponse pnResponse7 = new PhoneNumbersResponse(
                "0411111117", null, PhoneNumberStatus.AVAILABLE, false);
        PhoneNumbersResponse pnResponse8 = new PhoneNumbersResponse(
                "0411111118", null, PhoneNumberStatus.AVAILABLE, false);
        PhoneNumbersResponse pnResponse9 = new PhoneNumbersResponse(
                "0411111119", null, PhoneNumberStatus.AVAILABLE, false);
        PhoneNumbersResponse pnResponse10 = new PhoneNumbersResponse(
                "0411111120", null, PhoneNumberStatus.AVAILABLE, false);
        PhoneNumbersResponse pnResponse11 = new PhoneNumbersResponse(
                "0411111121", null, PhoneNumberStatus.AVAILABLE, false);
        PhoneNumbersResponse pnResponse12 = new PhoneNumbersResponse(
                "0411111122", null, PhoneNumberStatus.AVAILABLE, false);
        PhoneNumbersResponse pnResponse13 = new PhoneNumbersResponse(
                "0411111123", null, PhoneNumberStatus.ASSIGNED, false);
        PhoneNumbersResponse pnResponse14 = new PhoneNumbersResponse(
                "0411111124", null, PhoneNumberStatus.ASSIGNED, false);
        PhoneNumbersResponse pnResponse15 = new PhoneNumbersResponse(
                "0411111125", null, PhoneNumberStatus.ASSIGNED, false);

        List<PhoneNumbersResponse> pnResponseList = List.of(pnResponse1, pnResponse2, pnResponse3, pnResponse4,
                pnResponse5, pnResponse6, pnResponse7, pnResponse8, pnResponse9, pnResponse10, pnResponse11,
                pnResponse12, pnResponse13, pnResponse14, pnResponse15);

        pnResponsePage = new PageImpl<>(pnResponseList, pnPageable, pnResponseList.size());

        CustomerPhoneNumbersResponse cpnResponse1 = new CustomerPhoneNumbersResponse(
                "John Smith", "john.smith@example.com",
                List.of(pnResponse1, pnResponse2));
        CustomerPhoneNumbersResponse cpnResponse2 = new CustomerPhoneNumbersResponse(
                "Jane Doe", "jane.doe@example.com",
                List.of(pnResponse3));
        CustomerPhoneNumbersResponse cpnResponse3 = new CustomerPhoneNumbersResponse(
                "John Williams", "john.williams@example.com",
                List.of(pnResponse4));
        CustomerPhoneNumbersResponse cpnResponse4 = new CustomerPhoneNumbersResponse(
                "Luke Paul", "luke.paul@example.com",
                List.of(pnResponse5));
        CustomerPhoneNumbersResponse cpnResponse5 = new CustomerPhoneNumbersResponse(
                "Reba John", "reba.john@example.com",
                List.of(pnResponse6));

        List<CustomerPhoneNumbersResponse> cpnResponseList = List.of(
                cpnResponse1, cpnResponse2, cpnResponse3, cpnResponse4, cpnResponse5);

        cpnResponsePage = new PageImpl<>(cpnResponseList, cpnPageable, cpnResponseList.size());
    }

}
