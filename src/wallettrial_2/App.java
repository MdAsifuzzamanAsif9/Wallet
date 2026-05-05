package wallettrial_2;

import java.util.List;
import java.util.Scanner;

public class App {
    private static List<User>users;
    private static Scanner s=new Scanner(System.in);

    public static void main(String[] args) {
        users=FileUtil.readUsersFromFile();
        System.out.println("Welcome to Wallet");
        while(true){
            System.out.println("\nMenu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int option=Integer.parseInt(s.nextLine());

            switch(option){
                case 1:
                    register();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    FileUtil.writeUsersToFile(users);
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void register(){
        System.out.print("Enter username: ");
        String username=s.nextLine();
        
        if(isUsernameTaken(username)){
           System.out.println("This username has already been taken. Please choose a different username.");
           return;
        }
        
        System.out.print("Enter password: ");
        String password =s.nextLine();       
        double money=0.0;
        User newUser=new User(username, password, money);
        users.add(newUser);
        System.out.println("User registered successfully.");
        FileUtil.writeUsersToFile(users);  
    }

    private static void login(){
        System.out.print("Enter username: ");
        String username=s.nextLine();
        System.out.print("Enter password: ");
        String password=s.nextLine();
        
        if(username.equals("admin")&&password.equals("admin")){
            viewAllAccounts();
            return;
        }

        User user=authenticate(username,password);
        if(user!=null){
            userMenu(user);
        }else{
            System.out.println("Invalid Information.Try again.");
        }
    }

    private static User authenticate(String username,String password){
        for(User user:users){
            if(user.getUsername().equals(username) && user.checkPassword(password)){
                return user;
            }
        }
        return null;
    }

    private static void userMenu(User user){
        while(true){
            System.out.println("\nWelcome, "+user.getUsername());
            System.out.println("1. Deposit Money");
            System.out.println("2. Withdraw Money");
            System.out.println("3. Transfer Money");
            System.out.println("4. Check Balance");
            System.out.println("5. Transaction History");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int option=Integer.parseInt(s.nextLine());

            switch(option){
                case 1:
                    deposit(user);
                    break;
                case 2:
                    withdraw(user);
                    break;
                case 3:
                    transfer(user);
                    break;
                case 4:
                    checkBalance(user);
                    break;
                case 5:
                    transactionHistory(user);
                    break;
                case 6:
                    FileUtil.writeUsersToFile(users);
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void deposit(User user){
        System.out.print("Enter amount to deposit: ");
        double amount=Double.parseDouble(s.nextLine());
        System.out.print("Enter bank ID number: ");
        String bankNumber=s.nextLine();
        System.out.print("Enter bank ID PIN: ");
        String pin=s.nextLine();

        if(BankUtil.validateBankAccount(bankNumber, pin)){
            user.getAccount().deposit(amount,true);
            System.out.println("Deposited " + amount + " taka. New balance: " + user.getAccount().getBalance()+" taka");
            FileUtil.writeUsersToFile(users);  
        }else{
            System.out.println("Invalid bank credentials. Deposit failed.");
        }
    }

    private static void withdraw(User user){
        System.out.print("Enter nearby Wallet booth number : ");
        String boothNumber=s.nextLine();
        
        if(BoothUtil.validateBooth(boothNumber)){
            System.out.println("Scan the QR code shown on the screen of the booth\nVerifying...\nVerification successful!\n!! NOTICE:only available notes of 100,500,1000 taka,so withdraw amount must be divisible by 100. !!");
            
        double amount;
        while(true){
            System.out.print("Enter amount to withdraw: ");
            amount=Double.parseDouble(s.nextLine());

            if(amount%100==0){
                break;
            }else{
                System.out.println("Invalid amount. Please enter an amount divisible by 100.");
            }
        }
            if(user.getAccount().withdraw(amount,true)){
                System.out.println("Withdrew "+amount+" taka. New balance: "+user.getAccount().getBalance()+" taka");
                FileUtil.writeUsersToFile(users);  
            }else{
                System.out.println("Insufficient funds. Current balance: "+user.getAccount().getBalance()+" taka");
            }
        }else{
            System.out.println("Invalid booth number. Withdrawal failed.");
        }
    }

    private static void transfer(User user){
        System.out.print("Enter username of recipient: ");
        String recipientUsername=s.nextLine();
        User recipient=findUser(recipientUsername);
        if(recipient!=null){
            System.out.print("Enter amount to transfer: ");
            double amount=Double.parseDouble(s.nextLine());
            if(user.getAccount().transfer(recipient.getAccount(),recipientUsername,amount)){
                System.out.println("Transferred "+amount+" taka to "+recipient.getUsername());
            }else{
                System.out.println("Insufficient funds. Current balance: "+user.getAccount().getBalance()+" taka");
            }
            FileUtil.writeUsersToFile(users);  
        }else{
            System.out.println("Recipient not found.");
        }
    }

    private static User findUser(String username){
        for(User user:users){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }
    
    private static boolean isUsernameTaken(String username){
        for(User user:users){
            if(user.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    private static void checkBalance(User user){
        System.out.println("Current balance: "+user.getAccount().getBalance()+" taka");
    }

    private static void transactionHistory(User user){
        List<String> history=user.getAccount().getTransactionHistory();
        System.out.println("Transaction History:");
        for(String transaction:history){
            System.out.println(transaction);
        }
    }
    
    private static void viewAllAccounts(){
       System.out.println("\nAll Registered Accounts:");
       for(User user:users){
           System.out.println("Username: "+user.getUsername()+",  Balance: "+user.getAccount().getBalance()+" taka");
        }
    }
}