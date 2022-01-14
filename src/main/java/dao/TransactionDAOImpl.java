package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import datastructure.ArrayList;
import datastructure.List;

import model.BankAccount;
import model.Transaction;
import utilities.ConnectionUtility;

public class TransactionDAOImpl implements TransactionDAO{

	public List<Transaction> getTransaction(int id) {
		List<Transaction> transactions = new ArrayList<Transaction>();
		try {
			Connection conn = ConnectionUtility.getConnection();
			String sql = "select * from transaction where fromaccount=? or toaccount=? order by transactionid";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.setInt(2, id);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Transaction transaction = new Transaction(rs.getInt(1),rs.getDate(2),rs.getString(3),rs.getDouble(4),rs.getInt(5),rs.getInt(6));
				transactions.add(transaction);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactions;
	}

	public void addTransaction(Transaction transaction) {
		try {
			Connection conn = ConnectionUtility.getConnection();
			String sql = "insert into transaction values(default,now(),?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, transaction.getTransactionType());
			ps.setDouble(2, transaction.getAmount());
			ps.setInt(3, transaction.getFromAccount());
			ps.setInt(4, transaction.getToAccount());
			ps.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
