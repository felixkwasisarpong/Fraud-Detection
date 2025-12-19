package com.example.fraud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fraud.entity.FraudRule;

public interface FraudRuleRepository extends JpaRepository<FraudRule, Long>{

}
