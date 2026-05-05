package wallettrial_2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BankUtil {
    private static final String BANK="bank.txt";

    public static List<BankAccount>readBankAccountsFromFile(){
        List<BankAccount>bankAccounts=new ArrayList<>();
        try(BufferedReader reader=new BufferedReader(new FileReader(BANK))){
            String line;
            while((line=reader.readLine())!=null){
                bankAccounts.add(BankAccount.fromString(line));
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();          
        }catch(IOException e){
            e.printStackTrace();
        }
        return bankAccounts;
    }

    public static boolean validateBankAccount(String bankNumber,String pin){
        List<BankAccount>bankAccounts=readBankAccountsFromFile();
        for(BankAccount account:bankAccounts){
            if(account.getBankNumber().equals(bankNumber) && account.getPin().equals(pin)){
                return true;
            }
        }
        return false;
    }
}

