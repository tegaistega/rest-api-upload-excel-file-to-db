package com.fileupload.upload.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(generator = "uuid" )
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(length = 6000)
    private String fileName;

    @Column(length = 500)
    private String fileType;

    @Column(length = 5000)
    private String fileDescription;

    private boolean published;

    public File(String fileName, String fileType, String fileDescription, boolean published){
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileDescription = fileDescription;
        this.published = published;
    }

}
