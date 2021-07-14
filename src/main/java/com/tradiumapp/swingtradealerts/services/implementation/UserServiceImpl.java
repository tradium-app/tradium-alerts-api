package com.tradiumapp.swingtradealerts.services.implementation;


import com.tradiumapp.swingtradealerts.models.User;
import com.tradiumapp.swingtradealerts.repositories.UserRepository;
import com.tradiumapp.swingtradealerts.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findByIdIn(List<String> ids) {
        return userRepository.findByIdIn(ids);
    }

    @Override
    public List<User> findAllUsers() {
        return (ArrayList) userRepository.findAll();
    }

    @Override
    public User findOneById(ObjectId id) {
//        return userRepository.findOne(id);
        return null;
    }
}
