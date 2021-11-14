
package com.tradiumapp.swingtradealerts.repositories;

import com.tradiumapp.swingtradealerts.models.Alert;
import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AlertRepository extends PagingAndSortingRepository<Alert, ObjectId> {
    List<Alert> findByEnabled(boolean enabled);
    List<Alert> findByUserId(String userId);
}
