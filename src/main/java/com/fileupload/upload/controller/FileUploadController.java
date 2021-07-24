package com.fileupload.upload.controller;

import com.fileupload.upload.model.File;
import com.fileupload.upload.response.Message;
import com.fileupload.upload.service.FileService;
import com.fileupload.upload.utils.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/excel")
public class FileUploadController {

    protected static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    private final FileService fileService;

    public FileUploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public Message uploadFile(@RequestParam ("file")MultipartFile[] files) {

        String uploadedFileName = Arrays.stream(files).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(","));

        if (StringUtils.isEmpty(uploadedFileName)) {
            return new Message(uploadedFileName, "Please upload a valid file");
        }

        String notExcelFiles = Arrays.stream(files).filter(x -> !ExcelUtil.hasExcelFormat(x))
                .map(x -> x.getOriginalFilename())
                .collect(Collectors.joining(" , "));

        if (!StringUtils.isEmpty(notExcelFiles)) {
            return new Message(uploadedFileName, "Not an excel file, Please upload an excel file");
        }

        try {
            for (MultipartFile file : files) {
                log.info("entered the for loop in the try block");
                fileService.save(file);
                log.info("file saved");
            }
            return new Message(uploadedFileName, "File uploaded successfully");
        } catch (Exception exception) {
            return new Message(uploadedFileName, "File upload not successful");
        }
    }

    @GetMapping("/get")
    public List<File> getAll(){
        return fileService.getAllFiles();
    }

}
