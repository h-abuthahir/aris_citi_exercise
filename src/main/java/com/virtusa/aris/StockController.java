package com.virtusa.aris;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;
    private final Sinks.Many<Stock> sink;
    private final Random random = new Random();

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping("/quote")
    public Mono<Stock> getStockPrice(@RequestParam String symbol) {
        return stockService.getStockPrice(symbol);
    }

    @GetMapping("/quotes")
    public Flux<Stock> getStockPrices(@RequestParam List<String> symbols) {
        return stockService.getStockPrices(symbols);
    }

    @GetMapping("/subscribe")
    public Flux<Stock> subscribeToStocks(@RequestParam List<String> symbols) {
        return stockService.subscribeToStocks(symbols);
    }

    // For testing, simulate price updates
    @PostMapping("/update")
    public Mono<Stock> updateStockPrice(@RequestBody Stock stock) {
        sink.tryEmitNext(stock);
        return Mono.just(stock);
    }
    // Method to simulate stock price updates
    public void startSimulatingPriceUpdates() {
        simulatePriceUpdates("AAPL");
    }

    public void simulatePriceUpdates(String symbol) {
        Random random = new Random();
        Flux.interval(Duration.ofSeconds(1))
                .map(tick -> {
                    Stock stock = new Stock();
                    stock.setSymbol(symbol);
                    stock.setPrice(150 + random.nextDouble() * 10);
                    return stock;
                })
                .subscribe(sink::tryEmitNext);
    }
}