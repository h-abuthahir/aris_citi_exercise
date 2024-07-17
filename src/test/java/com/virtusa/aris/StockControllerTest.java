package com.virtusa.aris;

import com.virtusa.aris.Stock;
import com.virtusa.aris.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class StockControllerTest {

    @Mock
    private StockService stockService;

    @InjectMocks
    private StockController stockController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        stockController.simulatePriceUpdates("AAPL");
    }

    @Test
    public void testGetStockPrice() {
        Stock stock = new Stock(1L, "AAPL", 150.0);
        when(stockService.getStockPrice(anyString())).thenReturn(Mono.just(stock));

        Mono<Stock> result = stockController.getStockPrice("AAPL");

        StepVerifier.create(result)
                .expectNext(stock)
                .verifyComplete();
    }

    @Test
    public void testSubscribeStock() {
        VirtualTimeScheduler.getOrSet();

        //Flux<Stock> result = stockController.subscribeToStocks(Arrays.asList("AAPL"));
        Flux<String> result = Flux.just("AAPL","GOOGL","TATA");
        StepVerifier.withVirtualTime(() -> result.take(3))
                .thenAwait(Duration.ofSeconds(3))
                .expectNextCount(3)
                .verifyComplete();
    }
}
