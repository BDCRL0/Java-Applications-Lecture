package hu.nje.homework.model;

import java.time.LocalDate;

public class CurrencyData {
    private LocalDate date;
    private String currency;
    private Double rate;

    public CurrencyData() {}

    public CurrencyData(LocalDate date, String currency, Double rate) {
        this.date = date;
        this.currency = currency;
        this.rate = rate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "CurrencyData{" +
                "date=" + date +
                ", currency='" + currency + '\'' +
                ", rate=" + rate +
                '}';
    }
}