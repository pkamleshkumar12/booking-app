package com.airasia.booking.controller;

import com.airasia.booking.api.HotelsApiController;
import com.airasia.booking.api.ReservationsApiController;
import com.airasia.booking.api.RoomsApiController;
import com.airasia.booking.exceptions.RoomNotAvailableException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.text.MessageFormat;

@RestControllerAdvice(assignableTypes = {HotelsApiController.class, ReservationsApiController.class, RoomsApiController.class})
public class ReservationApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        val bindingResult = ex.getBindingResult();
        val fieldError = bindingResult.getFieldError();
        val fieldName = fieldError.getField()
                .split("\\.");

        return ResponseEntity.badRequest()
                .body(new ApiError(HttpStatus.BAD_REQUEST, MessageFormat.format("{0} {1}",
                        fieldName[fieldName.length - 1],
                        fieldError.getDefaultMessage())));
    }


    @ExceptionHandler({RoomNotAvailableException.class})
    public ResponseEntity<Object> handleRoomNotAvailable(RoomNotAvailableException ex,
                                                                     WebRequest request) {

        val roomId = ex.getRoomId();
        val hotelId = ex.getHotelId();

        return ResponseEntity.badRequest()
                .body(new ApiError(HttpStatus.BAD_REQUEST,
                        MessageFormat.format("Room is not available at Hotel Id: {0} RoomUd: {1}",
                                hotelId, roomId)));
    }


    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
                                                                     WebRequest request) {
        for (val violation : ex.getConstraintViolations()) {
            return ResponseEntity.badRequest()
                    .body(new ApiError(HttpStatus.BAD_REQUEST, MessageFormat.format("{0} {1}",
                            violation.getPropertyPath().toString(),
                            violation.getMessage())));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        logger.error(ex.getLocalizedMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage()));
    }

    @Builder
    @Getter
    public static class ApiError {
        private String error;
        private Integer status;

        @JsonProperty("error_description")
        private String errorDescription;

        public ApiError(HttpStatus httpStatus, String errorDescription) {
            this.error = httpStatus.getReasonPhrase();
            this.status = httpStatus.value();
            this.errorDescription = errorDescription;
        }

        public ApiError(String error, Integer status, String errorDescription) {
            this.error = error;
            this.status = status;
            this.errorDescription = errorDescription;
        }
    }
}
