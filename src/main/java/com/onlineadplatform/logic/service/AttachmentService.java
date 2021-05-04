package com.onlineadplatform.logic.service;

import com.onlineadplatform.logic.entities.Attachment;
import com.onlineadplatform.logic.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;

@Service
public class AttachmentService{

    private final AttachmentRepository attachmentRepository;

    @Autowired
    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    public Attachment uploadAttachment(MultipartFile file){
        String fileName = file.getOriginalFilename();
        String fileDownloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files").path(fileName != null ? fileName : new Date().toString()).toUriString();
        Attachment attachment = new Attachment();
        attachment.setAttachmentName(fileName);
        attachment.setAttachmentDownloadUri(fileDownloadUrl);
        attachment.setSize(file.getSize());
        attachment.setAttachmentType(file.getContentType());

        return attachmentRepository.save(attachment);
    }

}
