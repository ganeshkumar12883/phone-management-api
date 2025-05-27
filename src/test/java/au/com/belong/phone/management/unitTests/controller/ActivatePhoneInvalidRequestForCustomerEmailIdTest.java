package au.com.belong.phone.management.unitTests.controller;

import au.com.belong.phone.management.controller.PhoneNumbersController;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_EMAIL_ID_BLANK_MESSAGE;
import static au.com.belong.phone.management.constants.PhoneManagementConstants.CUSTOMER_EMAIL_ID_FORMAT_MESSAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhoneNumbersController.class)
public class ActivatePhoneInvalidRequestForCustomerEmailIdTest extends BasePhoneNumbersControllerTest {

    @ParameterizedTest
    @MethodSource("invalidCustomerEmailIdActivationRequests")
    void activatePhoneNumberForCustomer_testInvalidPhoneActivationRequest(
            String requestBody, String expectedMessage) throws Exception {
        MvcResult result = mockMvc.perform(post(ACTIVATE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        if (!response.contains(expectedMessage)) {
            throw new AssertionError("Expected message not found: "
                    + expectedMessage + "\nResponse: " + response);
        }
    }

    static List<Arguments> invalidCustomerEmailIdActivationRequests() {
        return List.of(
                Arguments.of(
                        """
                                {
                                  "phoneNumber": "0422222222",
                                  "phoneNumberType": "MOBILE",
                                  "customerName": "John Smith",
                                  "customerEmailId": null
                                }
                                """,
                        CUSTOMER_EMAIL_ID_BLANK_MESSAGE
                ),
                Arguments.of(
                        """
                                {
                                  "phoneNumber": "0422222222",
                                  "phoneNumberType": "MOBILE",
                                  "customerName": "John Smith",
                                  "customerEmailId": ""
                                }
                                """,
                        CUSTOMER_EMAIL_ID_BLANK_MESSAGE
                ),
                Arguments.of(
                        """
                                {
                                  "phoneNumber": "0422222222",
                                  "phoneNumberType": "MOBILE",
                                  "customerName": "John Smith",
                                  "customerEmailId": "  "
                                }
                                """,
                        CUSTOMER_EMAIL_ID_BLANK_MESSAGE
                ),
                Arguments.of(
                        """
                                {
                                  "phoneNumber": "0422222222",
                                  "phoneNumberType": "MOBILE",
                                  "customerName": "John Smith",
                                  "customerEmailId": "john.smith"
                                }
                                """,
                        CUSTOMER_EMAIL_ID_FORMAT_MESSAGE
                ),
                Arguments.of(
                        """
                                {
                                  "phoneNumber": "0422222222",
                                  "phoneNumberType": "MOBILE",
                                  "customerName": "John Smith",
                                  "customerEmailId": "john.smith#example.com"
                                }
                                """,
                        CUSTOMER_EMAIL_ID_FORMAT_MESSAGE
                ),
                Arguments.of(
                        """
                                {
                                  "phoneNumber": "0422222222",
                                  "phoneNumberType": "MOBILE",
                                  "customerName": "John Smith",
                                  "customerEmailId": ".com@john.smith"
                                }
                                """,
                        CUSTOMER_EMAIL_ID_FORMAT_MESSAGE
                ),
                Arguments.of(
                        """
                                {
                                  "phoneNumber": "0422222222",
                                  "phoneNumberType": "MOBILE",
                                  "customerName": "John Smith",
                                  "customerEmailId": "john.smith@@example.com"
                                }
                                """,
                        CUSTOMER_EMAIL_ID_FORMAT_MESSAGE
                ),
                Arguments.of(
                        """
                                {
                                  "phoneNumber": "0422222222",
                                  "phoneNumberType": "MOBILE",
                                  "customerName": "John Smith",
                                  "customerEmailId": "john.smith@example..com"
                                }
                                """,
                        CUSTOMER_EMAIL_ID_FORMAT_MESSAGE
                )
        );
    }
}
