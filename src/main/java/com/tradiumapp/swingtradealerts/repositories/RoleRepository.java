package com.tradiumapp.swingtradealerts.repositories;

import com.tradiumapp.swingtradealerts.models.User;
import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RoleRepository extends PagingAndSortingRepository<User, ObjectId> {
    List<User> findByIdIn(List<String> ids);

}
