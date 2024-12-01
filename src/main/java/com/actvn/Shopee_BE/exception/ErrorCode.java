package com.actvn.Shopee_BE.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public enum ErrorCode {
    USER_NOT_EXISTED(400,"User not existed")
    ;

    final int code;
    final String message;
}
