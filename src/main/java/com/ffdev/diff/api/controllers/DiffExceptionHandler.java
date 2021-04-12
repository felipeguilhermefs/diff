package com.ffdev.diff.api.controllers;

import com.ffdev.diff.api.dtos.ErrorDTO;
import com.ffdev.diff.api.enums.ErrorCode;
import com.ffdev.diff.domain.enums.DiffSide;
import com.ffdev.diff.domain.exceptions.DiffSideNotFoundException;
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

    private ErrorCode errorCode(DiffSideNotFoundException ex) {
        if (ex.getSide().equals(DiffSide.LEFT)) {
            return ErrorCode.LEFT_NOT_FOUND;
        }

        if (ex.getSide().equals(DiffSide.RIGHT)) {
            return ErrorCode.RIGHT_NOT_FOUND;
        }

        return ErrorCode.UNKNOWN;
    }
}
