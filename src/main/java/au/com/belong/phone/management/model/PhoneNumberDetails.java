package au.com.belong.phone.management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "PHONE_NUMBER_DETAILS",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"PHONE_NUMBER"}
        )
)
@EntityListeners(AuditingEntityListener.class)
public class PhoneNumberDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PHONE_NUMBER_DETAILS_ID")
    private Long phoneNumberDetailsId;

    @NotNull
    @Column(name="PHONE_NUMBER")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name="PHONE_NUMBER_TYPE")
    private PhoneNumberType phoneNumberType;

    @Enumerated(EnumType.STRING)
    @Column(name="PHONE_NUMBER_STATUS")
    private PhoneNumberStatus phoneNumberStatus;

    @Column(name="IS_ACTIVE")
    private Boolean active;

    @CreatedDate
    @Column(name = "CREATED_DATE_TIME", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @Column(name = "UPDATED_DATE_TIME", nullable = false)
    private LocalDateTime updatedDateTime;

    protected PhoneNumberDetails() {
    }

    private PhoneNumberDetails(Builder builder) {
        this.phoneNumberDetailsId = builder.phoneNumberDetailsId;
        this.phoneNumber = builder.phoneNumber;
        this.phoneNumberType = builder.phoneNumberType;
        this.phoneNumberStatus = builder.phoneNumberStatus;
        this.active = builder.active;
    }

    public Long getPhoneNumberDetailsId() {
        return phoneNumberDetailsId;
    }

    public void setPhoneNumberDetailsId(Long phoneNumberDetailsId) {
        this.phoneNumberDetailsId = phoneNumberDetailsId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PhoneNumberType getPhoneNumberType() {
        return phoneNumberType;
    }

    public void setPhoneNumberType(PhoneNumberType phoneNumberType) {
        this.phoneNumberType = phoneNumberType;
    }

    public PhoneNumberStatus getPhoneNumberStatus() {
        return phoneNumberStatus;
    }

    public void setPhoneNumberStatus(PhoneNumberStatus phoneNumberStatus) {
        this.phoneNumberStatus = phoneNumberStatus;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    // Builder
    public static class Builder {
        private Long phoneNumberDetailsId;
        private String phoneNumber;
        private PhoneNumberType phoneNumberType;
        private PhoneNumberStatus phoneNumberStatus;
        private Boolean active;

        public Builder phoneNumberDetailsId(Long phoneNumberDetailsId) {
            this.phoneNumberDetailsId = phoneNumberDetailsId;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder phoneNumberType(PhoneNumberType phoneNumberType) {
            this.phoneNumberType = phoneNumberType;
            return this;
        }

        public Builder phoneNumberStatus(PhoneNumberStatus phoneNumberStatus) {
            this.phoneNumberStatus = phoneNumberStatus;
            return this;
        }

        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        public PhoneNumberDetails build() {
            return new PhoneNumberDetails(this);
        }
    }
}
