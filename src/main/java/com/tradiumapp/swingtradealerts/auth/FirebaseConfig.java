package com.tradiumapp.swingtradealerts.auth;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.annotation.PostConstruct;

import com.google.auth.oauth2.GoogleCredentials;
import com.tradiumapp.swingtradealerts.mutations.LoginMutation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@Configuration
public class FirebaseConfig {
	Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

	@Value("${SERVICE_ACCOUNT_KEY}")
	private String serviceAccountKey;

	@PostConstruct
	public void init() throws IOException {

		byte[] decodedBytes = Base64.getDecoder().decode(serviceAccountKey);
		InputStream serviceStream =new ByteArrayInputStream(decodedBytes);
		FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(new BufferedInputStream(serviceStream)))
				.build();

		FirebaseApp.initializeApp(options);

		logger.info("FirebaseApp initialized...");
	}
}
