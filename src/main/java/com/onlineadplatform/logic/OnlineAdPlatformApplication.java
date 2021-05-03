package com.onlineadplatform.logic;

import com.onlineadplatform.logic.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Map;

@SpringBootApplication
public class OnlineAdPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineAdPlatformApplication.class, args);

    }

}
