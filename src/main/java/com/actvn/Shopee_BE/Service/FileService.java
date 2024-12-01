package com.actvn.Shopee_BE.Service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadImage(String path, MultipartFile file);

}
