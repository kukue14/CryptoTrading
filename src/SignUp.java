import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static ProjectUtiles.Utiles.*;

public class SignUp extends JFrame implements ActionListener {
    Container container;

    //    Realname
    JLabel realName = new JLabel("Name : ");
    JTextField realNameField = new JTextField(50);

    //    Username
    JLabel username = new JLabel("Username : ");
    JTextField usernameField = new JTextField(50);

    //    Email
    JLabel email = new JLabel("Email");
    JTextField emailField = new JTextField(50);

    //    Password
    JLabel password = new JLabel("Password : ");
    JPasswordField passwordField = new JPasswordField(50);

    //    Confirm Password
    JLabel confirmPassword = new JLabel("Confirm Password : ");
    JPasswordField confirmPasswordField = new JPasswordField(50);

    //    Phone number
    JLabel phone_no = new JLabel("Phone no : ");
    JTextField phoneNoField = new JTextField(50);

    //    Date of Birth
    JLabel DOB = new JLabel("Date of Birth (YYYY-mm-dd) : ");
    JTextField DOBField = new JTextField(50);

    //    Message
    JLabel message = new JLabel();

    //    Buttons
    JButton submit = new JButton("Submit");
    JButton clear = new JButton("Clear");
    JButton exit = new JButton("Exit");

    SignUp() {
        this.setTitle("SignUp Page");
        this.setSize(500, 400);
        this.setLocation(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container = getContentPane();
        container.setLayout(new BorderLayout());

//        JPanel detailsSection = new JPanel(new GridLayout(0,2));
        JPanel detailsSection = new JPanel();
        detailsSection.setLayout(null);
//        Realname
        realName.setBounds(30, 20, 80, 25);
        realNameField.setBounds(200, 20, 265, 25);
        detailsSection.add(realName);
        detailsSection.add(realNameField);

//        Username
        username.setBounds(30, 60, 80, 25);
        usernameField.setBounds(200, 60, 265, 25);
        detailsSection.add(username);
        detailsSection.add(usernameField);

//        Email
        email.setBounds(30, 100, 80, 25);
        emailField.setBounds(200, 100, 265, 25);
        detailsSection.add(email);
        detailsSection.add(emailField);

//        Password
        password.setBounds(30, 140, 80, 25);
        passwordField.setBounds(200, 140, 265, 25);
        detailsSection.add(password);
        detailsSection.add(passwordField);

//        Confirm Password
        confirmPassword.setBounds(30, 180, 150, 25);
        confirmPasswordField.setBounds(200, 180, 265, 25);
        detailsSection.add(confirmPassword);
        detailsSection.add(confirmPasswordField);

//        Phone number
        phone_no.setBounds(30, 220, 200, 25);
        phoneNoField.setBounds(200, 220, 265, 25);
        detailsSection.add(phone_no);
        detailsSection.add(phoneNoField);

//        Date of Birth
        DOB.setBounds(30, 260, 200, 25);
        DOBField.setBounds(200, 260, 265, 25);
        detailsSection.add(DOB);
        detailsSection.add(DOBField);

//        Message
        JPanel messagePanel = new JPanel(new FlowLayout());
        messagePanel.add(message);

//        Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(submit);
        submit.addActionListener(this);
        buttonPanel.add(clear);
        clear.addActionListener(this);
        buttonPanel.add(exit);
        exit.addActionListener(this);

        container.add(detailsSection, BorderLayout.CENTER);
        container.add(messagePanel, BorderLayout.SOUTH);
        container.add(buttonPanel, BorderLayout.SOUTH);
    }

    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(submit)) {
            try {
                //                Phone Number Format
                String phoneNoFormat = "\\+\\d{1,3}[0-9]{5,11}";
                String passwordPattern = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";

                Connection connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectAll);


                ArrayList<String> db_emails = new ArrayList<>();
                ArrayList<String> db_usernames = new ArrayList<>();
                while (resultSet.next()) {
                    String aUser = resultSet.getString("username");
                    db_usernames.add(aUser);
                    String aEmail = resultSet.getString("email");
                    db_emails.add(aEmail);
                }

                boolean condition = true;


                for (String aPerson : db_usernames) {
                    if (aPerson.equals(usernameField.getText())) {
                        JOptionPane.showMessageDialog(null, "This username is already exist! Cannot create again.");
                        condition = false;
                    }
                }

                if (condition) {

                    for (String aEmail : db_emails) {
                        if (aEmail.equals(emailField.getText())) {
                            JOptionPane.showMessageDialog(null, "This email is already exist! Cannot create again.");
                            condition = false;
                        }

                    }
                }


           /*     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                if (!(DOBField.getText().matches(simpleDateFormat.toString()))) {
                    JOptionPane.showMessageDialog(null, "Date of Birth Format is invalid!");
                } else */


                if (condition) {
                    if (!(String.valueOf(passwordField.getText()).equals(confirmPasswordField.getText()))) {
                        JOptionPane.showMessageDialog(null, "Passwords are not match!");
                    } else if (passwordField.getText().length() < 8) {
                        JOptionPane.showMessageDialog(null, "Password must be at least 8 characters!");
                    } else if (!(passwordField.getText().matches(passwordPattern))) {
                        JOptionPane.showMessageDialog(null, "Password is too weak! at least one upper case, special character must be contained.");
                    } else if (!(phoneNoField.getText().matches(phoneNoFormat))) {
                        JOptionPane.showMessageDialog(null, "Phone number is invalid! (+95.......)");
                    } else if ( !(isValidFormat("yyyy-MM-dd", DOBField.getText()))) {
                            JOptionPane.showMessageDialog(null, "Date of Birth format is invalid! Please type in the format (yyyy-MM-dd)!");
                    }else{
                        statement.executeUpdate("INSERT INTO user_info(realname, username, email, phone_no, DOB, password) VALUES ('" + realNameField.getText() + "', '" + usernameField.getText() + "', '" + emailField.getText() + "', '" + phoneNoField.getText() + "', '" + DOBField.getText() + "', '" + passwordField.getText() + "');");
                        JOptionPane.showMessageDialog(null, "Your account has been created successfully!");
                    }
                }

            } catch (SQLException ex) {
//                JOptionPane.showMessageDialog(null, "Something is wrong!");
                throw new RuntimeException(ex);
            }
        } else if (e.getSource().equals(clear)) {
            realNameField.setText("");
            usernameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            phoneNoField.setText("");
            DOBField.setText("");
        } else if (e.getSource().equals(exit)) {
            Home home = new Home();
            home.setVisible(true);
            dispose();
        }
    }
}


