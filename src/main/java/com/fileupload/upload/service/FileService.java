package com.fileupload.upload.service;

import com.fileupload.upload.model.File;
import com.fileupload.upload.repository.FileRepository;
import com.fileupload.upload.utils.ExcelUtil;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    protected final Logger log = LoggerFactory.getLogger(FileService.class);

    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";


    public static boolean isExcelFile(MultipartFile multipartFile){
        if (!TYPE.equals(multipartFile.getContentType())){
            return false;
        }
        return true;
    }

    public void save(MultipartFile file) {
        log.info("entered save method - service");
        try {
            log.info("entered try block - service");
            List<File> files = ExcelUtil.excelToFiles(file.getInputStream());
            log.info("created a list of files - service");
            fileRepository.saveAll(files);
            log.info("saved files in database using jpa - service");
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public ByteArrayInputStream load() {
        List<File> files = fileRepository.findAll();

        ByteArrayInputStream in = ExcelUtil.filesToExcel(files);
        return in;
    }

    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

}
