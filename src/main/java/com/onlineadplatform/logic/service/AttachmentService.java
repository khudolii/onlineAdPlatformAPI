package com.onlineadplatform.logic.service;

import com.onlineadplatform.logic.exceptions.ADAppException;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import com.onlineadplatform.logic.AppConstants;
import com.onlineadplatform.logic.entities.Attachment;
import com.onlineadplatform.logic.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    public static final Logger logger = LoggerFactory.getLogger(AttachmentService.class);

    @Autowired
    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    public Attachment uploadAttachment(MultipartFile file) throws ADAppException {
        try {
            String fileName = saveAttachment(file);
            String fileDownloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path(AppConstants.FILE_DOWNLOAD_PATH).path(fileName != null ? fileName : new Date().toString()).toUriString();
            Attachment attachment = new Attachment();
            attachment.setAttachmentName(fileName);
            attachment.setAttachmentDownloadUri(fileDownloadUrl);
            attachment.setSize(file.getSize());
            attachment.setAttachmentType(file.getContentType());
            return attachmentRepository.save(attachment);
        } catch (Exception e) {
            logger.error("uploadAttachment" + e);
            throw new ADAppException("Something went wrong! File didn't save!");
        }

    }

    public String saveAttachment(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        if (ObjectUtils.isEmpty(originalFilename)) {
            originalFilename = String.valueOf(new Date().getTime());
        }
        String fileName = StringUtils.cleanPath(originalFilename);
        try {
            if (fileName.contains("..")) {
                throw new InvalidFileNameException(fileName, "File name contains invalid symbols");
            }

            Path fileLocation = Paths.get(AppConstants.FILE_UPLOAD_DIR).toAbsolutePath().normalize();
            fileLocation = fileLocation.resolve(fileName);
            Files.copy(file.getInputStream(), fileLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (Exception e) {
            logger.error("saveAttachment: " + e);
            throw new Exception("File not saved!");
        }
    }

    public Resource findAttachment(String fileName) throws ADAppException {
        try {
            Path fileLocation = Paths.get(AppConstants.FILE_UPLOAD_DIR).toAbsolutePath().normalize();
            fileLocation = fileLocation.resolve(fileName).normalize();

            Resource resource = new UrlResource(fileLocation.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found! - " + fileName);
            }
        } catch (Exception e){
            logger.error("findAttachment: " + e);
            throw new ADAppException("Something went wrong. File not found!");
        }
    }
}
