package com.example.broker.persistence.jpa;

import com.example.broker.persistence.model.QuoteDTO;
import com.example.broker.persistence.model.QuoteEntity;
import com.example.broker.persistence.model.SymbolEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuotesRepository extends CrudRepository<QuoteEntity, Integer> {

    @Override
    List<QuoteEntity> findAll();

    Optional<QuoteDTO> findBySymbolValue(String symbol);

    // Ordering
    List<QuoteDTO> listOrderByVolumeDesc();

    List<QuoteDTO> listOrderByVolumeAsc();

    // Filter
    List<QuoteDTO> findByVolumeGreaterThan(BigDecimal volume);
}
