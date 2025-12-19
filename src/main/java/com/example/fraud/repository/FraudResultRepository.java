package com.example.fraud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fraud.entity.FraudResult;

public interface FraudResultRepository extends JpaRepository<FraudResult, Long>{

}
