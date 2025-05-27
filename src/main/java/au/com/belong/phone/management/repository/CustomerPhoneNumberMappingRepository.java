package au.com.belong.phone.management.repository;

import au.com.belong.phone.management.model.CustomerDetails;
import au.com.belong.phone.management.model.CustomerPhoneNumberMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerPhoneNumberMappingRepository extends JpaRepository<CustomerPhoneNumberMapping, Long> {

    @Query("SELECT COUNT(m) > 0 FROM CustomerPhoneNumberMapping m WHERE m.customerDetails.customerDetailsId =" +
            " :customerDetailsId AND m.phoneNumberDetails.phoneNumberDetailsId = :phoneNumberDetailsId")
    boolean customerPhoneNumberMappingExists(Long customerDetailsId, Long phoneNumberDetailsId);

}
