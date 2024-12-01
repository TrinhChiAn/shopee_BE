package com.actvn.Shopee_BE.Service.impl;

import com.actvn.Shopee_BE.Service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) {
        // Kiểm tra tên file gốc và lấy phần mở rộng
        String originFileName = file.getOriginalFilename();
        assert originFileName != null;

        // Tạo tên file ngẫu nhiên và giữ phần mở rộng gốc
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId + originFileName.substring(originFileName.lastIndexOf('.'));

        // Xây dựng đường dẫn file
        Path folderPath = Paths.get(path);
        Path filePath = folderPath.resolve(fileName);

        // Tạo thư mục nếu chưa tồn tại
        try {
            Files.createDirectories(folderPath); // Sẽ tạo thư mục kể cả khi nhiều cấp cha con
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder: " + e.getMessage(), e);
        }

        // Lưu tệp vào đường dẫn đã chỉ định
        try {
            Files.copy(file.getInputStream(), filePath);
        } catch (IOException e) {
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
        return filePath.toString();
    }
}
