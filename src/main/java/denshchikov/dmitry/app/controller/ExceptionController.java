package denshchikov.dmitry.app.controller;

import denshchikov.dmitry.app.model.response.common.ErrorResponse;
import denshchikov.dmitry.app.model.response.common.ErrorResponse.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static denshchikov.dmitry.app.constant.ErrorCode.TECHNICAL_ERROR;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException() {

        ErrorDetails errorDetails = new ErrorDetails(TECHNICAL_ERROR.getCode(), TECHNICAL_ERROR.getDescription());
        ErrorResponse errorResponse = new ErrorResponse(errorDetails);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}