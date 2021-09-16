
package com.tradiumapp.swingtradealerts.repositories;

import com.tradiumapp.swingtradealerts.models.Alert;
import com.tradiumapp.swingtradealerts.models.AlertStatus;
import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AlertRepository extends PagingAndSortingRepository<Alert, ObjectId> {
    List<Alert> findByStatusNot(AlertStatus status);
}
