package au.com.belong.phone.management.repository;

import au.com.belong.phone.management.dto.data.CustomerPhoneNumbersResultSet;
import au.com.belong.phone.management.model.CustomerDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerDetailsRepository extends JpaRepository<CustomerDetails, Long> {

    Page<CustomerDetails> findByCustomerEmailIdIgnoreCase(String customerEmailId, Pageable pageable);

    Page<CustomerDetails> findByCustomerNameIgnoreCase(String customerName, Pageable pageable);

    Page<CustomerDetails> findByCustomerNameContainingIgnoreCase(String customerName, Pageable pageable);

    @Query("select CustomerDetails from CustomerDetails c where c.customerName = :customerName and " +
           "c.customerEmailId = :customerEmailId")
    Optional<CustomerDetails> findByCustomerNameAndEmailIdIgnoreCase(@Param("customerName") String customerName,
                                                                     @Param("customerEmailId") String customerEmailId);

    /*
     * Main Query serves the purpose as below:
     *  - Returns customer info and phone numbers if both exist.
     *  - Returns customer info even if no phone numbers (to detect "no phones" case).
     *  - Returns nothing if no customer matches (to detect "customer not found").
     *
     * Count Query gives count of customers, even those without phone numbers (to align with the main query)
     * This way, pagination works correctly even if a customer has no phone numbers.
     *
     * Key SQL Concepts to Use
     *  - LEFT JOIN: Keeps customer even if there's no mapping or phone number.
     *  - LIKE on customer name: Allows partial matching.
     *  - TRIM/LOWER usage: Ensures case-insensitive and clean matching.
     */
    @Query(value = """
                SELECT
                    C.CUSTOMER_NAME AS customerName,
                    C.CUSTOMER_EMAIL_ID AS customerEmailId,
                    P.PHONE_NUMBER AS phoneNumber,
                    P.PHONE_NUMBER_TYPE AS phoneNumberType,
                    P.PHONE_NUMBER_STATUS AS phoneNumberStatus,
                    P.IS_ACTIVE AS phoneNumberActive
                FROM CUSTOMER_DETAILS C
                LEFT JOIN CUSTOMER_PHONE_MAPPING M ON C.CUSTOMER_DETAILS_ID = M.CUSTOMER_DETAILS_ID
                LEFT JOIN PHONE_NUMBER_DETAILS P ON M.PHONE_NUMBER_DETAILS_ID = P.PHONE_NUMBER_DETAILS_ID
                WHERE LOWER(C.CUSTOMER_NAME) LIKE LOWER(CONCAT('%', :customerName))
                ORDER BY C.CUSTOMER_NAME ASC, P.PHONE_NUMBER ASC
            """,
            countQuery = """
                SELECT COUNT(DISTINCT C.CUSTOMER_DETAILS_ID) 
                FROM CUSTOMER_DETAILS C
                LEFT JOIN CUSTOMER_PHONE_MAPPING M ON C.CUSTOMER_DETAILS_ID = M.CUSTOMER_DETAILS_ID
                WHERE LOWER(C.CUSTOMER_NAME) LIKE LOWER(CONCAT('%', :customerName))
            """,
            nativeQuery = true)
    Page<CustomerPhoneNumbersResultSet> findCustomerPhoneNumbersResultSet(
            @Param("customerName") String customerName, Pageable pageable);

}
