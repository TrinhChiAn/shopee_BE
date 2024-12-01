package com.actvn.Shopee_BE.exception;

import lombok.Getter;

@Getter
public class NotFoundCategory extends RuntimeException{
    public NotFoundCategory(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode=errorCode;
    }

    ErrorCode errorCode;
}
