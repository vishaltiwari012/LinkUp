package com.cw.linkedin.uploader_service.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    String uploadImage(MultipartFile file) throws IOException;
}
