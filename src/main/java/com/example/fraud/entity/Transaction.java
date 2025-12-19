package com.example.fraud.entity;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "transactions")
public class Transaction {

@Id
private String transationId;
private String accountId;
private String merchantId;
private Double amount;
private String currency;
private Instant timestamp;

@Enumerated(EnumType.STRING)
private Status status;
public enum Status {
    RRECEIVED,
    PROCESSED,
    FAILED
}




    
}
