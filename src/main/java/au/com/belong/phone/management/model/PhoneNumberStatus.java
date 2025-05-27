package au.com.belong.phone.management.model;

public enum PhoneNumberStatus {
    AVAILABLE("Available"),
    ASSIGNED("Assigned"),
    DORMANT("Dormant");

    private final String status;

    PhoneNumberStatus(String status) {
        this.status = status;
    }

    // Getter method to retrieve the string representation of the status
    public String getStatus() {
        return status;
    }
}
