package me.hanwool.mallutilapp.handler;

import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.ErrorResponse;
import me.hanwool.mallutilapp.exception.NotFoundException;
import me.hanwool.mallutilapp.value.ResponseCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = {"me.hanwool.orderservice"})
public class APIExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody
    ErrorResponse handleNotFoundExceptions(NotFoundException ex) {
//    ErrorResponse handleNotFoundExceptions(ServerHttpRequest request, Exception ex) {

        log.debug("handleNotFoundExceptions");
//        return createHttpErrorInfo(ResponseCode.NOT_FOUND, HttpStatus.NOT_FOUND, request, ex);
        return new ErrorResponse(ResponseCode.NOT_FOUND); // test
    }

    @Order(Ordered.LOWEST_PRECEDENCE)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public @ResponseBody
    ErrorResponse reactiveHandleExceptions(ServerHttpRequest request, Exception ex) {
        log.error("reactiveHandleExceptions : {}", ex);
        return createHttpErrorInfo(ResponseCode.FAIL, HttpStatus.INTERNAL_SERVER_ERROR, request, ex);
    }

    private ErrorResponse createHttpErrorInfo(ResponseCode responseCode, HttpStatus httpStatus, ServerHttpRequest request, Exception ex) {
        final String path = request.getURI().getPath();
//        final String path = request.getPath().pathWithinApplication().value();
        final String message = ex.getMessage();

        log.debug("HTTP status: {},path: {}, message: {}", httpStatus, path, message);
        return new ErrorResponse(responseCode);
    }
}
