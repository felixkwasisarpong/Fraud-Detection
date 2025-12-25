package com.example.fraud.event;

import lombok.Data;

import java.time.Instant;


@Data
public class TransactionEvent {

    private String transactionId;


    private  String accountId;


    private String merchantId;


    private Double amount;
    private String currency;

    private String deviceId;
    private String ipAddress;
    private Instant timestamp;

}
