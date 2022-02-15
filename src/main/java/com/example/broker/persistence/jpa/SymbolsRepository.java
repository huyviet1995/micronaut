package com.example.broker.persistence.jpa;
import com.example.broker.model.SymbolEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface SymbolsRepository extends CrudRepository<SymbolEntity, String> {
    @Override
    List<SymbolEntity> findAll();
}
