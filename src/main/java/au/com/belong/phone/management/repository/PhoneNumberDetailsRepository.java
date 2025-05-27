package au.com.belong.phone.management.repository;

import au.com.belong.phone.management.model.PhoneNumberStatus;
import au.com.belong.phone.management.model.PhoneNumberDetails;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneNumberDetailsRepository extends JpaRepository<PhoneNumberDetails, Long> {

    List<PhoneNumberDetails> findAll();

    Optional<PhoneNumberDetails> findPhoneDetailsByPhoneNumber(String phoneNumber);

    @Query("SELECT cp.phoneNumberDetails FROM CustomerPhoneNumberMapping cp " +
            "WHERE cp.customerDetails.customerDetailsId = :customerId")
    List<PhoneNumberDetails> findPhoneNumbersByCustomerId(Long customerId);

    @Modifying
    @Transactional
    @Query("UPDATE PhoneNumberDetails pn SET pn.active = TRUE, pn.phoneNumberStatus = :status " +
            "WHERE pn.phoneNumber = :phoneNumber")
    int activatePhoneNumber(String phoneNumber, PhoneNumberStatus status);
}
