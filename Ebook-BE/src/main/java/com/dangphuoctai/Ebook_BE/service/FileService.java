package com.dangphuoctai.Ebook_BE.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadImage(String path, MultipartFile file) throws IOException;

    InputStream getResource(String path, String fileName) throws FileNotFoundException;

    String downloadImageFromUrl(String imageUrl, String folderPath);

    String uploadFilePDF(String path, MultipartFile file) throws IOException;

    Resource getFilePDF(String path, String fileName) throws FileNotFoundException;
}
