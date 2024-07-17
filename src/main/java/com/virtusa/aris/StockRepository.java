package com.virtusa.aris;

import java.util.Collection;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface StockRepository extends ReactiveCrudRepository<Stock, Long> {
    Flux<Stock> findBySymbolIn(Collection<String> symbols);
}
