package wallettrial_2;

public class SaveAccount extends Account {
    private User user;

    public SaveAccount(User user, double balance) {
        super(balance);
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
