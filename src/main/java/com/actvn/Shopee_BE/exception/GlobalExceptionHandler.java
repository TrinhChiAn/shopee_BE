package com.actvn.Shopee_BE.exception;

import com.actvn.Shopee_BE.dto.Response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleInvalidCredentialsException(MethodArgumentNotValidException methodArgumentNotValidException){

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(methodArgumentNotValidException.getFieldError().getDefaultMessage())
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handleAllException(RuntimeException e, WebRequest webRequest) {

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler(value = NotFoundCategory.class)
    public ResponseEntity<ApiResponse> notFound(NotFoundCategory notFoundCategory){
        ApiResponse apiResponse = ApiResponse.builder()
                .message(notFoundCategory.errorCode.getMessage())
                .status(HttpStatus.valueOf(notFoundCategory.errorCode.getCode()))
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }


    //@ExceptionHandler(value = Exception.class)

}
