package wallettrial_2;

public class WalletResult {
    private final boolean success;
    private final String message;
    private final User user;

    public WalletResult(boolean success, String message) {
        this(success, message, null);
    }

    public WalletResult(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
