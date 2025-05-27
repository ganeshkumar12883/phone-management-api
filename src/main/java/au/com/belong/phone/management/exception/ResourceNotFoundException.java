package au.com.belong.phone.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceNotFoundException extends ResponseStatusException {

    private final String errorCode;

    public ResourceNotFoundException(String errorCode, String reason) {
        super(HttpStatus.NOT_FOUND, reason);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
