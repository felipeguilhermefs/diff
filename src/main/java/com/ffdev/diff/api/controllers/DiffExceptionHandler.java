package com.ffdev.diff.api.controllers;

import com.ffdev.diff.api.dtos.ErrorDTO;
import com.ffdev.diff.api.enums.ErrorCode;
import com.ffdev.diff.domain.enums.DiffSide;
import com.ffdev.diff.domain.exceptions.CustomException;
import com.ffdev.diff.domain.exceptions.DiffSideNotFoundException;
import com.ffdev.diff.domain.exceptions.InvalidBase64Exception;
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
    public ErrorDTO handleDiffSideNotFoundException(DiffSideNotFoundException ex) {
        return new ErrorDTO(errorCode(ex), ex.getMessage());
    }

    @ExceptionHandler(InvalidBase64Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleInvalidBase64Exception(InvalidBase64Exception ex) {
        return new ErrorDTO(errorCode(ex), ex.getMessage());
    }

    private ErrorCode errorCode(CustomException ex) {
        if (ex instanceof DiffSideNotFoundException err) {
            return err.getSide().equals(DiffSide.LEFT)
                    ? ErrorCode.LEFT_NOT_FOUND
                    : ErrorCode.RIGHT_NOT_FOUND;
        }

        if (ex instanceof InvalidBase64Exception) {
            return ErrorCode.BASE64_INVALID;
        }

        return ErrorCode.UNKNOWN;
    }
}
