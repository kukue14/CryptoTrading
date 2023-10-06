package ProjectUtiles;

import com.sun.net.httpserver.Request;

import java.sql.*;
import java.util.Scanner;

public class Utiles {
    public static final String DBURL = "jdbc:mysql://localhost:3306/CodeWall";
    public static final String USER = "root";
    public static final String PASSWORD = "kukue@014";
    public static final String selectAll = "SELECT * FROM user_info;";
    public static final String selectAllAgent = "SELECT * FROM agent;";
    public static final String selectAllHistory = "SELECT * FROM history;";

    public static String targetHistory(int id) {
        return "SELECT * FROM history WHERE id = " + id + ";";
    }
    public static String targetSelect(String emailField, String passwordField) {
        return "SELECT * FROM user_info where email = '" + emailField + "' and password = '" + passwordField + "';";
    }


//    Checking Password
    public static boolean checkPassword(String email, String password) {
        boolean checkPassword = false;
        try {
            Connection connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(selectAll);
            while (resultSet.next()) {
                if (email.equals(resultSet.getString("email")) && password.equals(resultSet.getString("password"))) checkPassword = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return checkPassword;
    }

//    Reset Password
    public static void resetPassword(String email, int phone_no, String DOB) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL, USER, PASSWORD);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(selectAll);
            if (email.equals(resultSet.getString("email")) && DOB.equals(resultSet.getString("DOB")) && phone_no == resultSet.getInt("phone_no")) {
                Scanner input = new Scanner(System.in);
                System.out.print("Enter new password : ");
                String newPassword = input.next();
                System.out.println("Confirm password : ");
                String confirmPassword = input.next();
                if (newPassword.equals(confirmPassword)) {
                    statement.executeUpdate("update user_info set password = '" + confirmPassword + "' where email = '" + email + "' and DOB = '" + DOB + "';");
                } else {
                    System.out.println("Passwords are not match!");
                }
            } else {
                System.out.println("Email or password wrong!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
