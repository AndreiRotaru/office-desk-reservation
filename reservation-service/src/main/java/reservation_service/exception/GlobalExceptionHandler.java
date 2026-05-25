package reservation_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidReservationDateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDate(InvalidReservationDateException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 400, LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserCustomException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserCustomException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 400, LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<ErrorResponse> handleReservationException(ReservationException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 400, LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
