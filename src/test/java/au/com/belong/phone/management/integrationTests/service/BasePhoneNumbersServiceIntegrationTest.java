package au.com.belong.phone.management.integrationTests.service;

import au.com.belong.phone.management.model.CustomerDetails;
import au.com.belong.phone.management.model.CustomerPhoneNumberMapping;
import au.com.belong.phone.management.model.PhoneNumberDetails;
import au.com.belong.phone.management.model.PhoneNumberStatus;
import au.com.belong.phone.management.model.PhoneNumberType;
import au.com.belong.phone.management.repository.CustomerDetailsRepository;
import au.com.belong.phone.management.repository.CustomerPhoneNumberMappingRepository;
import au.com.belong.phone.management.repository.PhoneNumberDetailsRepository;
import au.com.belong.phone.management.service.PhoneNumbersService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("test") // Uses application-test.yml
@Transactional
class BasePhoneNumbersServiceIntegrationTest {

    @Autowired
    protected PhoneNumbersService phoneNumbersService;

    @Autowired
    protected PhoneNumberDetailsRepository phoneNumberRepository;

    @Autowired
    protected CustomerDetailsRepository customerDetailsRepository;

    @Autowired
    protected CustomerPhoneNumberMappingRepository customerPhoneNumberMappingRepository;

    @BeforeEach
    void setUp() {
        // Optional: manually insert test data via repositories
        phoneNumberRepository.save(new PhoneNumberDetails.Builder()
                .phoneNumber("0411111111")
                .phoneNumberType(PhoneNumberType.MOBILE)
                .phoneNumberStatus(PhoneNumberStatus.ASSIGNED)
                .active(true)
                .build());
        phoneNumberRepository.save(new PhoneNumberDetails.Builder()
                .phoneNumber("0422222222")
                .phoneNumberType(PhoneNumberType.WORK)
                .phoneNumberStatus(PhoneNumberStatus.ASSIGNED)
                .active(true)
                .build());
        phoneNumberRepository.save(new PhoneNumberDetails.Builder()
                .phoneNumber("0433333333")
                .phoneNumberType(PhoneNumberType.MOBILE)
                .phoneNumberStatus(PhoneNumberStatus.ASSIGNED)
                .active(true)
                .build());
        phoneNumberRepository.save(new PhoneNumberDetails.Builder()
                .phoneNumber("0444444444")
                .phoneNumberType(PhoneNumberType.MOBILE)
                .phoneNumberStatus(PhoneNumberStatus.ASSIGNED)
                .active(false)
                .build());
        phoneNumberRepository.save(new PhoneNumberDetails.Builder()
                .phoneNumber("0455555555")
                .phoneNumberStatus(PhoneNumberStatus.AVAILABLE)
                .active(false)
                .build());
        phoneNumberRepository.save(new PhoneNumberDetails.Builder()
                .phoneNumber("0466666666")
                .phoneNumberStatus(PhoneNumberStatus.AVAILABLE)
                .active(false)
                .build());
        phoneNumberRepository.save(new PhoneNumberDetails.Builder()
                .phoneNumber("0477777777")
                .phoneNumberStatus(PhoneNumberStatus.AVAILABLE)
                .active(false)
                .build());
        phoneNumberRepository.save(new PhoneNumberDetails.Builder()
                .phoneNumber("0488888888")
                .phoneNumberStatus(PhoneNumberStatus.DORMANT)
                .active(false)
                .build());
        phoneNumberRepository.save(new PhoneNumberDetails.Builder()
                .phoneNumber("0499999999")
                .phoneNumberStatus(PhoneNumberStatus.DORMANT)
                .active(false)
                .build());
    }

    protected void createCustomersAndMapping() {
        customerDetailsRepository.save(new CustomerDetails.Builder()
                .customerName("Jane Doe")
                .customerEmailId("jane.doe@example.com")
                .build());

        customerDetailsRepository.save(new CustomerDetails.Builder()
                .customerName("Jane Citizen")
                .customerEmailId("jane.citizen@example.com")
                .build());

        customerDetailsRepository.save(new CustomerDetails.Builder()
                .customerName("Brian O Connor")
                .customerEmailId("brian.o.connor@example.com")
                .build());

        customerDetailsRepository.save(new CustomerDetails.Builder()
                .customerName("John Smith")
                .customerEmailId("john.smith@example.com")
                .build());

        CustomerPhoneNumberMapping cpnMapping1 = new CustomerPhoneNumberMapping
                .Builder()
                .customerDetails(
                        customerDetailsRepository.findByCustomerNameIgnoreCase("Jane Doe").get())
                .phoneNumberDetails(
                        phoneNumberRepository.findPhoneDetailsByPhoneNumber("0411111111").get())
                .build();
        customerPhoneNumberMappingRepository.save(cpnMapping1);

        CustomerPhoneNumberMapping cpnMapping2 = new CustomerPhoneNumberMapping
                .Builder()
                .customerDetails(
                        customerDetailsRepository.findByCustomerNameIgnoreCase("Jane Doe").get())
                .phoneNumberDetails(
                        phoneNumberRepository.findPhoneDetailsByPhoneNumber("0422222222").get())
                .build();
        customerPhoneNumberMappingRepository.save(cpnMapping2);

        CustomerPhoneNumberMapping cpnMapping3 = new CustomerPhoneNumberMapping
                .Builder()
                .customerDetails(
                        customerDetailsRepository.findByCustomerNameIgnoreCase("Jane Citizen").get())
                .phoneNumberDetails(
                        phoneNumberRepository.findPhoneDetailsByPhoneNumber("0433333333").get())
                .build();
        customerPhoneNumberMappingRepository.save(cpnMapping3);

        CustomerPhoneNumberMapping cpnMapping4 = new CustomerPhoneNumberMapping
                .Builder()
                .customerDetails(
                        customerDetailsRepository.findByCustomerNameIgnoreCase("Brian O Connor").get())
                .phoneNumberDetails(
                        phoneNumberRepository.findPhoneDetailsByPhoneNumber("0444444444").get())
                .build();
        customerPhoneNumberMappingRepository.save(cpnMapping4);
    }
}