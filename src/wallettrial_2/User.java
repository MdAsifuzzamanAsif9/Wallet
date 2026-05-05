package wallettrial_2;

public class User {
    private String username;
    private String password;
    private Account account;

    public User(String username,String password,double initialDeposit){
        this.username = username;
        this.password = password;
        this.account=new SaveAccount(this,initialDeposit);   
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
     public Account getAccount() {
        return account;
    }

    public boolean checkPassword(String password){
        return this.password.equals(password);
    }

   
}
