package com.actvn.Shopee_BE.dto.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PageResponse<T> {
    List<T> context;
    int pageNumber;
    int pageSize;
    long totalElements;
    int totalPages;
    boolean lastPage;
    boolean firstPage;
}
