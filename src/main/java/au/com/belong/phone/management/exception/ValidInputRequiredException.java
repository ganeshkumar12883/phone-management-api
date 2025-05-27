package au.com.belong.phone.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidInputRequiredException extends ResponseStatusException {

    private final String errorCode;

    public ValidInputRequiredException(String errorCode, String reason){
        super(HttpStatus.BAD_REQUEST, reason);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
