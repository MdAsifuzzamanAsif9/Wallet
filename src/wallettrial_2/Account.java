package wallettrial_2;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    protected double balance;
    protected List<String>transactionHistory;

    public Account(double balance){
        this.balance=balance;
        this.transactionHistory=new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount){
        deposit(amount,true);
    }

    public void deposit(double amount,boolean transaction){
        balance+=amount;
        if(transaction){
            transactionHistory.add("Deposited: "+amount+" taka, New Balance: "+balance+" taka");
        }
    }

    public boolean withdraw(double amount,boolean transaction){
        if(balance>=amount){
            balance-=amount;
            if(transaction){
                transactionHistory.add("Withdrew: "+amount+" taka, New Balance: "+balance+" taka");
            }
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount){
        return withdraw(amount,true);
    }

    public boolean transfer(Account recipientAccount,String recipientUsername,double amount){
        if(withdraw(amount,false)){
            recipientAccount.deposit(amount,false);
            transactionHistory.add("Transferred: "+amount+" taka to "+recipientUsername);
            recipientAccount.addTransferHistory(amount,this.getUsername());
            return true;
        }
        return false;
    }

    public void addTransferHistory(double amount,String senderUsername){
        transactionHistory.add("Received: "+amount+" taka from "+senderUsername);
    }

    public List<String> getTransactionHistory(){
        return transactionHistory;
    }

    public void addTransaction(String transaction){
        transactionHistory.add(transaction);
    }

    public abstract String getUsername();
}