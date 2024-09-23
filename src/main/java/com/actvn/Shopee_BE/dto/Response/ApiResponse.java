package com.actvn.Shopee_BE.dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@Builder
public class ApiResponse {
    int status = 200;
    String message;
    final LocalDateTime timestamp = LocalDateTime.now();
    List<CategoryResponse> result;
}
