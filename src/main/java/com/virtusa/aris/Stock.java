package com.virtusa.aris;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("stocks")
public class Stock {

    @Id
    private Long id;
    private String symbol;
    private Double price;

    // Constructors, getters, and setters
    public Stock() {}

    public Stock(Long id, String symbol, Double price) {
        this.id = id;
        this.symbol = symbol;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}