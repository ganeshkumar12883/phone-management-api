package au.com.belong.phone.management.repository;

import au.com.belong.phone.management.model.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Long> {

    Optional<CustomerDetails> findByCustomerEmailIdIgnoreCase(String customerEmailId);

    Optional<CustomerDetails> findByCustomerNameIgnoreCase(String customerName);

    List<CustomerDetails> findByCustomerNameContainingIgnoreCase(String customerName);

}
