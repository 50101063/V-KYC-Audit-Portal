package com.vkyc.auditportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // Enable asynchronous method execution for bulk processing
public class VkycAuditPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(VkycAuditPortalApplication.class, args);
	}

}
