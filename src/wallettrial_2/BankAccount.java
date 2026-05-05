
package wallettrial_2;

public class BankAccount {
    private String bankNumber;
    private String pin;

    public BankAccount(String bankNumber, String pin) {
        this.bankNumber = bankNumber;
        this.pin = pin;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public String getPin() {
        return pin;
    }

    public static BankAccount fromString(String Data){
        String[] parts=Data.split(",");
        String bankNumber=parts[0];
        String pin=parts[1];
        return new BankAccount(bankNumber,pin);
    }

    @Override
    public String toString() {
        return bankNumber+","+pin;
    }
}
