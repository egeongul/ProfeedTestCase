package com.testCase.testCase.Repository;

import com.testCase.testCase.Model.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface CurrencyRepo extends MongoRepository<Currency, String> {

    List<Currency> findByTypeAndSource(String type, String source);
    List<Currency> findByTypeOrderByDateDesc(String type);
    Page<Currency> findByTypeAndSource(String type, String source, Pageable pageable);
}
