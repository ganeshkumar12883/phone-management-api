package au.com.belong.phone.management.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "CUSTOMER_PHONE_MAPPING")
@EntityListeners(AuditingEntityListener.class)
public class CustomerPhoneNumberMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_DETAILS_ID")
    private CustomerDetails customerDetails;

    @ManyToOne
    @JoinColumn(name = "PHONE_NUMBER_DETAILS_ID")
    private PhoneNumberDetails phoneNumberDetails;

    @CreatedDate
    @Column(name = "CREATED_DATE_TIME", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @Column(name = "UPDATED_DATE_TIME", nullable = false)
    private LocalDateTime updatedDateTime;

    protected CustomerPhoneNumberMapping() {}

    private CustomerPhoneNumberMapping(Builder builder) {
        this.customerDetails = builder.customerDetails;
        this.phoneNumberDetails = builder.phoneNumberDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    public PhoneNumberDetails getPhoneNumberDetails() {
        return phoneNumberDetails;
    }

    public void setPhoneNumberDetails(PhoneNumberDetails phoneNumberDetails) {
        this.phoneNumberDetails = phoneNumberDetails;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public static class Builder {
        private CustomerDetails customerDetails;
        private PhoneNumberDetails phoneNumberDetails;

        public Builder customerDetails(CustomerDetails customerDetails) {
            this.customerDetails = customerDetails;
            return this;
        }

        public Builder phoneNumberDetails(PhoneNumberDetails phoneNumberDetails) {
            this.phoneNumberDetails = phoneNumberDetails;
            return this;
        }

        public CustomerPhoneNumberMapping build() {
            return new CustomerPhoneNumberMapping(this);
        }
    }
}
