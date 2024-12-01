package com.actvn.Shopee_BE.dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    HttpStatus status;
    String message;
    final LocalDateTime timestamp = LocalDateTime.now();
    T result;
}
