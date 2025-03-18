package com.unbeaten.Practon.models;

import jakarta.persistence.Embeddable;

@Embeddable
public class PaymentDetails {

    private String paymentMethod;
    private String transactionId;
    private Double amount;
    private String currency;
    private String paymentDate;

    // Default constructor
    public PaymentDetails() {
    }

    // Constructor with all fields
    public PaymentDetails(String paymentMethod, String transactionId, Double amount, String currency,
            String paymentDate) {
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.amount = amount;
        this.currency = currency;
        this.paymentDate = paymentDate;
    }

    // Getters and setters
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}
