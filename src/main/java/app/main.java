package app;

import dao.BankAccountDAOImpl;
import dao.TransactionDAOImpl;
import dao.UserDAOImpl;
import model.BankAccount;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.*;

import datastructure.ArrayList;

import java.util.Scanner;

public class main {
    static Scanner sc = new Scanner(System.in);
    static UserDAOImpl userDAOImpl = new UserDAOImpl();
    static BankAccountDAOImpl bankAccountDAOImpl = new BankAccountDAOImpl();
    static TransactionDAOImpl transactionDAOImpl = new TransactionDAOImpl();
    static User loginUsername;
    //log object
    static Logger logger = LogManager.getLogger();

    public static void mainMenu() {
        System.out.println("");
        int choice;
        while (true) {
            if(loginUsername==null) {
                System.out.println("Welcome to Main Menu!!!");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Quit");
                System.out.print("Enter Your Choice : ");
                choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1:
                        Login();
                        break;
                    case 2:
                        Register();
                        break;
                    case 3:
                        System.exit(0);
                    default:
                        System.out.println("Wrong Entry");
                }
            }else{
                System.out.println("Hello Customer "+ loginUsername.getFirstName() +" "+ loginUsername.getLastName() +"! Welcome to Main Menu!!!");
                System.out.println("1. Apply for a new bank account");
                System.out.println("2. View all your bank account");
                System.out.println("3. Logout");
                System.out.print("Enter Your Choice : ");
                choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1:
                        applyNewBankAccount();
                        break;
                    case 2:
                        viewBankAccount();
                        break;
                    case 3:
                        logout();
                        break;
                    default:
                        System.out.println("Wrong Entry");
                }
            }
        }

    }

    private static void Login() {
        System.out.println("");
        while (true) {
            System.out.println("Welcome to Login form");
            System.out.println("Enter Username:");
            String username = sc.nextLine();
            System.out.println("Enter Password:");
            String password = sc.nextLine();
            User user = userDAOImpl.login(username, password);
            if (user != null) {
                System.out.println("Hello " + username);
                loginUsername = user;
                logger.info("User Login "+loginUsername.getUsername());
                break;
            } else {
                System.out.println("Wrong username or password!");
                int choice;

                System.out.println("1. Try again");
                System.out.println("2. Return to main menu");
                System.out.println("3. Quit");
                System.out.print("Enter Your Choice : ");
                choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1:

                        break;
                    case 2:
                        return;

                    case 3:
                        System.exit(0);
                    default:
                        System.out.println("Wrong Entry");
                }

            }
        }
    }

    private static void logout() {
        System.out.println("");
        logger.info("User Logout "+loginUsername.getUsername());
        loginUsername =null;
        System.out.println("Logout!");

        mainMenu();
    }

    private static void Register() {
        System.out.println("");
        System.out.println("Welcome to Register form");
        String username;
        while (true) {
            System.out.println("Enter Username:");
            username = sc.nextLine();
            if(userDAOImpl.getUser(username)==null) {
                break;
            }else {
                System.out.println("Username already exists. Please choose a different username!");
            }
        }

        System.out.println("Enter Password:");
        String password = sc.nextLine();
        System.out.println("Enter Last Name:");
        String lastName = sc.nextLine();
        System.out.println("Enter First Name:");
        String firstName = sc.nextLine();
        User user = new User(username, password, lastName, firstName);
        userDAOImpl.register(user);
        System.out.println("Register successful");

        logger.info("User Register "+ username);

    }

    private static void applyNewBankAccount() {
        System.out.println("");
        System.out.println("Welcome to Apply New Bank Account form");
        String accountType;
        while (true) {
            System.out.println("Enter accountType: (enter 'saving' or 'checking')");
            accountType = sc.nextLine().toLowerCase();
            if(accountType.equalsIgnoreCase("saving") || accountType.equalsIgnoreCase("checking")) {
                break;
            }else {
                System.out.println("please enter 'saving' or 'checking'");
            }
        }
        String secondaryCustomerUsername;
        while (true) {
            System.out.println("Enter Secondary Customer Username: (enter 'no' if there is no joint applicant)");
            secondaryCustomerUsername = sc.nextLine();
            if(secondaryCustomerUsername.equalsIgnoreCase("no")) {
                secondaryCustomerUsername=null;
                break;
            }else if(userDAOImpl.getUser(secondaryCustomerUsername)!=null) {
                break;
            }else {
                System.out.println("Username doesn't exists. Please choose a different username!");
            }
        }

        BankAccount bankAccount = new BankAccount(accountType,loginUsername.getUsername(),secondaryCustomerUsername);
        bankAccountDAOImpl.registerBankAccount(bankAccount);


    }

    private static void viewBankAccount() {
        System.out.println("");
        System.out.println("List of your Bank Account:");
        System.out.println(TableBuilderUtility.bankAccountTableBuilder(bankAccountDAOImpl.getBankAccounts(loginUsername.getUsername())));
        int id;
        while(true) {
            try {
                System.out.println("Enter the ID to select an account:");
                id = sc.nextInt();
                sc.nextLine();
                ArrayList<BankAccount> bankAccount = new ArrayList<BankAccount>();
                bankAccount.add(bankAccountDAOImpl.getBankAccount(id));
                System.out.println(TableBuilderUtility.bankAccountTableBuilder(bankAccount));
                break;
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid ID, try again!");
            }
        }

        bankATM(id);


    }

    private static void bankATM(int id) {
        System.out.println("");
        System.out.println("1. View Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. View Transaction History");
        System.out.println("5. Money Transfer");
        System.out.println("6. Return to list of your Bank Account");
        System.out.println("7. Return to Main Menu");
        System.out.print("Enter Your Choice : ");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice) {
            case 1:
                System.out.println("Balance = "+bankAccountDAOImpl.getBankAccount(id).getBalance());
                bankATM(id);
                return;
            case 2:
                while (true) {
                    System.out.println("Enter the amount of money to deposit:");
                    double amount = sc.nextDouble();
                    sc.nextLine();
                    if(!bankAccountDAOImpl.deposit(id, amount)) {
                        System.out.println("Please Enter a POSITIVE amount of money");
                    }else {
                        System.out.println("New balance = "+bankAccountDAOImpl.getBankAccount(id).getBalance());
                        break;
                    }


                }
                bankATM(id);
                return;
            case 3:
                while (true) {
                    System.out.println("Enter the amount of money to withdraw:");
                    double amount = sc.nextDouble();
                    sc.nextLine();
                    if(amount<0) {
                        System.out.println("Please Enter a POSITIVE amount of money");
                    }
                    else if(!bankAccountDAOImpl.withdraw(id, amount)) {
                        System.out.println("Withdrawal result in a negative balance. Please Enter a SMALLER amount of money");
                    }else {
                        System.out.println("New balance = "+bankAccountDAOImpl.getBankAccount(id).getBalance());
                        break;
                    }


                }
                bankATM(id);
                return;
            case 4:
                viewTransactionHistory(id);
                bankATM(id);
                return;
            case 5:
                int to;
                double amount;
                while(true){
                    System.out.println("Recipient's account id:");
                    to = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter the amount:");
                    amount = sc.nextDouble();
                    sc.nextLine();
                    double oldbalance = bankAccountDAOImpl.getBankAccount(id).getBalance();
                    if(bankAccountDAOImpl.getBankAccount(to)==null) {
                        System.out.println("Invalid ID, try again!");
                    }else if(bankAccountDAOImpl.transfer(id, to, amount)==false) {
                        System.out.println("Try again!");
                    }else {
                        System.out.println("Old balance: "+oldbalance);
                        System.out.println("New balance: "+bankAccountDAOImpl.getBankAccount(id).getBalance());
                        break;
                    }
                }
                bankATM(id);
                return;
            case 6:
                viewBankAccount();
                return;
            case 7:
                mainMenu();
                return;
            default:
                System.out.println("Wrong Entry");
        }
    }

    private static void viewTransactionHistory(int id) {
        System.out.println("");
        System.out.println("Transaction History");
        System.out.println(TableBuilderUtility.transactionTableBuilder(transactionDAOImpl.getTransaction(id)));
        bankATM(id);
    }

    public static void main(String[] args) {
        mainMenu();
    }
}
