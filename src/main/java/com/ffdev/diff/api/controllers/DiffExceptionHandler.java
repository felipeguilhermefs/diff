package com.ffdev.diff.api.controllers;

import com.ffdev.diff.api.dtos.ErrorResponse;
import com.ffdev.diff.api.enums.ErrorCode;
import com.ffdev.diff.domain.enums.Side;
import com.ffdev.diff.domain.exceptions.DiffSideNotFoundException;
import com.ffdev.diff.domain.exceptions.InvalidBase64Exception;
import com.ffdev.diff.domain.exceptions.InvalidJsonException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DiffExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DiffSideNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleDiffSideNotFoundException(DiffSideNotFoundException ex) {
        var errorCode = ex.getSide().equals(Side.LEFT)
                ? ErrorCode.LEFT_NOT_FOUND
                : ErrorCode.RIGHT_NOT_FOUND;

        return new ErrorResponse(errorCode, ex.getMessage());
    }

    @ExceptionHandler(InvalidBase64Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidBase64Exception(InvalidBase64Exception ex) {
        return new ErrorResponse(ErrorCode.BASE64_INVALID, ex.getMessage());
    }

    @ExceptionHandler(InvalidJsonException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidJsonException(InvalidJsonException ex) {
        return new ErrorResponse(ErrorCode.JSON_INVALID, ex.getMessage());
    }
}
