package wallettrial_2;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WalletService {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private final DecimalFormat amountFormat = new DecimalFormat("0.00");
    private final List<User> users;

    public WalletService() {
        this.users = FileUtil.readUsersFromFile();
    }

    public WalletResult register(String username, String password) {
        String cleanUsername = sanitize(username);
        String cleanPassword = sanitize(password);

        if (cleanUsername.isEmpty() || cleanPassword.isEmpty()) {
            return new WalletResult(false, "Username and password are required.");
        }

        if (isUsernameTaken(cleanUsername)) {
            return new WalletResult(false, "This username has already been taken.");
        }

        User newUser = new User(cleanUsername, cleanPassword, 0.0);
        users.add(newUser);
        persist();
        return new WalletResult(true, "Account created successfully. You can log in now.", newUser);
    }

    public WalletResult login(String username, String password) {
        String cleanUsername = sanitize(username);
        String cleanPassword = sanitize(password);

        if (isAdminCredentials(cleanUsername, cleanPassword)) {
            return new WalletResult(true, "Admin access granted.");
        }

        User user = findUser(cleanUsername);
        if (user != null && user.checkPassword(cleanPassword)) {
            return new WalletResult(true, "Login successful.", user);
        }

        return new WalletResult(false, "Invalid username or password.");
    }

    public boolean isAdminCredentials(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }

    public WalletResult deposit(User user, String amountText, String bankNumber, String pin) {
        Double amount = parsePositiveAmount(amountText);
        if (amount == null) {
            return new WalletResult(false, "Enter a valid positive deposit amount.");
        }

        if (!BankUtil.validateBankAccount(sanitize(bankNumber), sanitize(pin))) {
            return new WalletResult(false, "Invalid bank account number or PIN.");
        }

        user.getAccount().deposit(amount, true);
        persist();
        return new WalletResult(true, "Deposited " + formatAmount(amount) + ". New balance: " + formatAmount(user.getAccount().getBalance()) + ".");
    }

    public WalletResult withdraw(User user, String boothNumber, String amountText) {
        Double amount = parsePositiveAmount(amountText);
        if (amount == null) {
            return new WalletResult(false, "Enter a valid positive withdrawal amount.");
        }

        if (!BoothUtil.validateBooth(sanitize(boothNumber))) {
            return new WalletResult(false, "Invalid wallet booth number.");
        }

        if (amount % 100 != 0) {
            return new WalletResult(false, "Withdrawal amount must be divisible by 100.");
        }

        if (!user.getAccount().withdraw(amount, true)) {
            return new WalletResult(false, "Insufficient balance for this withdrawal.");
        }

        persist();
        return new WalletResult(true, "Withdrawn " + formatAmount(amount) + ". Remaining balance: " + formatAmount(user.getAccount().getBalance()) + ".");
    }

    public WalletResult transfer(User sender, String recipientUsername, String amountText) {
        Double amount = parsePositiveAmount(amountText);
        if (amount == null) {
            return new WalletResult(false, "Enter a valid positive transfer amount.");
        }

        String cleanRecipient = sanitize(recipientUsername);
        if (cleanRecipient.isEmpty()) {
            return new WalletResult(false, "Recipient username is required.");
        }

        if (sender.getUsername().equals(cleanRecipient)) {
            return new WalletResult(false, "You cannot transfer money to your own account.");
        }

        User recipient = findUser(cleanRecipient);
        if (recipient == null) {
            return new WalletResult(false, "Recipient account not found.");
        }

        if (!sender.getAccount().transfer(recipient.getAccount(), cleanRecipient, amount)) {
            return new WalletResult(false, "Insufficient balance for this transfer.");
        }

        persist();
        return new WalletResult(true, "Transferred " + formatAmount(amount) + " to " + cleanRecipient + ".");
    }

    public List<String> getTransactionHistory(User user) {
        List<String> history = new ArrayList<>(user.getAccount().getTransactionHistory());
        Collections.reverse(history);
        return history;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public String getFormattedBalance(User user) {
        return formatAmount(user.getAccount().getBalance());
    }

    public String getFormattedTransactionCount(User user) {
        return String.valueOf(user.getAccount().getTransactionHistory().size());
    }

    private void persist() {
        FileUtil.writeUsersToFile(users);
    }

    private boolean isUsernameTaken(String username) {
        return findUser(username) != null;
    }

    private User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private String sanitize(String value) {
        return value == null ? "" : value.trim();
    }

    private Double parsePositiveAmount(String amountText) {
        try {
            double amount = Double.parseDouble(sanitize(amountText));
            if (amount <= 0) {
                return null;
            }
            return amount;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String formatAmount(double amount) {
        return amountFormat.format(amount) + " taka";
    }
}
