package main;

import config.dbConnect;
import static config.dbConnect.connectDB;
import java.sql.*;
import java.util.Scanner;

public class main {

    
    public static boolean accountExists(String email, String password) {
        boolean exists = false;
        String sql = "SELECT COUNT(*) FROM tbl_employee WHERE e_email = ? AND e_pass = ?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking account: " + e.getMessage());
        }
        return exists;
    }

    
    public static String getUserType(String email, String password) {
        String userType = "";
        String sql = "SELECT e_type FROM tbl_employee WHERE e_email = ? AND e_pass = ?";
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, dbConnect.hashPassword(password));
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    userType = rs.getString("e_type");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting user type: " + e.getMessage());
        }
        return userType;
    }

    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        dbConnect conf = new dbConnect();

        
        AdminDashboard.createDefaultAdmin();


        int task;
        do {
            System.out.println("\n========= Welcome to Employee Task Scheduler System! =========");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter task: ");
            task = sc.nextInt();
            sc.nextLine();

            switch (task) {
                case 1:
                    int attempt = 3;
                    String loginEmail = "", loginPass = "";

                    while (attempt > 0) {
                        System.out.println("\n==== LOGIN ====");
                        System.out.print("Enter Email: ");
                        loginEmail = sc.nextLine();
                        System.out.print("Enter Password: ");
                        loginPass = sc.nextLine();

                        String hashedLoginPass = dbConnect.hashPassword(loginPass);
                        if (accountExists(loginEmail, hashedLoginPass)) {
                            break;
                        } else {
                            attempt--;
                            if (attempt == 0) {
                                System.out.println("Too many failed attempts. Exiting...");
                                System.exit(0);
                            }
                            System.out.println("Invalid Account Credentials. Attempts Left: " + attempt);
                        }
                    }

                    String userType = getUserType(loginEmail, loginPass);
                    if (userType.equalsIgnoreCase("Admin")) {
                        AdminDashboard.showMenu(loginEmail);
                    } else if (userType.equalsIgnoreCase("Employee")) {
                        Employee.showMenu(loginEmail);
                    } else {
                        System.out.println("Invalid login or user type not found.");
                    }
                    break;

                case 2:
                    System.out.println("\n==== REGISTER NEW USER ====");
                    System.out.print("Enter Name: ");
                    String regName = sc.nextLine();
                    System.out.print("Enter Email: ");
                    String regEmail = sc.nextLine();
                    System.out.print("Enter Password: ");
                    String regPass = sc.nextLine();
                    System.out.print("Enter User Type (1 - Admin / 2 - Employee): ");
                    int regType = sc.nextInt();
                    sc.nextLine();

                    String regUserType = (regType == 1) ? "Admin" : "Employee";
                    String hashedRegPass = dbConnect.hashPassword(regPass);

                    String regSql = "INSERT INTO tbl_employee (e_name, e_email, e_type, e_status, e_pass) VALUES (?, ?, ?, ?, ?)";
                    conf.addRecord(regSql, regName, regEmail, regUserType, "Pending", hashedRegPass);
                    System.out.println("Registration successful! (Password hashed)");
                    break;

                case 3:
                    System.out.println(" Exiting program...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (task != 3);
    }
}


