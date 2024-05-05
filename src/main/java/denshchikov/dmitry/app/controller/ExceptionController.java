package denshchikov.dmitry.app.controller;

import denshchikov.dmitry.app.model.response.common.ErrorResponse;
import denshchikov.dmitry.app.model.response.common.ErrorResponse.ErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static denshchikov.dmitry.app.constant.ErrorCode.TECHNICAL_ERROR;

@Slf4j
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(Exception e) {
        log.error(e.getMessage(), e);

        ErrorDetails errorDetails = new ErrorDetails(TECHNICAL_ERROR.getCode(), TECHNICAL_ERROR.getDescription());
        ErrorResponse errorResponse = new ErrorResponse(errorDetails);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}