package com.example.fraud.entity;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "fraud_rules")
public class FraudRule {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long RuleId;
	private String name;
	private String paramKey;
	private Boolean enabled;
	private Instant createdAT;
	private Instant updatedAt;
	public enum RuleType{
		THRESHOLD,
		MERCHANT,
		VELOCITY
	}
	public Long getRuleId() {
		return RuleId;
	}
	public void setRuleId(Long ruleId) {
		RuleId = ruleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParamKey() {
		return paramKey;
	}
	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Instant getCreatedAT() {
		return createdAT;
	}
	public void setCreatedAT(Instant createdAT) {
		this.createdAT = createdAT;
	}
	public Instant getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
	

}
