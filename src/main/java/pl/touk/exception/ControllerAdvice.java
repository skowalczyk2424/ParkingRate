package pl.touk.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse notFound(RecordNotFoundException ex) {
        return new ErrorResponse(ex.getLocalizedMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse internal(Throwable ex) {
        return new ErrorResponse(ex.getLocalizedMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse wrongParameter(IllegalArgumentException ex) {
        return new ErrorResponse(ex.getLocalizedMessage());
    }
}
