package com.tradiumapp.swingtradealerts.mutations;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.tradiumapp.swingtradealerts.models.Response;
import com.tradiumapp.swingtradealerts.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Component
public class LoginMutation implements GraphQLMutationResolver {
    Logger logger = LoggerFactory.getLogger(LoginMutation.class);

    @Value("${SERVICE_ACCOUNT_KEY}")
    private String serviceAccountKey;

    @Autowired
    MongoTemplate mongoTemplate;

//    @PostConstruct
//    public void init() throws IOException {
//        byte[] decodedBytes = Base64.getDecoder().decode(serviceAccountKey);
//        InputStream serviceStream =new ByteArrayInputStream(decodedBytes);
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(new BufferedInputStream(serviceStream)))
//                .build();
//
//        FirebaseApp.initializeApp(options);
//    }

    public Response loginUser(final String accessToken) throws FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(accessToken);
        String firebaseUid = decodedToken.getUid();

        Query query = new Query();
        query.addCriteria(Criteria.where("firebaseUid").is(firebaseUid));
        User user = mongoTemplate.findOne(query, User.class);

        if(user == null){
            user = new User();
            user.firebaseUid = firebaseUid;
            user.name = decodedToken.getName();
            user.email = decodedToken.getEmail();
            user.imageUrl =  decodedToken.getPicture();

            mongoTemplate.save(user);
        }

        return new Response(true, "Login successful.", user);
    }
}
