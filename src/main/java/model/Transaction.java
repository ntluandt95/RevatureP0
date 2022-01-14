package model;

import java.util.Date;

public class Transaction {
	private int transactionId;
	private Date transactionDay;
	private String transactionType;
	private double amount;
	private int fromAccount;
	private int toAccount;
	public Transaction(int transactionId, Date transactionDay, String transactionType, Double amount,
			int fromAccount, int toAccount) {
		this.transactionId = transactionId;
		this.transactionDay = transactionDay;
		this.transactionType = transactionType;
		this.amount = amount;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
	}
	public Transaction(String transactionType, double amount, int fromAccount, int toAccount) {
		this.transactionType = transactionType;
		this.amount = amount;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
	}
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public Date getTransactionDay() {
		return transactionDay;
	}
	public void setTransactionDay(Date transactionDay) {
		this.transactionDay = transactionDay;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getFromAccount() {
		return fromAccount;
	}
	public void setFromAccount(int fromAccount) {
		this.fromAccount = fromAccount;
	}
	public int getToAccount() {
		return toAccount;
	}
	public void setToAccount(int toAccount) {
		this.toAccount = toAccount;
	}

	@Override
	public String toString() {
		return "Transaction{" +
				"transactionId=" + transactionId +
				", transactionDay=" + transactionDay +
				", transactionType='" + transactionType + '\'' +
				", amount=" + amount +
				", fromAccount=" + fromAccount +
				", toAccount=" + toAccount +
				'}';
	}
}
