package wallettrial_2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final String ACCOUNTS="account.txt";
    private static final String TRANSACTIONS="transactions.txt";

    public static List<User>readUsersFromFile(){
        List<User> users=new ArrayList<>();
        try(BufferedReader br=new BufferedReader(new FileReader(ACCOUNTS))){
            String line;
            while((line=br.readLine())!=null) {
                String[] userData=line.split(",");
                String username=userData[0];
                String password=userData[1];
                double balance=Double.parseDouble(userData[2]);
                User user=new User(username,password,balance);
                users.add(user);
            }
        }catch(IOException e){
            System.out.println("Error reading user data from file.");
        }
        loadTransactionHistory(users);
        return users;
    }

    public static void writeUsersToFile(List<User>users){
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(ACCOUNTS))){
            for(User user:users){
                bw.write(user.getUsername()+","+user.getPassword()+","+user.getAccount().getBalance());
                bw.newLine();
            }
        }catch(IOException e){
            System.out.println("Error writing user data to file.");
        }
        saveTransactionHistory(users);
    }

    public static void saveTransactionHistory(List<User> users){
        try(BufferedWriter bw=new BufferedWriter(new FileWriter(TRANSACTIONS))){
            for(User user:users){
                for(String transaction:user.getAccount().getTransactionHistory()){
                    bw.write(user.getUsername()+","+transaction);
                    bw.newLine();
                }
            }
        }catch(IOException e){
            System.out.println("Error saving transaction history.");
        }
    }

    public static void loadTransactionHistory(List<User>users){
        try(BufferedReader br=new BufferedReader(new FileReader(TRANSACTIONS))){
            String line;
            while((line=br.readLine())!=null){
                String[] transactionData=line.split(",",2);
                String username=transactionData[0];
                String transaction=transactionData[1];
                for(User user:users){
                    if(user.getUsername().equals(username)){
                        user.getAccount().addTransaction(transaction);
                        break;
                    }
                }
            }
        }catch(IOException e){
            System.out.println("Error loading transaction history.");
        }
    }
}