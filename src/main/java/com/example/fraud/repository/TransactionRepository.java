package com.example.fraud.repository;
import com.example.fraud.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository  extends JpaRepository<Transaction, String> {



}
