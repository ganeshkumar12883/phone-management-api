package au.com.belong.phone.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceConflictException extends ResponseStatusException {

    private final String errorCode;

    public ResourceConflictException(String errorCode, String reason){
        super(HttpStatus.CONFLICT, reason);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
