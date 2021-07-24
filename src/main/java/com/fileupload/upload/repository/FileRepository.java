package com.fileupload.upload.repository;

import com.fileupload.upload.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
}
