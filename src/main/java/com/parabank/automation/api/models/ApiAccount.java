package com.parabank.automation.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiAccount {

	private Long id;
	private Integer customerId;
	private String type;
	private BigDecimal balance;

	public ApiAccount() {
	}

	public ApiAccount(Long id, Integer customerId, String type, BigDecimal balance) {
		this.id = id;
		this.customerId = customerId;
		this.type = type;
		this.balance = balance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}