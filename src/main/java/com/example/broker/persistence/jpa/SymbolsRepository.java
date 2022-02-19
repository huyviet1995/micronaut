package com.example.broker.persistence.jpa;
import com.example.broker.persistence.model.SymbolEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SymbolsRepository extends CrudRepository<SymbolEntity, String> {
    @Override
    List<SymbolEntity> findAll();
}
