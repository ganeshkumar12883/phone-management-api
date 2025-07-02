package au.com.belong.phone.management.repository;

import au.com.belong.phone.management.model.PhoneNumberDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneNumberDetailsRepository extends JpaRepository<PhoneNumberDetails, Long> {

    Page<PhoneNumberDetails> findAll(Pageable pageable);

    Optional<PhoneNumberDetails> findPhoneDetailsByPhoneNumber(String phoneNumber);
}
