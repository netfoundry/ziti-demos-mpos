package com.nf.flash.models;

import java.io.Serializable;

public class SaleTransactionDetails implements Serializable {

    private static final long serialVersionUID = 7528786489694997350L;
    private String saleTransactionId;
    private long saleDateTime;
    private String saleMobileAlias;
    private String saleAmount;
    private String saleStatus;

    public String getSaleTransactionId() {
        return saleTransactionId;
    }

    public void setSaleTransactionId(String saleTransactionId) {
        this.saleTransactionId = saleTransactionId;
    }

    public long getSaleDateTime() {
        return saleDateTime;
    }

    public void setSaleDateTime(long saleDateTime) {
        this.saleDateTime = saleDateTime;
    }

    public String getSaleMobileAlias() {
        return saleMobileAlias;
    }

    public void setSaleMobileAlias(String saleMobileAlias) {
        this.saleMobileAlias = saleMobileAlias;
    }

    public String getAmount() {
        return saleAmount;
    }

    public void setAmount(String amount) {
        this.saleAmount = amount;
    }

    public String getStatus() {
        return saleStatus;
    }

    public void setStatus(String status) {
        this.saleStatus = status;
    }

}
