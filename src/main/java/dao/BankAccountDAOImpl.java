package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import datastructure.ArrayList;
import datastructure.List;

import model.BankAccount;
import model.Transaction;
import model.User;
import utilities.ConnectionUtility;

public class BankAccountDAOImpl implements BankAccountDAO{
	private static TransactionDAOImpl transactionDAOImpl = new TransactionDAOImpl();
	public List<BankAccount> getBankAccounts() {
		List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
		try {
			Connection conn = ConnectionUtility.getConnection();
			String sql = "SELECT * FROM BankAccount order by BankAccountId";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				BankAccount bankAccount = new BankAccount(rs.getInt(1),rs.getDouble(2),rs.getString(3),rs.getString(4),rs.getString(5));
				bankAccounts.add(bankAccount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bankAccounts;
	}

	public List<BankAccount> getPendingBankAccounts() {
		List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
		try {
			Connection conn = ConnectionUtility.getConnection();
			String sql = "SELECT * FROM BankAccount where order by BankAccountId";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				BankAccount bankAccount = new BankAccount(rs.getInt(1),rs.getDouble(2),rs.getString(3),rs.getString(4),rs.getString(5));
				bankAccounts.add(bankAccount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bankAccounts;
	}
	
	public List<BankAccount> getBankAccounts(String username) {
		List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
		try {
			Connection conn = ConnectionUtility.getConnection();
			String sql = "SELECT * FROM BankAccount where (PrimaryCustomerUsername=? or SecondaryCustomerUsername=?) order by BankAccountId";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, username);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				BankAccount bankAccount = new BankAccount(rs.getInt(1),rs.getDouble(2),rs.getString(3),rs.getString(4),rs.getString(5));
				bankAccounts.add(bankAccount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bankAccounts;
	}

	public void registerBankAccount(BankAccount bankAccount) {
		try {
			Connection conn = ConnectionUtility.getConnection();
			String sql = "insert into bankaccount values (default,0,?,?,?);";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, bankAccount.getAccountType());
			ps.setString(2, bankAccount.getPrimaryCustomerUsername());
			ps.setString(3, bankAccount.getSecondaryCustomerUsername());

			ps.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void approveBankAccount(int id, int choice) {

		try {
			Connection conn = ConnectionUtility.getConnection();
			String sql;
			if(choice==1) {
				sql = "UPDATE BankAccount where BankAccountId=?";
			}else {
				sql = "UPDATE BankAccount where BankAccountId=?";
			}
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}

	public BankAccount getBankAccount(int id) {
		List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
		try {
			Connection conn = ConnectionUtility.getConnection();
			String sql = "SELECT * FROM BankAccount where BankAccountId=? order by BankAccountId";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				BankAccount bankAccount = new BankAccount(rs.getInt(1),rs.getDouble(2),rs.getString(3),rs.getString(4),rs.getString(5));
				bankAccounts.add(bankAccount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			return bankAccounts.get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
		
	}

	public boolean deposit(int id, double amount) {
		if(amount<0) return false;
		double balance = getBankAccount(id).getBalance();
		double newbalance = balance+amount;
		
		try {
			Connection conn = ConnectionUtility.getConnection();
			String sql;
			sql = "UPDATE BankAccount SET balance=? where BankAccountId=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDouble(1, newbalance);
			ps.setInt(2, id);
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Transaction transaction = new Transaction("deposit",amount, id, id);
		transactionDAOImpl.addTransaction(transaction);
		
		return true;
	}

	public boolean withdraw(int id, double amount) {
		if(amount<0) return false;
		double balance = getBankAccount(id).getBalance();
		double newbalance = balance-amount;
		if(newbalance<0) return false;
		try {
			Connection conn = ConnectionUtility.getConnection();
			String sql;
			sql = "UPDATE BankAccount SET balance=? where BankAccountId=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDouble(1, newbalance);
			ps.setInt(2, id);
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Transaction transaction = new Transaction("withdraw",amount, id, id);
		transactionDAOImpl.addTransaction(transaction);
		
		return true;
	}

	public boolean transfer(int from, int to, double amount) {
		if(amount<0) {
			System.out.println("The amount is negative, please enter a positive amount...");
			return false;
		}
		double balance = getBankAccount(from).getBalance();
		double newbalance = balance-amount;
		if(newbalance<0) {
			System.out.println("Don't have enough money to transfer, please enter a smaller amount...");
			return false;
		}
		withdrawTransfer(from, amount);
		depositTransfer(to, amount);
		Transaction transaction = new Transaction("transfer",amount, from, to);
		transactionDAOImpl.addTransaction(transaction);
		return true;
	}

	private boolean withdrawTransfer(int id, double amount){
		if(amount<0) return false;
		double balance = getBankAccount(id).getBalance();
		double newbalance = balance-amount;
		if(newbalance<0) return false;
		try {
			Connection conn = ConnectionUtility.getConnection();
			String sql;
			sql = "UPDATE BankAccount SET balance=? where BankAccountId=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDouble(1, newbalance);
			ps.setInt(2, id);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}


		return true;
	}

	private boolean depositTransfer(int id, double amount){
		if(amount<0) return false;
		double balance = getBankAccount(id).getBalance();
		double newbalance = balance+amount;

		try {
			Connection conn = ConnectionUtility.getConnection();
			String sql;
			sql = "UPDATE BankAccount SET balance=? where BankAccountId=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDouble(1, newbalance);
			ps.setInt(2, id);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

}
