package com.fileupload.upload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message extends Throwable {

    private String fileName;

    private String message;

    public Message(String message) {
        this.message = message;
    }
}
