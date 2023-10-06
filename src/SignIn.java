import javax.management.ObjectInstance;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.ObjIntConsumer;

import static ProjectUtiles.Utiles.*;

class CryptoPrice {
    Random random = new Random();
    double cryptoPrice = random.nextInt(27000, 30000) + Math.random();
}

public class SignIn extends JFrame implements ActionListener {
    Container container;

    //    Email
    JLabel email = new JLabel("Email : ");
    JTextField emailField = new JTextField(50);

    //    Password
    JLabel password = new JLabel("Password : ");
    JPasswordField passwordField = new JPasswordField(50);

    //    Buttons
    JButton login = new JButton("LogIn");
    JButton clear = new JButton("Clear");
    JButton exit = new JButton("Exit");

    SignIn() {
        this.setTitle("SignIn Page");
        this.setSize(500, 200);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container = getContentPane();
        container.setLayout(new BorderLayout());

        JPanel firstSection = new JPanel();
        firstSection.setLayout(null);

//        Email
        email.setBounds(30, 20, 80, 25);
        emailField.setBounds(200, 20, 265, 25);
        firstSection.add(email);
        firstSection.add(emailField);

//        Password
        password.setBounds(30, 60, 80, 25);
        passwordField.setBounds(200, 60, 265, 25);
        firstSection.add(password);
        firstSection.add(passwordField);


//        Button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(login);
        login.addActionListener(this);
        buttonPanel.add(clear);
        clear.addActionListener(this);
        buttonPanel.add(exit);
        exit.addActionListener(this);

        container.add(firstSection, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(login)) {
            try {
                Connection connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectAll);

                int findingUser = 0;
                while (resultSet.next()) {
                    if (emailField.getText().equals(resultSet.getString("email")) || passwordField.getPassword().equals(resultSet.getString("password"))) {
                        findingUser++;
                    }
                }
                if (findingUser != 0) {
                    if (checkPassword(emailField.getText(), passwordField.getText())) {
                        Menu menu = new Menu(emailField.getText());
                        menu.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Email or Password is wrong!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, emailField.getText() + " is not found!");
                }


            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource().equals(clear)) {
            emailField.setText("");
            passwordField.setText("");
        } else if (e.getSource().equals(exit)) {
            Home home = new Home();
            home.setVisible(true);
            dispose();
        }
    }
}

//Menu Function
class Menu extends JFrame implements ActionListener {
    Container menuContainer;
    String emailField;

    String realName = "";
    String userName = "";

    //    Greeting
    JLabel user = new JLabel();
    //    Buttons
    JButton cashIn = new JButton("Cash in");
    JButton checkData = new JButton("Check Data");
    JButton buyCrypto = new JButton("Buy Crypto");
    JButton sellCrypto = new JButton("Sell Crypto");
    JButton history = new JButton("History");
    JButton logOut = new JButton("LogOut");

    Menu(String emailField) {
        this.emailField = emailField;
        this.setTitle("Menu Page");
        this.setSize(360, 350);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuContainer = getContentPane();
        menuContainer.setLayout(new GridLayout(7, 1));

        JPanel cashInPanel = new JPanel(null);
        JPanel checkDataPanel = new JPanel(null);
        JPanel buyCryptoPanel = new JPanel(null);
        JPanel sellCryptoPanel = new JPanel(null);
        JPanel historyPanel = new JPanel(null);
        JPanel logOutPanel = new JPanel(null);

        cashIn.setBounds(80, 2, 200, 30);
        checkData.setBounds(80, 4, 200, 30);
        buyCrypto.setBounds(80, 6, 200, 30);
        sellCrypto.setBounds(80, 8, 200, 30);
        history.setBounds(80, 10, 200, 30);
        logOut.setBounds(80, 12, 200, 30);

        cashInPanel.add(cashIn);
        cashIn.addActionListener(this);
        checkDataPanel.add(checkData);
        checkData.addActionListener(this);
        buyCryptoPanel.add(buyCrypto);
        buyCrypto.addActionListener(this);
        sellCryptoPanel.add(sellCrypto);
        sellCrypto.addActionListener(this);
        historyPanel.add(history);
        history.addActionListener(this);
        logOutPanel.add(logOut);
        logOut.addActionListener(this);

        try {
            Connection connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectAll);
            while (resultSet.next()) {
                if (emailField.equals(resultSet.getString("email"))) {
                    realName = resultSet.getString("realname");
                    userName = resultSet.getString("username");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        menuContainer.add(user);
        user.setText("Welcome, " + realName + " (" + userName + ")");
        menuContainer.add(cashInPanel);
        menuContainer.add(checkDataPanel);
        menuContainer.add(buyCryptoPanel);
        menuContainer.add(sellCryptoPanel);
        menuContainer.add(historyPanel);
        menuContainer.add(logOutPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cashIn)) {
            CashIn cashIn1 = new CashIn(emailField);
            cashIn1.setVisible(true);
            dispose();
        } else if (e.getSource().equals(checkData)) {
            CheckData checkData1 = new CheckData(emailField);
            checkData1.setVisible(true);
            dispose();
        } else if (e.getSource().equals(buyCrypto)) {
            BuyCrypto buyCrypto1 = new BuyCrypto(emailField);
            buyCrypto1.setVisible(true);
            dispose();
        } else if (e.getSource().equals(sellCrypto)) {
            SellCrypto sellCrypto1 = new SellCrypto(emailField);
            sellCrypto1.setVisible(true);
            dispose();
        } else if (e.getSource().equals(history)) {
            ViewHistory viewHistory = new ViewHistory(emailField);
            viewHistory.setVisible(true);
            dispose();
        } else if (e.getSource().equals(logOut)) {
            Home home = new Home();
            home.setVisible(true);
            dispose();
        }
    }
}

//Cash in
class CashIn extends JFrame implements ActionListener {
    Container cashInContainer;
    String emailField;

    JLabel amount = new JLabel("Amount : ");
    JTextField amountField = new JTextField(50);

    JLabel password = new JLabel("Password : ");
    JPasswordField passwordField = new JPasswordField(50);

    //    Buttons
    JButton submit = new JButton("Submit");
    JButton clear = new JButton("Clear");
    JButton exit = new JButton("Exit");

    CashIn(String emailField) {
        this.emailField = emailField;
        this.setTitle("CashIn Page");
        this.setSize(500, 200);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cashInContainer = getContentPane();
        cashInContainer.setLayout(new BorderLayout());

        JPanel firstSection = new JPanel(null);

//        Amount
        amount.setBounds(30, 20, 80, 25);
        amountField.setBounds(200, 20, 265, 25);
        firstSection.add(amount);
        firstSection.add(amountField);

//        Password
        password.setBounds(30, 60, 80, 25);
        passwordField.setBounds(200, 60, 265, 25);
        firstSection.add(password);
        firstSection.add(passwordField);

//        Button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(submit);
        submit.addActionListener(this);
        buttonPanel.add(clear);
        clear.addActionListener(this);
        buttonPanel.add(exit);
        exit.addActionListener(this);

        cashInContainer.add(firstSection, BorderLayout.CENTER);
        cashInContainer.add(buttonPanel, BorderLayout.SOUTH);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(submit)) {
            if (checkPassword(emailField, passwordField.getText())) {
                try {
                    Connection connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(targetSelect(emailField, passwordField.getText()));

                    double db_balance = 0.0;
                    while (resultSet.next()) {
                        db_balance = resultSet.getDouble("balance") + Double.parseDouble(amountField.getText());
                    }


                    statement.executeUpdate("UPDATE user_info SET balance = " + db_balance + " where email = '" + emailField + "';");
                    JOptionPane.showMessageDialog(null, "CashIn Process is Done Successfully!");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Password is Wrong!");
            }
        } else if (e.getSource().equals(clear)) {
            amountField.setText("");
            passwordField.setText("");
        } else if (e.getSource().equals(exit)) {
            Menu menu = new Menu(emailField);
            menu.setVisible(true);
            dispose();
        }
    }
}

//Check Data
class CheckData extends JFrame implements ActionListener {
    Container checkDataContainer;
    static String emailField;

    //    Password
    JLabel password = new JLabel("Password : ");
    JPasswordField passwordField = new JPasswordField(50);

    //    Change Password
    JButton changePassword = new JButton("Change Password");

    //    Delete Account
    JButton deleteAccount = new JButton("Delete Account!");

    //    Buttons
    JButton submit = new JButton("Submit");
    JButton clear = new JButton("Clear");
    JButton exit = new JButton("Exit");

    CheckData(String emailField) {
        this.emailField = emailField;
        this.setTitle("Check Data Page");
        this.setSize(500, 200);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        checkDataContainer = getContentPane();
        checkDataContainer.setLayout(new BorderLayout());

        JPanel firstSection = new JPanel(null);
        //        Password
        password.setBounds(30, 20, 80, 25);
        passwordField.setBounds(200, 20, 265, 25);
        firstSection.add(password);
        firstSection.add(passwordField);

        changePassword.setBounds(60, 80, 150, 25);
        firstSection.add(changePassword);
        changePassword.addActionListener(this);
        deleteAccount.setBounds(290, 80, 150, 25);
        firstSection.add(deleteAccount);
        deleteAccount.addActionListener(this);

        //        Button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(submit);
        submit.addActionListener(this);
        buttonPanel.add(clear);
        clear.addActionListener(this);
        buttonPanel.add(exit);
        exit.addActionListener(this);

        checkDataContainer.add(firstSection, BorderLayout.CENTER);
        checkDataContainer.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(submit)) {
            if (checkPassword(emailField, passwordField.getText())) {
                try {
                    Connection connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(targetSelect(emailField, passwordField.getText()));

                    String db_name = "";
                    String db_username = "";
                    String db_email = "";
                    String db_phoneNo = "";
                    String db_DOB = "";
                    double db_balance = 0.0;
                    double db_cryptoAmount = 0.0;
                    while (resultSet.next()) {
                        db_name = resultSet.getString("realname");
                        db_username = resultSet.getString("username");
                        db_email = resultSet.getString("email");
                        db_phoneNo = resultSet.getString("phone_no");
                        db_DOB = resultSet.getString("DOB");
                        db_balance = resultSet.getDouble("balance");
                        db_cryptoAmount = resultSet.getDouble("crypto_amount");
                    }

                    JOptionPane.showMessageDialog(null, """
                            Your information : \s
                            Name                :  %s
                            Username            :  %s
                            Email               :  %s
                            Phone number        :  %s
                            Date of Birth       :  %s
                            Balance             :  %f
                            Crypto amount       :  %f
                            """.formatted(db_name, db_username, db_email, db_phoneNo, db_DOB, db_balance, db_cryptoAmount));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Password is wrong!");
            }

        } else if (e.getSource().equals(changePassword)) {
            ChangePassword changePassword1 = new ChangePassword();
            changePassword1.setVisible(true);
            dispose();
        } else if (e.getSource().equals(deleteAccount)) {
            String userEnteredPassword = JOptionPane.showInputDialog("Enter password : ");
            String userPassword = "";
            try {
                Connection connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectAll);
                while (resultSet.next()) {
                    if (emailField.equals(resultSet.getString("email"))) {
                        userPassword = resultSet.getString("password");
                    }
                }
                if (userPassword.equals(userEnteredPassword)) {
                    statement.executeUpdate("DELETE FROM history WHERE email = '" + emailField + "';");
                    statement.executeUpdate("DELETE FROM user_info WHERE email = '" + emailField + "';");
                }
                Home home = new Home();
                home.setVisible(true);
                dispose();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        } else if (e.getSource().equals(clear)) {
            passwordField.setText("");
        } else if (e.getSource().equals(exit)) {
            Menu menu = new Menu(emailField);
            menu.setVisible(true);
            dispose();
        }
    }
}

class ChangePassword extends JFrame implements ActionListener {
    Container changePasswordContainer;

    //    Current Password
    JLabel currentPassword = new JLabel("Current password : ");
    JPasswordField currentPasswordField = new JPasswordField(50);

    //        New Password
    JLabel newPassword = new JLabel("New password : ");
    JPasswordField newPasswordField = new JPasswordField(50);

    //        Confirm Password
    JLabel confirmPassword = new JLabel("Confirm password : ");
    JPasswordField confirmPasswordField = new JPasswordField(50);

    //    Buttons
    JButton change = new JButton("Change");
    JButton clear = new JButton("Clear");
    JButton exit = new JButton("Exit");

    ChangePassword() {
        this.setTitle("Check Data Page");
        this.setSize(500, 200);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        changePasswordContainer = getContentPane();
        changePasswordContainer.setLayout(new BorderLayout());

        JPanel firstSection = new JPanel(null);
        //       Current Password
        currentPassword.setBounds(20, 10, 200, 25);
        currentPasswordField.setBounds(250, 10, 265, 25);
        firstSection.add(currentPassword);
        firstSection.add(currentPasswordField);

        //       New Password
        newPassword.setBounds(20, 50, 200, 25);
        newPasswordField.setBounds(250, 50, 265, 25);
        firstSection.add(newPassword);
        firstSection.add(newPasswordField);

        //       Confirm Password
        confirmPassword.setBounds(20, 90, 200, 25);
        confirmPasswordField.setBounds(250, 90, 265, 25);
        firstSection.add(confirmPassword);
        firstSection.add(confirmPasswordField);


        //        Button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(change);
        change.addActionListener(this);
        buttonPanel.add(clear);
        clear.addActionListener(this);
        buttonPanel.add(exit);
        exit.addActionListener(this);

        changePasswordContainer.add(firstSection, BorderLayout.CENTER);
        changePasswordContainer.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String passwordPattern = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";

        if (e.getSource().equals(change)) {
            String userPassword = "";
            try {
                Connection connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectAll);
                while (resultSet.next()) {
                    if (CheckData.emailField.equals(resultSet.getString("email"))) {
                        userPassword = resultSet.getString("password");
                    }
                }
                if (currentPasswordField.getText().equals(userPassword)) {
                    if (Arrays.equals(newPasswordField.getPassword(), confirmPasswordField.getPassword())) {
                        if (confirmPasswordField.getText().length() < 8) {
                            JOptionPane.showMessageDialog(null, "Password must be at least 8 characters!");
                        } else if (!(confirmPasswordField.getText().matches(passwordPattern))) {
                            JOptionPane.showMessageDialog(null, "Password is too weak! at least one upper case, special character must be contained.");
                        } else {
                            statement.executeUpdate("UPDATE user_info SET password = '" + confirmPasswordField.getText() + "' WHERE email = '" + CheckData.emailField + "';");
                            JOptionPane.showMessageDialog(null, "Changed password successfully!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Passwords are not match!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Current password is wrong!");
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource().equals(clear)) {
            currentPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        } else if (e.getSource().equals(exit)) {
            CheckData checkData = new CheckData(CheckData.emailField);
            checkData.setVisible(true);
            dispose();
        }
    }
}


class BuyCrypto extends JFrame implements ActionListener {

    Container buyCryptoContainer;
    static String emailField;
    String selectedRadio = "";


    JLabel selectCurrency = new JLabel("Select buy with : ");
    String[] radioText = {"Crypto", "Money"};
    JRadioButton[] buyRadio;
    ButtonGroup buttonGroup = new ButtonGroup();

    //    Buttons
    JButton P2P = new JButton("P2P");
    JButton agent = new JButton("Agent");
    JButton exit = new JButton("Exit");


    BuyCrypto(String emailField) {
        BuyCrypto.emailField = emailField;
        this.setTitle("Buy Crypto");
        this.setSize(315, 300);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buyCryptoContainer = getContentPane();
        buyCryptoContainer.setLayout(new GridLayout(4, 1));

        JPanel radioPanel = new JPanel(null);
        buyRadio = new JRadioButton[radioText.length];
        radioPanel.setLayout(new FlowLayout());
        radioPanel.add(selectCurrency);
        for (int i = 0; i < radioText.length; i++) {
            buyRadio[i] = new JRadioButton(radioText[i]);
            buttonGroup.add(buyRadio[i]);
            radioPanel.add(buyRadio[i]);
        }

        JPanel P2PPanel = new JPanel(null);
        P2P.setBounds(50, 10, 200, 30);
        P2PPanel.add(P2P);
        P2P.addActionListener(this);

        JPanel agentPanel = new JPanel(null);
        agent.setBounds(50, 15, 200, 30);
        agentPanel.add(agent);
        agent.addActionListener(this);

        JPanel exitPanel = new JPanel(null);
        exit.setBounds(50, 20, 200, 30);
        exitPanel.add(exit);
        exit.addActionListener(this);

        buyCryptoContainer.add(radioPanel);
        buyCryptoContainer.add(P2PPanel);
        buyCryptoContainer.add(agentPanel);
        buyCryptoContainer.add(exitPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < radioText.length; i++) {
            if (buyRadio[i].isSelected()) selectedRadio = radioText[i];
        }
        if (e.getSource().equals(exit)) {
            Menu menu = new Menu(emailField);
            menu.setVisible(true);
            dispose();
        } else if (selectedRadio.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select buying type : ");
        } else {
            if (e.getSource().equals(P2P)) {
                BuyCrypto.P2P p2p = new P2P();
                p2p.setVisible(true);
                dispose();
            } else if (e.getSource().equals(agent)) {
                Agent agent1 = new Agent();
                agent1.setVisible(true);
                dispose();
            }
        }
    }

    ArrayList<Seller> sellerArrayList;

    static class Seller {
        String name;
        int ID;

        Seller(String name, int ID) {
            this.name = name;
            this.ID = ID;
        }
    }

    //            User
    double userCrypto = 0;
    String userPassword = "";
    double userBalance = 0.0;
    String userEnteredPassword;
    int userID = 0;
    String userName = "";

    //            Seller
    int sellerID = 0;
    String sellerName = "";
    String sellerEmail = "";
    double sellerCrypto = 0.0;
    double sellerBalance = 0.0;


    //            Date And Time
    String date = "";
    String time = "";

    StringBuilder outputStr = new StringBuilder("Available sellers : \n");
    double cost = 0.0;

    class P2P extends JFrame implements ActionListener {

        Container P2PContainer;

        double selectedAmount = 0.0;
        int selectedID = 0;

        JLabel selectAmount;
        JTextField selectAmountField = new JTextField(50);

        //        Select seller ID
        JLabel selectSellerID = new JLabel("Select seller ID : ");
        JTextField selectSellerIDField = new JTextField(20);

        //        Total amount
        JLabel totalAmount;
        JTextField totalAmountField = new JTextField(50);

        //        User Balance
        JLabel yourBalance = new JLabel("Your balance : ");
        JTextField yourBalanceField = new JTextField(50);

        //        Button
        JButton enter = new JButton("Enter");
        JButton submit = new JButton("Submit");
        JButton clear = new JButton("Clear");
        JButton exit = new JButton("Exit");

        CryptoPrice cryptoPrice = new CryptoPrice();

        P2P() {
            this.setTitle("P2P Trading");
            this.setSize(550, 400);
            this.setLocation(100, 100);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            P2PContainer = getContentPane();
            P2PContainer.setLayout(new GridLayout(5, 1));

            selectAmount = new JLabel("Select " + selectedRadio + " amount : ");
            if (selectedRadio.equalsIgnoreCase("CRYPTO")) totalAmount = new JLabel("Total Amount : ");
            else if (selectedRadio.equalsIgnoreCase("MONEY")) totalAmount = new JLabel("Crypto Amount : ");

            JPanel amountPanel = new JPanel(null);
            selectAmount.setBounds(30, 2, 200, 25);
            selectAmountField.setBounds(200, 2, 200, 25);
            enter.setBounds(410, 2, 100, 25);
            amountPanel.add(selectAmount);
            amountPanel.add(selectAmountField);
            amountPanel.add(enter);
            enter.addActionListener(this);

            JPanel selectIDPanel = new JPanel(null);
            selectSellerID.setBounds(20, 0, 200, 20);
            selectSellerIDField.setBounds(200, 0, 300, 20);
            selectIDPanel.add(selectSellerID);
            selectIDPanel.add(selectSellerIDField);

            JPanel showDetailPanel = new JPanel(null);
            totalAmount.setBounds(10, 25, 200, 20);
            totalAmountField.setBounds(100, 25, 150, 20);
            showDetailPanel.add(totalAmount);
            showDetailPanel.add(totalAmountField);

            yourBalance.setBounds(260, 25, 200, 20);
            yourBalanceField.setBounds(350, 25, 150, 20);
            showDetailPanel.add(yourBalance);
            showDetailPanel.add(yourBalanceField);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(submit);
            submit.addActionListener(this);
            buttonPanel.add(clear);
            clear.addActionListener(this);
            buttonPanel.add(exit);
            exit.addActionListener(this);


            P2PContainer.add(amountPanel);
            P2PContainer.add(selectIDPanel);
            P2PContainer.add(showDetailPanel);
//            P2PContainer.add(passwordPanel);
            P2PContainer.add(buttonPanel);
        }

        @Override
        public void actionPerformed(ActionEvent e) {


            try {
                Connection connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectAll);
                while (resultSet.next()) {
                    if (emailField.equals(resultSet.getString("email"))) {
                        userBalance = resultSet.getDouble("balance");
                        userID = resultSet.getInt("id");
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            if (e.getSource().equals(enter)) {
                selectedAmount = Double.parseDouble(selectAmountField.getText());
                sellerArrayList = new ArrayList<>();
                try {
                    Connection connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(selectAll);

                    if (selectedRadio.equalsIgnoreCase("MONEY")) {
                        selectedAmount = selectedAmount / cryptoPrice.cryptoPrice;
                    }

                    cost = selectedAmount * cryptoPrice.cryptoPrice;
                    if (selectedRadio.equalsIgnoreCase("CRYPTO")) totalAmountField.setText(String.valueOf(cost));
                    else if (selectedRadio.equalsIgnoreCase("MONEY"))
                        totalAmountField.setText(String.valueOf(selectedAmount));

                    yourBalanceField.setText(String.valueOf(userBalance));

                    while (resultSet.next()) {
                        if (selectedAmount <= resultSet.getInt("crypto_amount") && userID != resultSet.getInt("id")) {
                            sellerID = resultSet.getInt("id");
                            sellerName = resultSet.getString("realname");
                            Seller seller = new Seller(sellerName, sellerID);
                            sellerArrayList.add(seller);
                        }
                    }

                    if (sellerArrayList.size() == 0) {
                        JOptionPane.showMessageDialog(null, "Sellers are unavailable right now!");
                    } else {
                        for (int i = 0; i < sellerArrayList.size(); i++) {
                            outputStr.append(String.valueOf((i + 1) + ". " + sellerArrayList.get(i).name + "      ID : " + sellerArrayList.get(i).ID + "\n"));
                        }

                        JOptionPane.showMessageDialog(null, outputStr);
                        outputStr = new StringBuilder("Available sellers : \n");
                    }


                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else if (e.getSource().equals(submit)) {
                cost = selectedAmount * cryptoPrice.cryptoPrice;
                selectedAmount = Double.parseDouble(selectAmountField.getText());
                try {
                    selectedID = Integer.parseInt(selectSellerIDField.getText());
                    try {
                        Connection connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(selectAll);

                        while (resultSet.next()) {
                            if (emailField.equals(resultSet.getString("email"))) {
                                userName = resultSet.getString("realname");
                                userCrypto = resultSet.getDouble("crypto_amount");
                                userPassword = resultSet.getString("password");
                            }

                            if (selectSellerIDField.getText().equals(String.valueOf(resultSet.getInt("id")))) {
                                sellerID = resultSet.getInt("id");
                                sellerName = resultSet.getString("realname");
                                sellerBalance = resultSet.getDouble("balance");
                                sellerCrypto = resultSet.getDouble("crypto_amount");
                                sellerEmail = resultSet.getString("email");
                            }
                        }

                        userEnteredPassword = JOptionPane.showInputDialog("Enter password : ");

                        if (selectedRadio.equalsIgnoreCase("MONEY")) {
                            cost = selectedAmount;
                            selectedAmount = selectedAmount / cryptoPrice.cryptoPrice;
                        }

                        try {
                            if (checkPassword(emailField, userEnteredPassword)) {
                                if (cost <= userBalance) {
                                    boolean sellerBool = false;
                                    for (Seller seller : sellerArrayList) {
                                        if (selectedID == seller.ID) {
                                            sellerBool = true;
                                            break;
                                        }
                                    }
                                    if (sellerBool) {
//                                User Section
                                        userBalance = userBalance - cost;
                                        userCrypto = userCrypto + selectedAmount;
                                        statement.executeUpdate("UPDATE user_info SET balance = " + userBalance + ", crypto_amount = " + userCrypto + " WHERE email = '" + emailField + "' and id = " + userID + ";");

//                                Seller Section
                                        sellerBalance = sellerBalance + cost;
                                        sellerCrypto = sellerCrypto - selectedAmount;
                                        statement.executeUpdate("UPDATE user_info SET balance = " + sellerBalance + ", crypto_amount = " + sellerCrypto + " WHERE email = '" + sellerEmail + "' and id = " + sellerID + ";");

                                        date = String.valueOf(java.time.LocalDate.now());
                                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                                        time = String.valueOf(dateTimeFormatter.format(java.time.LocalTime.now()));
//                                History Record
                                        statement.executeUpdate("INSERT INTO history(id, Receiver, sender, Transaction_date, Transaction_time, transfer_crypto_amount, cost, email, source) VALUES (" + userID + ", '" + userName + "', '" + sellerName + "', '" + date + "', '" + time + "', " + selectedAmount + ", " + cost + ", '" + emailField + "', 'P2P');");
                                        JOptionPane.showMessageDialog(null, "Buying process is successfully Done!");

                                    } else {
                                        JOptionPane.showMessageDialog(null, "Selected Seller ID is invalid!");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Your balance is not enough!");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Password is wrong!");
                            }
                        } catch (Exception ex1) {
//                        JOptionPane.showMessageDialog(null, "Please click on 'Enter' to verify!");
                            throw new RuntimeException(ex1);
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(null, "Please enter seller ID!");
                    System.out.println(ex.getMessage());
                }

            } else if (e.getSource().equals(clear)) {
                selectAmountField.setText("");
                selectSellerIDField.setText("");
                totalAmountField.setText("");
                yourBalanceField.setText("");
            } else if (e.getSource().equals(exit)) {
                BuyCrypto buyCrypto = new BuyCrypto(emailField);
                buyCrypto.setVisible(true);
                dispose();
            }

        }
    }

    class Agent extends JFrame implements ActionListener {

        Container agentContainer;

        double selectedAmount = 0.0;

        JLabel selectAmount;
        JTextField selectAmountField = new JTextField(50);

        //        Total amount
        JLabel totalAmount;
        JTextField totalAmountField = new JTextField(50);

        //        User Balance
        JLabel yourBalance = new JLabel("Your balance : ");
        JTextField yourBalanceField = new JTextField(50);

        //        Button
        JButton enter = new JButton("Enter");
        JButton submit = new JButton("Submit");
        JButton clear = new JButton("Clear");
        JButton exit = new JButton("Exit");

        CryptoPrice cryptoPrice = new CryptoPrice();

        double agentCrypto = 0.0;
        double agentBalance = 0.0;

        Agent() {
            this.setTitle("Agent Trading");
            this.setSize(550, 300);
            this.setLocation(100, 100);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            agentContainer = getContentPane();
            agentContainer.setLayout(new GridLayout(3, 1));

            selectAmount = new JLabel("Select " + selectedRadio + " amount : ");
            if (selectedRadio.equalsIgnoreCase("CRYPTO")) totalAmount = new JLabel("Total Amount : ");
            else if (selectedRadio.equalsIgnoreCase("MONEY")) totalAmount = new JLabel("Crypto Amount : ");

            JPanel amountPanel = new JPanel(null);
            selectAmount.setBounds(30, 2, 200, 25);
            selectAmountField.setBounds(200, 2, 200, 25);
            enter.setBounds(410, 2, 100, 25);
            amountPanel.add(selectAmount);
            amountPanel.add(selectAmountField);
            amountPanel.add(enter);
            enter.addActionListener(this);

            JPanel showDetailPanel = new JPanel(null);
            totalAmount.setBounds(10, 25, 200, 20);
            totalAmountField.setBounds(100, 25, 150, 20);
            showDetailPanel.add(totalAmount);
            showDetailPanel.add(totalAmountField);

            yourBalance.setBounds(260, 25, 200, 20);
            yourBalanceField.setBounds(350, 25, 150, 20);
            showDetailPanel.add(yourBalance);
            showDetailPanel.add(yourBalanceField);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(submit);
            submit.addActionListener(this);
            buttonPanel.add(clear);
            clear.addActionListener(this);
            buttonPanel.add(exit);
            exit.addActionListener(this);


            agentContainer.add(amountPanel);
            agentContainer.add(showDetailPanel);
            agentContainer.add(buttonPanel);
        }

        Connection connection;

        {
            try {
                connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectAllAgent);
                while (resultSet.next()) {
                    agentCrypto = resultSet.getDouble("amount");
                    agentBalance = resultSet.getDouble("balance");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(enter)) {
                selectedAmount = Double.parseDouble(selectAmountField.getText());
                if (selectedAmount <= agentCrypto) {
                    try {
                        Connection connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(selectAll);

                        while (resultSet.next()) {
                            if (emailField.equals(resultSet.getString("email"))) {
                                userBalance = resultSet.getDouble("balance");
                                userCrypto = resultSet.getDouble("crypto_amount");
                                userID = resultSet.getInt("id");
                                userName = resultSet.getString("realname");
                            }
                        }

                        if (selectedRadio.equalsIgnoreCase("MONEY")) {
                            selectedAmount = selectedAmount / cryptoPrice.cryptoPrice;
                        }

                        cost = selectedAmount * cryptoPrice.cryptoPrice;
                        if (selectedRadio.equalsIgnoreCase("CRYPTO")) totalAmountField.setText(String.valueOf(cost));
                        else if (selectedRadio.equalsIgnoreCase("MONEY"))
                            totalAmountField.setText(String.valueOf(selectedAmount));

                        yourBalanceField.setText(String.valueOf(userBalance));

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Agent doesn't have enough crypto!");
                }
            } else if (e.getSource().equals(submit)) {
                cost = selectedAmount * cryptoPrice.cryptoPrice;
                selectedAmount = Double.parseDouble(selectAmountField.getText());

                try {
                    Connection connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(selectAll);

                    while (resultSet.next()) {
                        if (emailField.equals(resultSet.getString("email"))) {
                            userName = resultSet.getString("realname");
                            userCrypto = resultSet.getDouble("crypto_amount");
                            userPassword = resultSet.getString("password");
                        }
                    }
                    userEnteredPassword = JOptionPane.showInputDialog("Enter password : ");

                    if (selectedRadio.equalsIgnoreCase("MONEY")) {
                        cost = selectedAmount;
                        selectedAmount = selectedAmount / cryptoPrice.cryptoPrice;
                    }

                    agentCrypto = agentCrypto - selectedAmount;
                    agentBalance = agentBalance + cost;

                    try {
                        if (checkPassword(emailField, userEnteredPassword)) {
                            if (cost <= userBalance) {
//                                User Section
                                userBalance = userBalance - cost;
                                userCrypto = userCrypto + selectedAmount;
                                statement.executeUpdate("UPDATE user_info SET balance = " + userBalance + ", crypto_amount = " + userCrypto + " WHERE email = '" + emailField + "' and id = " + userID + ";");

//                                    Agent Section
                                statement.executeUpdate("UPDATE agent SET amount = " + agentCrypto + ", balance = " + agentBalance + ";");

                                date = String.valueOf(java.time.LocalDate.now());
                                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                                time = String.valueOf(dateTimeFormatter.format(java.time.LocalTime.now()));
//                                History Record
                                statement.executeUpdate("INSERT INTO history(id, Receiver, sender, Transaction_date, Transaction_time, transfer_crypto_amount, cost, email, source) VALUES (" + userID + ", '" + userName + "', 'Agent', '" + date + "', '" + time + "', " + selectedAmount + ", " + cost + ", '" + emailField + "', 'Crypto Agent');");
                                JOptionPane.showMessageDialog(null, "Buying process is successfully Done!");
                            } else {
                                JOptionPane.showMessageDialog(null, "Your balance is not enough!");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Password is wrong!");
                        }
                    } catch (Exception ex1) {
//                        JOptionPane.showMessageDialog(null, "Please click on 'Enter' to verify!");
                        System.out.println(ex1.getMessage());
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            } else if (e.getSource().equals(clear)) {
                selectAmountField.setText("");
                totalAmountField.setText("");
                yourBalanceField.setText("");
            } else if (e.getSource().equals(exit)) {
                BuyCrypto buyCrypto = new BuyCrypto(emailField);
                buyCrypto.setVisible(true);
                dispose();
            }
        }
    }
}

class SellCrypto extends JFrame implements ActionListener {

    static String emailField;
    Container sellCryptoContainer;

    ArrayList<Buyer> buyerArrayList;

    static class Buyer {
        String name;
        int ID;

        Buyer(String name, int ID) {
            this.name = name;
            this.ID = ID;
        }
    }

    //            User
    double userCrypto = 0;
    double userBalance = 0.0;
    String userEnteredPassword;
    int userID = 0;
    String userName = "";

    //            Buyer
    int buyerID = 0;
    String buyerName = "";
    String buyerEmail = "";
    double buyerCrypto = 0.0;
    double buyerBalance = 0.0;


    //            Date And Time
    String date = "";
    String time = "";

    StringBuilder outputStr = new StringBuilder("Available buyers : \n");
    double cost = 0.0;


    double selectedAmount = 0.0;
    int selectedID = 0;

    JLabel selectAmount = new JLabel("Enter sell amount of Crypto : ");
    JTextField selectAmountField = new JTextField(50);

    //        Select seller ID
    JLabel selectBuyerID = new JLabel("Select buyer ID : ");
    JTextField selectBuyerIDField = new JTextField(20);

    //        Total amount
    JLabel totalAmount = new JLabel("Total amount to get : ");
    JTextField totalAmountField = new JTextField(50);

    //        User Balance
    JLabel yourBalance = new JLabel("Your balance : ");
    JTextField yourBalanceField = new JTextField(50);

    //        Button
    JButton enter = new JButton("Enter");
    JButton submit = new JButton("Submit");
    JButton clear = new JButton("Clear");
    JButton exit = new JButton("Exit");


    CryptoPrice cryptoPrice = new CryptoPrice();

    SellCrypto(String emailField) {
        SellCrypto.emailField = emailField;
        this.setTitle("Sell Crypto");
        this.setSize(550, 400);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sellCryptoContainer = getContentPane();
        sellCryptoContainer.setLayout(new GridLayout(4, 1));

        JPanel amountPanel = new JPanel(null);
        selectAmount.setBounds(30, 2, 200, 25);
        selectAmountField.setBounds(200, 2, 200, 25);
        enter.setBounds(410, 2, 100, 25);
        amountPanel.add(selectAmount);
        amountPanel.add(selectAmountField);
        amountPanel.add(enter);
        enter.addActionListener(this);

        JPanel selectIDPanel = new JPanel(null);
        selectBuyerID.setBounds(20, 0, 200, 20);
        selectBuyerIDField.setBounds(200, 0, 300, 20);
        selectIDPanel.add(selectBuyerID);
        selectIDPanel.add(selectBuyerIDField);

        JPanel showDetailPanel = new JPanel(null);
        totalAmount.setBounds(2, 25, 200, 20);
        totalAmountField.setBounds(120, 25, 150, 20);
        showDetailPanel.add(totalAmount);
        showDetailPanel.add(totalAmountField);

        yourBalance.setBounds(270, 25, 200, 20);
        yourBalanceField.setBounds(350, 25, 150, 20);
        showDetailPanel.add(yourBalance);
        showDetailPanel.add(yourBalanceField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(submit);
        submit.addActionListener(this);
        buttonPanel.add(clear);
        clear.addActionListener(this);
        buttonPanel.add(exit);
        exit.addActionListener(this);


        sellCryptoContainer.add(amountPanel);
        sellCryptoContainer.add(selectIDPanel);
        sellCryptoContainer.add(showDetailPanel);
        sellCryptoContainer.add(buttonPanel);
    }

    int no_users = 0;
    Connection connection = null;

    {
        try {
            connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectAll);
            while (resultSet.next()) no_users++;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectAll);
            while (resultSet.next()) {
                if (emailField.equals(resultSet.getString("email"))) {
                    userID = resultSet.getInt("id");
                    userBalance = resultSet.getDouble("balance");
                    userCrypto = resultSet.getDouble("crypto_amount");
                }
                no_users++;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        if (e.getSource().equals(enter)) {
            selectedAmount = Double.parseDouble(selectAmountField.getText());
            buyerArrayList = new ArrayList<>();

            if (userCrypto >= selectedAmount) {
                try {
                    Connection connection1 = DriverManager.getConnection(DBURL, USER, PASSWORD);
                    Statement statement = connection1.createStatement();
                    ResultSet resultSet = statement.executeQuery(selectAll);

                    cost = selectedAmount * cryptoPrice.cryptoPrice;
                    totalAmountField.setText(String.valueOf(cost));
                    yourBalanceField.setText(String.valueOf(userBalance));
                    int findBuyer = 0;
                    while (resultSet.next()) {
                        if (cost <= resultSet.getDouble("balance") && userID != resultSet.getInt("id")) {
                            buyerID = resultSet.getInt("id");
                            buyerName = resultSet.getString("realname");
                            buyerCrypto = resultSet.getDouble("crypto_amount");
                            buyerBalance = resultSet.getDouble("balance");
                            buyerEmail = resultSet.getString("email");
                            Buyer buyer = new Buyer(buyerName, buyerID);
                            buyerArrayList.add(buyer);
                            System.out.println(buyerID);
                            System.out.println(buyerName);
                        } else findBuyer++;
                    }

                    if (findBuyer == no_users) {
                        JOptionPane.showMessageDialog(null, "Buyers don't have enough money");
                    } else {

                        for (int i = 0; i < buyerArrayList.size(); i++) {
                            outputStr.append(String.valueOf((i + 1) + ". " + buyerArrayList.get(i).name + "         ID : " + buyerArrayList.get(i).ID + "\n"));
                        }
                        JOptionPane.showMessageDialog(null, outputStr);
                    }
                } catch (SQLException sqlE) {
                    throw new RuntimeException(sqlE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Your Crypto amount is not enough to sell!");
            }
        } else if (e.getSource().equals(submit)) {
            selectedAmount = Double.parseDouble(selectAmountField.getText());
            selectedID = Integer.parseInt(selectBuyerIDField.getText());
            userEnteredPassword = JOptionPane.showInputDialog("Enter password : ");
            if (checkPassword(emailField, userEnteredPassword)) {

                boolean buyerBool = false;
                for (Buyer buyer : buyerArrayList) {
                    if (selectedID == buyer.ID) {
                        buyerBool = true;
                        break;
                    }
                }
                if (buyerBool) {
                    try {
                        buyerID = Integer.parseInt(selectBuyerIDField.getText());
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Please enter the buyer ID!");
                    }
                    try {
                        Connection connection1 = DriverManager.getConnection(DBURL, USER, PASSWORD);
                        Statement statement = connection1.createStatement();
                        ResultSet resultSet = statement.executeQuery(selectAll);
                        while (resultSet.next()) {
                            if (emailField.equals(resultSet.getString("email"))) {
                                userCrypto = resultSet.getDouble("crypto_amount");
                                userName = resultSet.getString("realname");
                            }
                            if (String.valueOf(buyerID).equals(resultSet.getString("id"))) {
                                buyerID = resultSet.getInt("id");
                                buyerName = resultSet.getString("realname");
                                buyerCrypto = resultSet.getDouble("crypto_amount");
                                buyerEmail = resultSet.getString("email");
                            }
                        }

//                    User Section
                        userCrypto = userCrypto - selectedAmount;
                        userBalance = userBalance + cost;
                        statement.executeUpdate("UPDATE user_info SET balance = " + userBalance + ", crypto_amount = " + userCrypto + " WHERE email = '" + emailField + "' and id = " + userID + ";");

//                    Buyer Section
                        buyerCrypto = buyerCrypto + selectedAmount;
                        buyerBalance = buyerBalance - cost;
                        statement.executeUpdate("UPDATE user_info SET balance = " + buyerBalance + ", crypto_amount = " + buyerCrypto + " WHERE email = '" + buyerEmail + "' and id = " + buyerID + ";");

                        date = String.valueOf(java.time.LocalDate.now());
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                        time = String.valueOf(dateTimeFormatter.format(java.time.LocalTime.now()));
//                                History Record
                        statement.executeUpdate("INSERT INTO history(id, Receiver, sender, Transaction_date, Transaction_time, transfer_crypto_amount, cost, email, source) VALUES (" + userID + ", '" + buyerName + "', '" + userName + "', '" + date + "', '" + time + "', " + selectedAmount + ", " + cost + ", '" + emailField + "', 'P2P');");
                        JOptionPane.showMessageDialog(null, "Selling process is successfully Done!");

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Selected buyer ID is invalid!");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Password is wrong!");
            }
        } else if (e.getSource().equals(clear)) {
            selectAmountField.setText("");
            selectBuyerIDField.setText("");
            totalAmountField.setText("");
            yourBalanceField.setText("");
        } else if (e.getSource().equals(exit)) {
            Menu menu = new Menu(emailField);
            menu.setVisible(true);
            dispose();
        }
    }
}


class ViewHistory extends JFrame implements ActionListener {

    Container viewHistoryContainer;
    String emailField;
    int id;

    JTextArea view = new JTextArea(50, 50);

    //        Button
    JButton show = new JButton("show");
    JButton delete = new JButton("Delete history");
    JButton clear = new JButton("Clear");
    JButton exit = new JButton("Exit");

    String userEnteredPassword;

    ViewHistory(String emailField) {

        userEnteredPassword = JOptionPane.showInputDialog("Enter password : ");


        this.emailField = emailField;
        this.setTitle("History");
        this.setSize(1300, 500);
        viewHistoryContainer = getContentPane();
        viewHistoryContainer.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(show);
        show.addActionListener(this);
        buttonPanel.add(delete);
        delete.addActionListener(this);
        buttonPanel.add(clear);
        clear.addActionListener(this);
        buttonPanel.add(exit);
        exit.addActionListener(this);

        viewHistoryContainer.add(view, BorderLayout.CENTER);
        viewHistoryContainer.add(buttonPanel, BorderLayout.SOUTH);

    }

    StringBuilder viewStr;

    @Override
    public void actionPerformed(ActionEvent e) {
        viewStr = new StringBuilder(" Receiver\t\tSender\t\tTransaction Date\tTransaction Time\tTransfer Crypto Amount\tCost\t\tSource\n");
        if (e.getSource().equals(show)) {
            try {
                Connection connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectAllHistory);
                while (resultSet.next()) {
                    if (emailField.equals(resultSet.getString("email"))) {
                        id = resultSet.getInt("id");
                        viewStr.append(resultSet.getString("receiver")).append("\t\t").append(resultSet.getString("sender")).append("\t\t").append(resultSet.getString("transaction_date")).append("\t\t").append(resultSet.getString("transaction_time")).append("\t\t").append(String.valueOf(resultSet.getDouble("transfer_crypto_amount"))).append("\t\t").append(String.valueOf(resultSet.getDouble("cost"))).append("\t\t").append(resultSet.getString("source")).append("\n");
                    }
                }
                view.setText(String.valueOf(viewStr));
//
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource().equals(delete)) {
            try {
                Connection connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
                Statement statement = connection.createStatement();

                statement.executeUpdate("DELETE FROM history WHERE id = " + id + " and email = '" + emailField + "';");
                JOptionPane.showMessageDialog(null, "Histories are deleted!");
//
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource().equals(clear)) {
            view.setText("");
        } else if (e.getSource().equals(exit)) {
            Menu menu = new Menu(emailField);
            menu.setVisible(true);
            dispose();
        }
    }
}