package main;

import config.dbConnect;
import static config.dbConnect.connectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static void viewEmployee() {
        String votersQuery = "SELECT * FROM tbl_employee";
        String[] votersHeaders = {"id", "name", "email", "type", "status"};
        String[] votersColumns = {"e_id", "e_name", "e_email", "e_type", "e_status"};
        dbConnect conf = new dbConnect();
        conf.viewRecords(votersQuery, votersHeaders, votersColumns);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int task;
        dbConnect conf = new dbConnect();

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
                    String loginEmail = "";
                    String loginPass = "";

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

                    String userType = "";
                    String loginSql = "SELECT e_type FROM tbl_employee WHERE e_email = ? AND e_pass = ?";
                    try (Connection conn = connectDB();
                         PreparedStatement pstmt = conn.prepareStatement(loginSql)) {
                        pstmt.setString(1, loginEmail);
                        pstmt.setString(2, dbConnect.hashPassword(loginPass)); 
                        try (ResultSet rs = pstmt.executeQuery()) {
                            if (rs.next()) {
                                userType = rs.getString("e_type");
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println("Error getting user type: " + e.getMessage());
                    }

                    if (!userType.isEmpty()) {
                        System.out.println(" Login Successful! Welcome " + loginEmail + " (" + userType + ")");

                        if (userType.equalsIgnoreCase("Admin")) {
                            boolean adminMenu = true;
                            while (adminMenu) {
                                System.out.println("\n==== ADMIN DASHBOARD ====");
                                System.out.println("1. Add Employee");
                                System.out.println("2. View Employees");
                                System.out.println("3. Update Employee");
                                System.out.println("4. Delete Employee");
                                System.out.println("5. Logout");
                                System.out.print("Enter choice: ");
                                int choice1 = sc.nextInt();
                                sc.nextLine();

                                switch (choice1) {
                                    case 1:
                                        System.out.print("Enter Employee Name: ");
                                        String name = sc.nextLine();
                                        System.out.print("Enter Email: ");
                                        String email = sc.nextLine();
                                        System.out.print("Enter User Type (1 - Admin / 2 - Employee): ");
                                        int t = sc.nextInt();
                                        sc.nextLine();
                                        String uType = (t == 1) ? "Admin" : "Employee";
                                        System.out.print("Enter Password: ");
                                        String pass = sc.nextLine();

                                       
                                        String hashedPass = dbConnect.hashPassword(pass);

                                        String sql = "SELECT * FROM tbl_employee WHERE u_email = ? AND u_pass = ?";
                                        boolean loginSuccess = conf.login(sql, email, hashedPass);

                                        if (loginSuccess) {
                                            System.out.println("Login successful!");
                                        } else {
                                            System.out.println("Invalid email or password.");
                                        }
                                        break;

                                    case 2:
                                        viewEmployee();
                                        break;

                                    case 3:
                                        viewEmployee();
                                        System.out.print("Enter Employee ID to Update: ");
                                        int id = sc.nextInt();
                                        sc.nextLine();

                                        System.out.print("Enter New Name: ");
                                        String newName = sc.nextLine();
                                        System.out.print("Enter New Email: ");
                                        String newEmail = sc.nextLine();
                                        System.out.print("Enter New Password: ");
                                        String newPass = sc.nextLine();
                                        System.out.print("Enter New Status: ");
                                        String newStatus = sc.nextLine();

                                        
                                        String hashedNewPass = dbConnect.hashPassword(newPass);

                                        sql = "UPDATE tbl_employee SET e_name = ?, e_email = ?, e_pass = ?, e_status = ? WHERE e_id = ?";
                                        conf.updateRecord(sql, newName, newEmail, hashedNewPass, newStatus, id);
                                        System.out.println(" Employee updated successfully!");
                                        break;

                                    case 4:
                                        viewEmployee();
                                        System.out.print("Enter Employee ID to Delete: ");
                                        int deleteId = sc.nextInt();
                                        sc.nextLine();

                                        sql = "DELETE FROM tbl_employee WHERE e_id = ?";
                                        conf.deleteRecord(sql, deleteId);
                                        System.out.println(" Employee deleted successfully!");
                                        break;

                                    case 5:
                                        adminMenu = false;
                                        break;
                                }
                            }

                        } else {
                            boolean empMenu = true;
                            while (empMenu) {
                                System.out.println("\n==== EMPLOYEE MENU ====");
                                System.out.println("1. View Employees");
                                System.out.println("2. Update My Info");
                                System.out.println("3. Logout");
                                System.out.print("Enter choice: ");
                                int choice2 = sc.nextInt();
                                sc.nextLine();

                                switch (choice2) {
                                    case 1:
                                        viewEmployee();
                                        break;

                                    case 2:
                                        System.out.print("Enter New Name: ");
                                        String newName = sc.nextLine();
                                        System.out.print("Enter New Email: ");
                                        String newEmail = sc.nextLine();
                                        System.out.print("Enter New Password: ");
                                        String newPass = sc.nextLine();

                                        
                                        String hashedEmpPass = dbConnect.hashPassword(newPass);

                                        String sql = "UPDATE tbl_employee SET e_name = ?, e_email = ?, e_pass = ? WHERE e_email = ?";
                                        conf.updateRecord(sql, newName, newEmail, hashedEmpPass, loginEmail);
                                        System.out.println(" Your info updated successfully!");
                                        break;

                                    case 3:
                                        empMenu = false;
                                        break;
                                }
                            }
                        }

                    } else {
                        System.out.println(" Invalid login.");
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
                    System.out.println(" Registration successful! (Password hashed)");
                    break;

                case 3:
                    System.out.println("Exiting program...");
                    System.exit(0);
            }
        } while (task != 3);
    }
}
