package com.example.fraud.event;

import lombok.Data;


@Data
public class TransactionEvent {

    private String transactionId;


    private  String accountId;


    private String merchantId;


    private Double amount;
    private String currency;

}
