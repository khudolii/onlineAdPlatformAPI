package com.onlineadplatform.logic.controller;

import com.onlineadplatform.logic.entities.Attachment;
import com.onlineadplatform.logic.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class AttachmentController {
    final
    AttachmentRepository attachmentRepository;

    public AttachmentController(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @GetMapping("/run")
    public void runTest() {
        Attachment attachment = new Attachment();
        attachment.setAttachmentName("TES2T");
        attachment.setAttachmentType("typ3e");
        attachment.setAttachmentDownloadUri("testu3ri");
        attachmentRepository.save(attachment);
        attachmentRepository.findAll().stream().map(Attachment::toString).forEach(System.out::println);
    }
}
