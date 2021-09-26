package com.tradiumapp.swingtradealerts.auth.service.shared;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.tradiumapp.swingtradealerts.auth.firebase.FirebaseTokenHolder;
import com.tradiumapp.swingtradealerts.auth.service.exception.FirebaseTokenInvalidException;
import org.apache.commons.lang3.StringUtils;

public class FirebaseParser {
	public FirebaseTokenHolder parseToken(String idToken) {
		if (StringUtils.isBlank(idToken)) {
			throw new IllegalArgumentException("FirebaseTokenBlank");
		}
		try {
			FirebaseToken decodedToken  = FirebaseAuth.getInstance().verifyIdToken(idToken);

			return new FirebaseTokenHolder(decodedToken);
		} catch (Exception e) {
			throw new FirebaseTokenInvalidException(e.getMessage());
		}
	}
}
