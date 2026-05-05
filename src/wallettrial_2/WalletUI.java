package wallettrial_2;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import javax.swing.Timer;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class WalletUI extends JFrame {
    private static final String AUTH_SCREEN = "auth";
    private static final String USER_SCREEN = "user";
    private static final String ADMIN_SCREEN = "admin";

    private static final String DASH_OVERVIEW = "overview";
    private static final String DASH_DEPOSIT = "deposit";
    private static final String DASH_WITHDRAW = "withdraw";
    private static final String DASH_TRANSFER = "transfer";

    private static final Color BG_TOP = new Color(8, 16, 42);
    private static final Color BG_BOTTOM = new Color(16, 29, 74);
    private static final Color PANEL = new Color(14, 24, 58);
    private static final Color PANEL_ALT = new Color(23, 39, 82);
    private static final Color PANEL_DEEP = new Color(7, 14, 38);
    private static final Color OUTLINE = new Color(70, 122, 208);
    private static final Color TEXT = new Color(232, 238, 255);
    private static final Color MUTED = new Color(151, 171, 224);
    private static final Color CYAN = new Color(74, 214, 255);
    private static final Color PINK = new Color(255, 83, 222);
    private static final Color GOLD = new Color(255, 202, 67);
    private static final Color GREEN = new Color(93, 235, 136);
    private static final Color RED = new Color(255, 103, 111);
    private static final Color DARK_BUTTON = new Color(30, 51, 107);

    private final WalletService service = new WalletService();
    private final CardLayout screenCards = new CardLayout();
    private final JPanel screenRoot = new JPanel(screenCards);
    private final CardLayout dashboardCards = new CardLayout();
    private final JPanel dashboardContent = new JPanel(dashboardCards);
    private final Timer introAnimationTimer;
    private int introFrame;

    private User currentUser;

    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JTextField registerUsernameField;
    private JPasswordField registerPasswordField;

    private JLabel statusLabel;
    private JLabel userWelcomeLabel;
    private JLabel userBalanceLabel;
    private JLabel overviewBalanceValue;
    private JLabel overviewTransactionsValue;
    private JLabel overviewUserValue;
    private JTextArea historyArea;

    private JTextField depositAmountField;
    private JTextField depositBankField;
    private JPasswordField depositPinField;

    private JTextField withdrawBoothField;
    private JTextField withdrawAmountField;

    private JTextField transferRecipientField;
    private JTextField transferAmountField;

    private JTextArea adminArea;

    public WalletUI() {
        setTitle("Wallet");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1220, 780));
        setSize(1360, 860);
        setLocationRelativeTo(null);

        screenRoot.setOpaque(false);
        dashboardContent.setOpaque(false);

        screenRoot.add(buildAuthScreen(), AUTH_SCREEN);
        screenRoot.add(buildUserScreen(), USER_SCREEN);
        screenRoot.add(buildAdminScreen(), ADMIN_SCREEN);

        PixelScenePanel shell = new PixelScenePanel();
        shell.setLayout(new BorderLayout());
        shell.setBorder(new EmptyBorder(16, 16, 12, 16));
        shell.add(screenRoot, BorderLayout.CENTER);
        shell.add(buildStatusBar(), BorderLayout.SOUTH);

        setContentPane(shell);
        screenCards.show(screenRoot, AUTH_SCREEN);

        introAnimationTimer = new Timer(140, e -> {
            introFrame = (introFrame + 1) % 40;
            repaint();
        });
        introAnimationTimer.start();
    }

    private JPanel buildAuthScreen() {
        JPanel screen = transparentPanel(new BorderLayout(20, 20));

        screen.add(buildHeroScene(), BorderLayout.WEST);
        screen.add(buildAuthCard(), BorderLayout.CENTER);
        return screen;
    }

    private JPanel buildHeroScene() {
        PixelDisplayPanel hero = new PixelDisplayPanel();
        hero.setPreferredSize(new Dimension(460, 0));
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));
        hero.setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel title = pixelLabel("WALLET", 34, TEXT);
        JLabel subtitle = htmlLabel(
            "<html><div style='width:320px;'>Fast wallet access with a playful pixel-style frontend.</div></html>",
            15,
            MUTED
        );

        hero.add(Box.createVerticalStrut(12));
        hero.add(title);
        hero.add(Box.createVerticalStrut(14));
        hero.add(subtitle);
        hero.add(Box.createVerticalStrut(24));
        hero.add(createFeatureChip("ATM CASHOUT ANIMATION", CYAN));
        hero.add(Box.createVerticalStrut(12));
        hero.add(createFeatureChip("LOGIN AND REGISTER", GREEN));
        hero.add(Box.createVerticalStrut(12));
        hero.add(createFeatureChip("DEPOSIT WITHDRAW TRANSFER", GOLD));
        hero.add(Box.createVerticalGlue());
        return hero;
    }

    private JPanel buildAuthCard() {
        PixelCardPanel panel = new PixelCardPanel(PANEL_ALT, OUTLINE, true);
        panel.setLayout(new BorderLayout(16, 16));
        panel.setBorder(new EmptyBorder(26, 26, 26, 26));

        JPanel top = transparentPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(pixelLabel("INSERT COIN TO ENTER", 24, TEXT));
        top.add(Box.createVerticalStrut(8));
        top.add(htmlLabel(
            "<html><div style='width:500px;'>Sign in to your wallet or register a new player profile. The original wallet logic is still running underneath this themed frontend.</div></html>",
            14,
            MUTED
        ));

        JPanel center = transparentPanel(new GridLayout(1, 2, 18, 18));
        center.add(buildLoginPanel());
        center.add(buildRegisterPanel());

        panel.add(top, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildLoginPanel() {
        PixelCardPanel card = new PixelCardPanel(PANEL_DEEP, CYAN, false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        loginUsernameField = createTextField();
        loginPasswordField = createPasswordField();

        card.add(pixelLabel("LOGIN", 20, CYAN));
        card.add(Box.createVerticalStrut(14));
        card.add(createLabeledField("USERNAME", loginUsernameField));
        card.add(Box.createVerticalStrut(12));
        card.add(createLabeledField("PASSWORD", loginPasswordField));
        card.add(Box.createVerticalStrut(18));

        JButton loginButton = createButton("START GAME", CYAN, PANEL_DEEP);
        loginButton.addActionListener(e -> handleLogin());
        card.add(loginButton);
        card.add(Box.createVerticalStrut(12));
        card.add(htmlLabel("<html>Admin route: <b>admin/admin</b></html>", 13, MUTED));
        return card;
    }

    private JPanel buildRegisterPanel() {
        PixelCardPanel card = new PixelCardPanel(PANEL_DEEP, PINK, false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        registerUsernameField = createTextField();
        registerPasswordField = createPasswordField();

        card.add(pixelLabel("REGISTER", 20, PINK));
        card.add(Box.createVerticalStrut(14));
        card.add(createLabeledField("NEW USERNAME", registerUsernameField));
        card.add(Box.createVerticalStrut(12));
        card.add(createLabeledField("NEW PASSWORD", registerPasswordField));
        card.add(Box.createVerticalStrut(18));

        JButton registerButton = createButton("CREATE PROFILE", PINK, PANEL_DEEP);
        registerButton.addActionListener(e -> handleRegister());
        card.add(registerButton);
        card.add(Box.createVerticalStrut(12));
        card.add(htmlLabel("Fresh accounts begin at 0.00 taka.", 13, MUTED));
        return card;
    }

    private JPanel buildUserScreen() {
        JPanel screen = transparentPanel(new BorderLayout(18, 18));
        screen.add(buildDashboardHeader(), BorderLayout.NORTH);
        screen.add(buildDashboardBody(), BorderLayout.CENTER);
        return screen;
    }

    private JPanel buildDashboardHeader() {
        PixelCardPanel header = new PixelCardPanel(PANEL_ALT, OUTLINE, true);
        header.setLayout(new BorderLayout(18, 18));
        header.setBorder(new EmptyBorder(22, 22, 22, 22));

        JPanel left = transparentPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        userWelcomeLabel = pixelLabel("WELCOME, PLAYER", 28, TEXT);
        JLabel subtitle = htmlLabel(
            "<html><div style='width:520px;'>Move through your wallet like a retro control room: check your stash, power up your balance, and keep the transaction log glowing.</div></html>",
            14,
            MUTED
        );

        left.add(userWelcomeLabel);
        left.add(Box.createVerticalStrut(10));
        left.add(subtitle);

        JPanel right = transparentPanel(new GridLayout(1, 2, 14, 14));
        userBalanceLabel = pixelLabel("0.00 taka", 22, GOLD);
        right.add(createMetricTile("COIN BALANCE", userBalanceLabel, GOLD));
        right.add(createMetricTile("SYSTEM", pixelLabel("ONLINE", 18, GREEN), GREEN));

        header.add(left, BorderLayout.CENTER);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel buildDashboardBody() {
        JPanel body = transparentPanel(new BorderLayout(18, 18));
        body.add(buildSidebar(), BorderLayout.WEST);
        body.add(buildCenterArea(), BorderLayout.CENTER);
        body.add(buildHistoryPanel(), BorderLayout.EAST);
        return body;
    }

    private JPanel buildSidebar() {
        PixelDisplayPanel sidebar = new PixelDisplayPanel();
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 18, 20, 18));

        sidebar.add(pixelLabel("MAP SELECT", 20, TEXT));
        sidebar.add(Box.createVerticalStrut(18));
        sidebar.add(createNavButton("OVERVIEW", DASH_OVERVIEW, CYAN));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(createNavButton("DEPOSIT", DASH_DEPOSIT, GREEN));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(createNavButton("WITHDRAW", DASH_WITHDRAW, GOLD));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(createNavButton("TRANSFER", DASH_TRANSFER, PINK));
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(createButton("REFRESH", CYAN, PANEL_DEEP, e -> refreshUserData()));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(createButton("LOGOUT", RED, PANEL_DEEP, e -> logout()));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(createMiniInfoTile("SAVE TYPE", "TXT FILES"));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(createMiniInfoTile("BANK MODE", "VALIDATION"));
        return sidebar;
    }

    private JPanel buildCenterArea() {
        dashboardContent.add(buildOverviewPanel(), DASH_OVERVIEW);
        dashboardContent.add(buildDepositPanel(), DASH_DEPOSIT);
        dashboardContent.add(buildWithdrawPanel(), DASH_WITHDRAW);
        dashboardContent.add(buildTransferPanel(), DASH_TRANSFER);
        dashboardCards.show(dashboardContent, DASH_OVERVIEW);

        JPanel wrapper = transparentPanel(new BorderLayout());
        wrapper.add(dashboardContent, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildOverviewPanel() {
        PixelCardPanel panel = new PixelCardPanel(PANEL_ALT, CYAN, true);
        panel.setLayout(new BorderLayout(16, 16));
        panel.setBorder(new EmptyBorder(22, 22, 22, 22));

        JPanel top = transparentPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(pixelLabel("CONTROL ROOM", 24, CYAN));
        top.add(Box.createVerticalStrut(8));
        top.add(htmlLabel(
            "<html><div style='width:520px;'>This screen tracks your current wallet state with a retro-console mood. Use the left-side menu to deposit, withdraw, or transfer without leaving the dashboard.</div></html>",
            14,
            MUTED
        ));

        JPanel stats = transparentPanel(new GridLayout(1, 3, 14, 14));
        overviewUserValue = pixelLabel("--", 18, PINK);
        overviewBalanceValue = pixelLabel("0.00 taka", 18, GOLD);
        overviewTransactionsValue = pixelLabel("0", 18, GREEN);
        stats.add(createMetricTile("PLAYER", overviewUserValue, PINK));
        stats.add(createMetricTile("BALANCE", overviewBalanceValue, GOLD));
        stats.add(createMetricTile("LOG ENTRIES", overviewTransactionsValue, GREEN));

        panel.add(top, BorderLayout.NORTH);
        panel.add(stats, BorderLayout.CENTER);
        panel.add(createInfoPanel(
            "SYSTEM NOTES",
            "Welcome to the Wallet control room.\n\n"
                + "Deposit uses bank account validation.\n"
                + "Withdrawal checks valid booth numbers.\n"
                + "Transfer updates both wallets.\n"
                + "History stays saved in local text files.\n\n"
                + "This version leans into a retro pixel-night theme inspired by classic arcade and platformer desk scenes."
        ), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildDepositPanel() {
        PixelCardPanel panel = new PixelCardPanel(PANEL_ALT, GREEN, true);
        panel.setLayout(new BorderLayout(16, 16));
        panel.setBorder(new EmptyBorder(22, 22, 22, 22));

        depositAmountField = createTextField();
        depositBankField = createTextField();
        depositPinField = createPasswordField();

        JPanel form = transparentPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.add(pixelLabel("POWER-UP DEPOSIT", 24, GREEN));
        form.add(Box.createVerticalStrut(8));
        form.add(htmlLabel("Load coins into the wallet after a valid bank check.", 14, MUTED));
        form.add(Box.createVerticalStrut(18));
        form.add(createLabeledField("AMOUNT", depositAmountField));
        form.add(Box.createVerticalStrut(12));
        form.add(createLabeledField("BANK ID NUMBER", depositBankField));
        form.add(Box.createVerticalStrut(12));
        form.add(createLabeledField("BANK ID PIN", depositPinField));
        form.add(Box.createVerticalStrut(18));
        form.add(createButton("DEPOSIT NOW", GREEN, PANEL_DEEP, e -> handleDeposit()));

        panel.add(form, BorderLayout.CENTER);
        panel.add(createRulePanel(
            "DEPOSIT RULES",
            new String[] {
                "Amount must be a positive number.",
                "Bank ID number must exist in bank.txt.",
                "Bank ID PIN must match the selected bank ID.",
                "Successful deposits instantly update balance and activity log."
            }
        ), BorderLayout.EAST);
        return panel;
    }

    private JPanel buildWithdrawPanel() {
        PixelCardPanel panel = new PixelCardPanel(PANEL_ALT, GOLD, true);
        panel.setLayout(new BorderLayout(16, 16));
        panel.setBorder(new EmptyBorder(22, 22, 22, 22));

        withdrawBoothField = createTextField();
        withdrawAmountField = createTextField();

        JPanel form = transparentPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.add(pixelLabel("BOOTH EXIT", 24, GOLD));
        form.add(Box.createVerticalStrut(8));
        form.add(htmlLabel("Use a valid booth number and withdraw arcade-style cash.", 14, MUTED));
        form.add(Box.createVerticalStrut(18));
        form.add(createLabeledField("WALLET BOOTH NUMBER", withdrawBoothField));
        form.add(Box.createVerticalStrut(12));
        form.add(createLabeledField("AMOUNT", withdrawAmountField));
        form.add(Box.createVerticalStrut(18));
        form.add(createButton("WITHDRAW", GOLD, PANEL_DEEP, e -> handleWithdraw()));
        form.add(Box.createVerticalStrut(12));
        form.add(htmlLabel("Only amounts divisible by 100 are allowed.", 13, MUTED));

        panel.add(form, BorderLayout.CENTER);
        panel.add(createRulePanel(
            "WITHDRAWAL RULES",
            new String[] {
                "Booth number must exist in WalletBooth.txt.",
                "Amount must be a positive number.",
                "Amount must be divisible by 100.",
                "Wallet must have enough balance before cash out."
            }
        ), BorderLayout.EAST);
        return panel;
    }

    private JPanel buildTransferPanel() {
        PixelCardPanel panel = new PixelCardPanel(PANEL_ALT, PINK, true);
        panel.setLayout(new BorderLayout(16, 16));
        panel.setBorder(new EmptyBorder(22, 22, 22, 22));

        transferRecipientField = createTextField();
        transferAmountField = createTextField();

        JPanel form = transparentPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.add(pixelLabel("WARP PIPE TRANSFER", 24, PINK));
        form.add(Box.createVerticalStrut(8));
        form.add(htmlLabel("Send funds to another registered player profile.", 14, MUTED));
        form.add(Box.createVerticalStrut(18));
        form.add(createLabeledField("RECIPIENT USERNAME", transferRecipientField));
        form.add(Box.createVerticalStrut(12));
        form.add(createLabeledField("AMOUNT", transferAmountField));
        form.add(Box.createVerticalStrut(18));
        form.add(createButton("SEND COINS", PINK, PANEL_DEEP, e -> handleTransfer()));

        panel.add(form, BorderLayout.CENTER);
        panel.add(createRulePanel(
            "TRANSFER RULES",
            new String[] {
                "Recipient username must already exist.",
                "Sender cannot transfer to the same account.",
                "Amount must be a positive number.",
                "Both wallets receive updated transaction history."
            }
        ), BorderLayout.EAST);
        return panel;
    }

    private JPanel buildHistoryPanel() {
        PixelDisplayPanel panel = new PixelDisplayPanel();
        panel.setPreferredSize(new Dimension(330, 0));
        panel.setLayout(new BorderLayout(14, 14));
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel top = transparentPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(pixelLabel("ACTIVITY LOG", 22, TEXT));
        top.add(Box.createVerticalStrut(6));
        top.add(htmlLabel("Recent wallet actions are listed newest first.", 13, MUTED));

        historyArea = createConsoleArea();
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);

        JPanel bottom = transparentPanel(new GridLayout(1, 2, 10, 10));
        bottom.add(createButton("REFRESH", CYAN, PANEL_DEEP, e -> refreshUserData()));
        bottom.add(createButton("LOGOUT", RED, PANEL_DEEP, e -> logout()));

        panel.add(top, BorderLayout.NORTH);
        panel.add(wrapScroll(historyArea), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildAdminScreen() {
        JPanel screen = transparentPanel(new BorderLayout(20, 20));

        PixelCardPanel top = new PixelCardPanel(PANEL_ALT, PINK, true);
        top.setLayout(new BorderLayout(18, 18));
        top.setBorder(new EmptyBorder(22, 22, 22, 22));

        JPanel left = transparentPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(pixelLabel("ADMIN OVERWORLD", 28, TEXT));
        left.add(Box.createVerticalStrut(8));
        left.add(htmlLabel("Browse every registered user and current balance from the retro admin console.", 14, MUTED));

        JPanel buttons = transparentPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.add(createButton("REFRESH", CYAN, PANEL_DEEP, e -> refreshAdminData()));
        buttons.add(createButton("BACK TO LOGIN", PINK, PANEL_DEEP, e -> {
            setStatus("Returned to the login screen.");
            screenCards.show(screenRoot, AUTH_SCREEN);
        }));

        top.add(left, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.EAST);

        PixelDisplayPanel center = new PixelDisplayPanel();
        center.setLayout(new BorderLayout(14, 14));
        center.setBorder(new EmptyBorder(18, 18, 18, 18));
        adminArea = createConsoleArea();
        adminArea.setFont(pixelFont(15));
        center.add(wrapScroll(adminArea), BorderLayout.CENTER);

        screen.add(top, BorderLayout.NORTH);
        screen.add(center, BorderLayout.CENTER);
        return screen;
    }

    private JPanel buildStatusBar() {
        PixelCardPanel bar = new PixelCardPanel(PANEL, OUTLINE, false);
        bar.setLayout(new BorderLayout());
        bar.setBorder(new EmptyBorder(10, 14, 10, 14));

        statusLabel = new JLabel("Welcome to Wallet.");
        statusLabel.setFont(pixelFont(13));
        statusLabel.setForeground(TEXT);
        bar.add(statusLabel, BorderLayout.WEST);
        return bar;
    }

    private JPanel createMetricTile(String title, JLabel value, Color accent) {
        PixelCardPanel tile = new PixelCardPanel(PANEL_DEEP, accent, false);
        tile.setLayout(new BoxLayout(tile, BoxLayout.Y_AXIS));
        tile.setBorder(new EmptyBorder(16, 16, 16, 16));
        JLabel titleLabel = pixelLabel(title, 13, accent);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        value.setAlignmentX(Component.LEFT_ALIGNMENT);
        tile.add(titleLabel);
        tile.add(Box.createVerticalStrut(10));
        tile.add(value);
        return tile;
    }

    private JLabel createFeatureChip(String text, Color accent) {
        JLabel label = pixelLabel(text, 14, accent);
        label.setOpaque(true);
        label.setBackground(PANEL_DEEP);
        label.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(accent, 2),
            new EmptyBorder(8, 10, 8, 10)
        ));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createMiniInfoTile(String title, String value) {
        PixelCardPanel tile = new PixelCardPanel(PANEL_DEEP, OUTLINE, false);
        tile.setLayout(new BoxLayout(tile, BoxLayout.Y_AXIS));
        tile.setBorder(new EmptyBorder(12, 12, 12, 12));
        tile.add(pixelLabel(title, 12, MUTED));
        tile.add(Box.createVerticalStrut(6));
        tile.add(pixelLabel(value, 15, TEXT));
        return tile;
    }

    private JButton createNavButton(String text, String target, Color accent) {
        return createButton(text, accent, PANEL_DEEP, e -> dashboardCards.show(dashboardContent, target));
    }

    private JButton createButton(String text, Color background, Color foreground) {
        PixelButton button = new PixelButton(text, background, foreground);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        return button;
    }

    private JButton createButton(String text, Color background, Color foreground, java.awt.event.ActionListener listener) {
        JButton button = createButton(text, background, foreground);
        button.addActionListener(listener);
        return button;
    }

    private JPanel createLabeledField(String label, Container field) {
        JPanel panel = transparentPanel(new BorderLayout(0, 6));
        JLabel title = pixelLabel(label, 13, MUTED);
        panel.add(title, BorderLayout.NORTH);
        panel.add((Component) field, BorderLayout.CENTER);
        return panel;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        styleInput(field);
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        styleInput(field);
        return field;
    }

    private void styleInput(JTextField field) {
        field.setFont(pixelFont(15));
        field.setForeground(TEXT);
        field.setCaretColor(PINK);
        field.setBackground(PANEL_DEEP);
        field.setSelectedTextColor(PANEL_DEEP);
        field.setSelectionColor(CYAN);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(OUTLINE, 2),
            new EmptyBorder(10, 12, 10, 12)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JTextArea createConsoleArea() {
        JTextArea area = new JTextArea();
        area.setFont(pixelFont(14));
        area.setBackground(PANEL_DEEP);
        area.setForeground(TEXT);
        area.setCaretColor(CYAN);
        area.setBorder(new EmptyBorder(14, 14, 14, 14));
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    private JScrollPane wrapScroll(JTextArea area) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(new LineBorder(OUTLINE, 2));
        scroll.getViewport().setBackground(PANEL_DEEP);
        scroll.setPreferredSize(new Dimension(290, 170));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private JPanel createRulePanel(String title, String[] rules) {
        PixelCardPanel card = new PixelCardPanel(PANEL_DEEP, OUTLINE, false);
        card.setLayout(new BorderLayout(10, 10));
        card.setPreferredSize(new Dimension(320, 0));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel heading = pixelLabel(title, 17, TEXT);
        JTextArea copy = createConsoleArea();
        copy.setText(formatRules(rules));
        copy.setBorder(new EmptyBorder(6, 6, 6, 6));

        card.add(heading, BorderLayout.NORTH);
        card.add(wrapScroll(copy), BorderLayout.CENTER);
        return card;
    }

    private JPanel createInfoPanel(String title, String text) {
        PixelCardPanel card = new PixelCardPanel(PANEL_DEEP, OUTLINE, false);
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel heading = pixelLabel(title, 16, TEXT);
        JTextArea copy = createConsoleArea();
        copy.setText(text);
        copy.setRows(7);
        copy.setBorder(new EmptyBorder(6, 6, 6, 6));

        card.add(heading, BorderLayout.NORTH);
        card.add(wrapScroll(copy), BorderLayout.CENTER);
        return card;
    }

    private String formatRules(String[] rules) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rules.length; i++) {
            builder.append(i + 1).append(". ").append(rules[i]);
            if (i < rules.length - 1) {
                builder.append("\n\n");
            }
        }
        return builder.toString();
    }

    private JLabel pixelLabel(String text, int size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(pixelFont(size));
        label.setForeground(color);
        return label;
    }

    private JLabel htmlLabel(String text, int size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Dialog", Font.PLAIN, size));
        label.setForeground(color);
        return label;
    }

    private Font pixelFont(int size) {
        return new Font("Monospaced", Font.BOLD, size);
    }

    private JPanel transparentPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    private JPanel transparentPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setOpaque(false);
        return panel;
    }

    private void handleRegister() {
        WalletResult result = service.register(registerUsernameField.getText(), new String(registerPasswordField.getPassword()));
        setStatus(result.getMessage());
        showFeedback(result);
        if (result.isSuccess()) {
            registerUsernameField.setText("");
            registerPasswordField.setText("");
        }
    }

    private void handleLogin() {
        String username = loginUsernameField.getText().trim();
        String password = new String(loginPasswordField.getPassword());
        WalletResult result = service.login(username, password);

        if (!result.isSuccess()) {
            setStatus(result.getMessage());
            showFeedback(result);
            return;
        }

        if (service.isAdminCredentials(username, password)) {
            refreshAdminData();
            screenCards.show(screenRoot, ADMIN_SCREEN);
            setStatus("Admin dashboard opened.");
            return;
        }

        currentUser = result.getUser();
        refreshUserData();
        dashboardCards.show(dashboardContent, DASH_OVERVIEW);
        screenCards.show(screenRoot, USER_SCREEN);
        setStatus("Welcome back, " + currentUser.getUsername() + ".");
    }

    private void handleDeposit() {
        WalletResult result = service.deposit(
            currentUser,
            depositAmountField.getText(),
            depositBankField.getText(),
            new String(depositPinField.getPassword())
        );
        afterWalletAction(result);
        if (result.isSuccess()) {
            depositAmountField.setText("");
            depositBankField.setText("");
            depositPinField.setText("");
        }
    }

    private void handleWithdraw() {
        WalletResult result = service.withdraw(
            currentUser,
            withdrawBoothField.getText(),
            withdrawAmountField.getText()
        );
        afterWalletAction(result);
        if (result.isSuccess()) {
            withdrawBoothField.setText("");
            withdrawAmountField.setText("");
        }
    }

    private void handleTransfer() {
        WalletResult result = service.transfer(
            currentUser,
            transferRecipientField.getText(),
            transferAmountField.getText()
        );
        afterWalletAction(result);
        if (result.isSuccess()) {
            transferRecipientField.setText("");
            transferAmountField.setText("");
        }
    }

    private void afterWalletAction(WalletResult result) {
        setStatus(result.getMessage());
        showFeedback(result);
        if (result.isSuccess()) {
            refreshUserData();
        }
    }

    private void refreshUserData() {
        if (currentUser == null) {
            return;
        }

        String balance = service.getFormattedBalance(currentUser);
        userWelcomeLabel.setText("WELCOME, " + currentUser.getUsername().toUpperCase());
        userBalanceLabel.setText(balance);
        overviewUserValue.setText(currentUser.getUsername().toUpperCase());
        overviewBalanceValue.setText(balance);
        overviewTransactionsValue.setText(service.getFormattedTransactionCount(currentUser));

        List<String> history = service.getTransactionHistory(currentUser);
        if (history.isEmpty()) {
            historyArea.setText("No transactions recorded yet.");
        } else {
            StringBuilder builder = new StringBuilder();
            for (String item : history) {
                builder.append("> ").append(item).append("\n\n");
            }
            historyArea.setText(builder.toString().trim());
        }
        historyArea.setCaretPosition(0);
    }

    private void refreshAdminData() {
        List<User> users = service.getAllUsers();
        StringBuilder builder = new StringBuilder();
        builder.append("USERNAME             | BALANCE\n");
        builder.append("---------------------+---------------------\n");
        for (User user : users) {
            builder.append(String.format("%-20s | %-20s%n", user.getUsername(), service.getFormattedBalance(user)));
        }
        if (users.isEmpty()) {
            builder.append("No registered accounts found.");
        }
        adminArea.setText(builder.toString());
        adminArea.setCaretPosition(0);
    }

    private void logout() {
        currentUser = null;
        loginPasswordField.setText("");
        screenCards.show(screenRoot, AUTH_SCREEN);
        setStatus("Logged out successfully.");
    }

    private void setStatus(String message) {
        statusLabel.setText(message);
    }

    private void showFeedback(WalletResult result) {
        JOptionPane.showMessageDialog(
            this,
            result.getMessage(),
            result.isSuccess() ? "Wallet" : "Action Required",
            result.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE
        );
    }

    private static class PixelButton extends JButton {
        private final Color fill;
        private final Color ink;

        PixelButton(String text, Color fill, Color ink) {
            super(text);
            this.fill = fill;
            this.ink = ink;
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setForeground(ink);
            setFont(new Font("Monospaced", Font.BOLD, 15));
            setHorizontalAlignment(SwingConstants.CENTER);
            setMargin(new Insets(12, 14, 12, 14));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            int shadow = 4;
            Color top = fill.brighter();
            Color border = fill.darker().darker();

            g2.setColor(border);
            g2.fillRect(4, 4 + shadow, getWidth() - 8, getHeight() - 8);
            g2.setColor(fill);
            g2.fillRect(2, 2, getWidth() - 8, getHeight() - 8);
            g2.setColor(top);
            g2.fillRect(2, 2, getWidth() - 8, 6);
            g2.setColor(new Color(255, 255, 255, 45));
            for (int x = 8; x < getWidth() - 12; x += 12) {
                g2.fillRect(x, 12, 6, 2);
            }

            if (getModel().isPressed()) {
                g2.translate(2, 2);
            }
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        public void setForeground(Color fg) {
            super.setForeground(fg);
        }
    }

    private static class PixelCardPanel extends JPanel {
        private final Color fill;
        private final Color outline;
        private final boolean stripes;

        PixelCardPanel(Color fill, Color outline, boolean stripes) {
            this.fill = fill;
            this.outline = outline;
            this.stripes = stripes;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(4, 8, 24, 200));
            g2.fillRect(6, 6, getWidth() - 6, getHeight() - 6);

            g2.setColor(fill);
            g2.fillRect(0, 0, getWidth() - 8, getHeight() - 8);

            g2.setColor(outline);
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(1, 1, getWidth() - 11, getHeight() - 11);
            g2.drawRect(4, 4, getWidth() - 17, getHeight() - 17);

            if (stripes) {
                g2.setColor(new Color(255, 255, 255, 16));
                for (int y = 10; y < getHeight() - 18; y += 10) {
                    g2.fillRect(12, y, getWidth() - 36, 3);
                }
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private class PixelDisplayPanel extends PixelCardPanel {
        PixelDisplayPanel() {
            super(PANEL, OUTLINE, true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setColor(new Color(26, 48, 110));
            for (int i = 0; i < getWidth(); i += 48) {
                int height = 28 + (i / 24 % 4) * 16;
                g2.fillRect(i, getHeight() - height - 8, 34, height);
            }

            paintPixelCloud(g2, 40, 44, CYAN);
            paintPixelCloud(g2, 220, 70, PINK);
            paintQuestionBlock(g2, 76, getHeight() - 118, GOLD);
            paintQuestionBlock(g2, 124, getHeight() - 118, RED);
            paintMushroom(g2, 70, getHeight() - 76);
            paintMushroom(g2, 132, getHeight() - 76);
            paintCoin(g2, getWidth() - 82, 46);
            paintAtmScene(g2, getWidth() - 220, getHeight() - 250, introFrame);
            g2.dispose();
        }
    }

    private static class PixelScenePanel extends JPanel {
        PixelScenePanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            g2.setColor(BG_TOP);
            g2.fillRect(0, 0, getWidth(), getHeight());

            for (int y = 0; y < getHeight(); y += 28) {
                int blend = Math.min(120, y / 6);
                g2.setColor(new Color(
                    Math.min(30, BG_TOP.getRed() + blend / 8),
                    Math.min(60, BG_TOP.getGreen() + blend / 4),
                    Math.min(120, BG_TOP.getBlue() + blend / 2)
                ));
                g2.fillRect(0, y, getWidth(), 28);
            }

            for (int i = 0; i < 55; i++) {
                int x = (i * 137) % Math.max(1, getWidth() - 20);
                int y = 30 + ((i * 79) % Math.max(1, getHeight() / 2));
                g2.setColor(i % 3 == 0 ? PINK : (i % 3 == 1 ? CYAN : MUTED));
                g2.fillRect(x, y, 3, 3);
            }

            g2.setColor(new Color(39, 73, 148));
            g2.fillRect(getWidth() - 240, 46, 120, 120);
            g2.setColor(new Color(12, 20, 54));
            g2.fillRect(getWidth() - 184, 46, 12, 120);
            g2.fillRect(getWidth() - 240, 102, 120, 12);
            g2.setColor(new Color(78, 168, 255, 90));
            g2.fillRect(getWidth() - 236, 50, 112, 112);

            for (int i = 0; i < getWidth(); i += 56) {
                int base = getHeight() - 54 - (i / 56 % 2) * 10;
                g2.setColor(new Color(18, 34, 86));
                g2.fillRect(i, base, 42, 54);
            }

            paintQuestionBlock(g2, 72, 52, PINK);
            paintQuestionBlock(g2, 136, 52, PINK);
            paintShelf(g2, 92, 190, 164);
            paintShelf(g2, 286, 214, 178);
            paintPlant(g2, 308, 180);
            paintCoin(g2, 412, 152);
            paintMushroom(g2, getWidth() - 184, getHeight() - 118);
            paintMushroom(g2, getWidth() - 126, getHeight() - 106);
            paintDesk(g2, getWidth() / 2 - 220, getHeight() - 170, 470);
            paintMonitor(g2, getWidth() / 2 - 120, getHeight() - 248, 220, 120);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static void paintQuestionBlock(Graphics2D g2, int x, int y, Color accent) {
        g2.setColor(new Color(8, 14, 40));
        g2.fillRect(x + 4, y + 4, 44, 44);
        g2.setColor(accent);
        g2.fillRect(x, y, 44, 44);
        g2.setColor(accent.brighter());
        g2.fillRect(x, y, 44, 6);
        g2.setColor(new Color(22, 27, 58));
        g2.fillRect(x + 14, y + 10, 16, 8);
        g2.fillRect(x + 22, y + 18, 8, 12);
        g2.fillRect(x + 14, y + 26, 8, 8);
        g2.fillRect(x + 24, y + 32, 6, 6);
    }

    private static void paintCoin(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(133, 64, 0));
        g2.fillRect(x + 2, y + 2, 18, 26);
        g2.setColor(GOLD);
        g2.fillRect(x, y, 18, 26);
        g2.setColor(new Color(255, 235, 130));
        g2.fillRect(x + 4, y + 4, 10, 18);
    }

    private static void paintMushroom(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(84, 28, 22));
        g2.fillRect(x + 4, y + 12, 34, 18);
        g2.setColor(RED);
        g2.fillRect(x, y, 42, 18);
        g2.setColor(new Color(255, 221, 225));
        g2.fillRect(x + 8, y + 18, 24, 18);
        g2.setColor(Color.WHITE);
        g2.fillRect(x + 8, y + 4, 8, 8);
        g2.fillRect(x + 24, y + 6, 8, 8);
    }

    private static void paintPlant(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(22, 90, 44));
        g2.fillRect(x + 14, y, 10, 36);
        g2.fillRect(x, y + 10, 14, 10);
        g2.fillRect(x + 24, y + 8, 14, 10);
        g2.setColor(new Color(57, 120, 214));
        g2.fillRect(x + 4, y + 36, 28, 18);
    }

    private static void paintShelf(Graphics2D g2, int x, int y, int width) {
        g2.setColor(new Color(141, 98, 78));
        g2.fillRect(x, y, width, 10);
        g2.fillRect(x + 16, y + 10, 8, 18);
        g2.fillRect(x + width - 24, y + 10, 8, 18);
    }

    private static void paintDesk(Graphics2D g2, int x, int y, int width) {
        g2.setColor(new Color(26, 56, 114));
        g2.fillRect(x, y, width, 14);
        g2.setColor(new Color(18, 34, 78));
        g2.fillRect(x + 18, y + 14, 12, 92);
        g2.fillRect(x + width - 30, y + 14, 12, 92);
        g2.fillRect(x + 90, y + 14, width - 180, 20);
    }

    private static void paintMonitor(Graphics2D g2, int x, int y, int width, int height) {
        g2.setColor(new Color(69, 165, 255));
        g2.fillRect(x - 6, y - 6, width + 12, height + 12);
        g2.setColor(new Color(235, 242, 255));
        g2.fillRect(x, y, width, height);
        g2.setColor(new Color(80, 112, 200));
        g2.fillRect(x + 10, y + 12, width - 20, 14);
        g2.setColor(new Color(144, 168, 229));
        for (int i = 0; i < 6; i++) {
            g2.fillRect(x + 14, y + 36 + i * 12, width - 34, 6);
        }
        g2.setColor(new Color(59, 156, 103));
        g2.fillRect(x + 18, y + 46, 12, 28);
        g2.setColor(new Color(210, 74, 96));
        g2.fillRect(x + 42, y + 60, 12, 18);
    }

    private static void paintAtmScene(Graphics2D g2, int x, int y, int frame) {
        g2.setColor(new Color(10, 18, 46));
        g2.fillRect(x - 18, y + 118, 170, 12);

        g2.setColor(new Color(67, 126, 214));
        g2.fillRect(x + 70, y, 74, 110);
        g2.setColor(new Color(128, 188, 255));
        g2.fillRect(x + 78, y + 8, 58, 34);
        g2.setColor(new Color(18, 43, 88));
        g2.fillRect(x + 84, y + 50, 46, 8);
        g2.fillRect(x + 84, y + 66, 46, 8);
        g2.fillRect(x + 90, y + 86, 34, 6);
        g2.setColor(new Color(255, 203, 74));
        g2.fillRect(x + 110, y + 92, 18, 6);

        int walkCycle = frame % 20;
        int moneyPhase = frame >= 20 ? 1 : 0;
        int armOffset = walkCycle < 10 ? 0 : 2;
        int legOffset = walkCycle < 10 ? 0 : 2;

        g2.setColor(new Color(173, 94, 52));
        g2.fillRect(x + 10, y + 28, 12, 12);
        g2.setColor(RED);
        g2.fillRect(x + 6, y + 18, 20, 8);
        g2.fillRect(x + 4, y + 40, 24, 14);
        g2.setColor(new Color(30, 83, 188));
        g2.fillRect(x + 8, y + 54, 20, 20);
        g2.setColor(new Color(255, 214, 178));
        g2.fillRect(x + 24, y + 42 + armOffset, 8, 8);
        g2.fillRect(x + 2, y + 42 + (2 - armOffset), 8, 8);
        g2.setColor(new Color(96, 58, 27));
        g2.fillRect(x + 8, y + 74, 8, 18 + legOffset);
        g2.fillRect(x + 20, y + 74, 8, 18 + (2 - legOffset));

        if (moneyPhase == 1) {
            g2.setColor(GREEN);
            g2.fillRect(x + 34, y + 42, 16, 8);
            g2.setColor(new Color(205, 255, 205));
            g2.fillRect(x + 38, y + 44, 8, 2);
        }
    }

    private static void paintPixelCloud(Graphics2D g2, int x, int y, Color color) {
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 110));
        g2.fillRect(x, y, 18, 8);
        g2.fillRect(x + 14, y - 8, 18, 8);
        g2.fillRect(x + 28, y, 18, 8);
        g2.fillRect(x + 10, y + 8, 22, 8);
    }
}
