package com.example.homework.model;

import java.time.LocalDateTime;

public class AccountInfo {
    private String accountId;
    private String accountType;
    private String baseCurrency;
    private String status;
    private String createdDate;
    private Double balance;
    private Double nav;
    private Double unrealizedPL;
    private Double marginUsed;
    private Double marginAvailable;
    private Integer totalTrades;
    private Integer winningTrades;
    private Integer losingTrades;
    private Double winRate;

    // Constructors
    public AccountInfo() {}

    public AccountInfo(String accountId, String accountType, String baseCurrency, 
                      String status, String createdDate, Double balance, Double nav, 
                      Double unrealizedPL, Double marginUsed, Double marginAvailable) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.baseCurrency = baseCurrency;
        this.status = status;
        this.createdDate = createdDate;
        this.balance = balance;
        this.nav = nav;
        this.unrealizedPL = unrealizedPL;
        this.marginUsed = marginUsed;
        this.marginAvailable = marginAvailable;
    }

    // Getters and Setters
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public String getBaseCurrency() { return baseCurrency; }
    public void setBaseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }

    public Double getNav() { return nav; }
    public void setNav(Double nav) { this.nav = nav; }

    public Double getUnrealizedPL() { return unrealizedPL; }
    public void setUnrealizedPL(Double unrealizedPL) { this.unrealizedPL = unrealizedPL; }

    public Double getMarginUsed() { return marginUsed; }
    public void setMarginUsed(Double marginUsed) { this.marginUsed = marginUsed; }

    public Double getMarginAvailable() { return marginAvailable; }
    public void setMarginAvailable(Double marginAvailable) { this.marginAvailable = marginAvailable; }

    public Integer getTotalTrades() { return totalTrades; }
    public void setTotalTrades(Integer totalTrades) { this.totalTrades = totalTrades; }

    public Integer getWinningTrades() { return winningTrades; }
    public void setWinningTrades(Integer winningTrades) { this.winningTrades = winningTrades; }

    public Integer getLosingTrades() { return losingTrades; }
    public void setLosingTrades(Integer losingTrades) { this.losingTrades = losingTrades; }

    public Double getWinRate() { return winRate; }
    public void setWinRate(Double winRate) { this.winRate = winRate; }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "accountId='" + accountId + '\'' +
                ", accountType='" + accountType + '\'' +
                ", baseCurrency='" + baseCurrency + '\'' +
                ", status='" + status + '\'' +
                ", balance=" + balance +
                ", nav=" + nav +
                '}';
    }
}
