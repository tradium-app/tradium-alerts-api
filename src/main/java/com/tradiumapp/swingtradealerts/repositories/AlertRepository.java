
package com.tradiumapp.swingtradealerts.repositories;

import com.tradiumapp.swingtradealerts.models.Alert;
import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AlertRepository extends PagingAndSortingRepository<Alert, ObjectId> {
}
