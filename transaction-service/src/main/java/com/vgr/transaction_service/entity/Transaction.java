package com.vgr.transaction_service.entity;



import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String type; // DEPOSIT, WITHDRAW, TRANSFER

    private LocalDateTime transactionDate = LocalDateTime.now();

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the fromAccount
	 */
	public String getFromAccount() {
		return fromAccount;
	}

	/**
	 * @param fromAccount the fromAccount to set
	 */
	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	/**
	 * @return the toAccount
	 */
	public String getToAccount() {
		return toAccount;
	}

	/**
	 * @param toAccount the toAccount to set
	 */
	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the transactionDate
	 */
	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

    
}
