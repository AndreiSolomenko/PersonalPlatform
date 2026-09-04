package com.personalplatform.repos;

import com.personalplatform.model.TestData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestDataRepository
        extends JpaRepository<TestData, Long> {
}
