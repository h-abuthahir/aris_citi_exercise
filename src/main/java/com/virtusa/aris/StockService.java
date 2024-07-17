package com.virtusa.aris;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final Sinks.Many<Stock> sink;
    private final Random random = new Random();
    
    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
        startStockPriceUpdates();
    }

    public Mono<Stock> getStockPrice(String symbol) {
        return stockRepository.findBySymbolIn(List.of(symbol)).single();
    }

    public Flux<Stock> getStockPrices(Collection<String> symbols) {
        return stockRepository.findBySymbolIn(symbols);
    }

    public Flux<Stock> subscribeToStocks(List<String> symbols) {
        return sink.asFlux().filter(stock -> symbols.contains(stock.getSymbol()))
        		.delayElements(Duration.ofSeconds(1));
    }

    private void startStockPriceUpdates() {
	        
        Flux.interval(Duration.ofSeconds(3))
        .flatMap(tick -> stockRepository.findAll()
            .flatMap(stock -> {
                stock.setPrice(stock.getPrice() + (random.nextDouble() - 0.5) * 10);
                return stockRepository.save(stock);
            }))
        .subscribe(sink::tryEmitNext);
                        
      }
}