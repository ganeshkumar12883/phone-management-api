package au.com.belong.phone.management.repository;

import au.com.belong.phone.management.dto.data.CustomerPhoneNumbersResultSet;
import au.com.belong.phone.management.dto.response.PhoneNumbersResponse;
import au.com.belong.phone.management.model.CustomerPhoneNumberMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerPhoneNumberMappingRepository extends JpaRepository<CustomerPhoneNumberMapping, Long> {

    @Query("SELECT COUNT(m) > 0 FROM CustomerPhoneNumberMapping m WHERE m.customerDetails.customerDetailsId =" +
            " :customerDetailsId AND m.phoneNumberDetails.phoneNumberDetailsId = :phoneNumberDetailsId")
    boolean customerPhoneNumberMappingExists(Long customerDetailsId, Long phoneNumberDetailsId);

    @Query("SELECT new au.com.belong.phone.management.dto.response.PhoneNumbersResponse(" +
           "p.phoneNumber, p.phoneNumberType, p.phoneNumberStatus, p.active) " +
           "FROM CustomerPhoneNumberMapping m " +
           "JOIN m.phoneNumberDetails p " +
           "WHERE m.customerDetails.customerDetailsId = :customerId")
    List<PhoneNumbersResponse> findPhoneNumbersResponseByCustomerId(@Param("customerId") Long customerId);

}
