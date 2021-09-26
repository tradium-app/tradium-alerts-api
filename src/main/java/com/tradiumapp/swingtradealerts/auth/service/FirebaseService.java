package com.tradiumapp.swingtradealerts.auth.service;

import com.tradiumapp.swingtradealerts.auth.firebase.FirebaseTokenHolder;

public interface FirebaseService {

	FirebaseTokenHolder parseToken(String idToken);

}
