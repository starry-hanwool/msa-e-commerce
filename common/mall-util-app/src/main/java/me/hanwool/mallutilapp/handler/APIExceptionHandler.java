package me.hanwool.mallutilapp.handler;

import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.ErrorResponse;
import me.hanwool.mallutilapp.exception.NotFoundException;
import me.hanwool.mallutilapp.value.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class APIExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody
    ErrorResponse handleNotFoundExceptions(ServerHttpRequest request, Exception ex) {

        return createHttpErrorInfo(ResponseCode.NOT_FOUND, HttpStatus.NOT_FOUND, request, ex);
    }

    private ErrorResponse createHttpErrorInfo(ResponseCode responseCode, HttpStatus httpStatus, ServerHttpRequest request, Exception ex) {
        final String path = request.getPath().pathWithinApplication().value();
        final String message = ex.getMessage();

        log.debug("HTTP status: {},path: {}, message: {}", httpStatus, path, message);
        return new ErrorResponse(responseCode);
    }
}
