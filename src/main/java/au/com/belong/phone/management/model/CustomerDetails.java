package au.com.belong.phone.management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "CUSTOMER_DETAILS")
@EntityListeners(AuditingEntityListener.class)
public class CustomerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_DETAILS_ID")
    private Long customerDetailsId;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUSTOMER_EMAIL_ID")
    private String customerEmailId;

    @CreatedDate
    @Column(name = "CREATED_DATE_TIME", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @Column(name = "UPDATED_DATE_TIME", nullable = false)
    private LocalDateTime updatedDateTime;

    protected CustomerDetails() {}

    private CustomerDetails(Builder builder) {
        this.customerDetailsId = builder.customerDetailsId;
        this.customerName = builder.customerName;
        this.customerEmailId = builder.customerEmailId;
    }

    public Long getCustomerDetailsId() {
        return customerDetailsId;
    }

    public void setCustomerDetailsId(Long customerDetailsId) {
        this.customerDetailsId = customerDetailsId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmailId() {
        return customerEmailId;
    }

    public void setCustomerEmailId(String customerEmailId) {
        this.customerEmailId = customerEmailId;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public static class Builder {
        private Long customerDetailsId;
        private String customerName;
        private String customerEmailId;

        public Builder customerDetailsId(Long customerDetailsId) {
            this.customerDetailsId = customerDetailsId;
            return this;
        }

        public Builder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder customerEmailId(String customerEmailId) {
            this.customerEmailId = customerEmailId;
            return this;
        }

        public CustomerDetails build() {
            return new CustomerDetails(this);
        }
    }
}