package com.example.fraud.service;
import com.example.fraud.dto.TransactionRequest;
import com.example.fraud.dto.TransactionResponse;

public interface TransactionService {

    TransactionResponse receiveTransaction(TransactionRequest request);

}
