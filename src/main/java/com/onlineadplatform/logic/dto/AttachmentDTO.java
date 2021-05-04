package com.onlineadplatform.logic.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class AttachmentDTO implements Serializable {
    private String attachmentName;
    private String attachmentDownloadUri;
    private String attachmentType;
    private Long size;
}
