package dao;

import datastructure.List;

import model.Transaction;

public interface TransactionDAO {
	List<Transaction> getTransaction(int id);
	void addTransaction(Transaction transaction);
}
