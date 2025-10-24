package main;

import config.dbConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminDashboard {

    
    public static void createDefaultAdmin() throws SQLException {
        try (Connection conn = dbConnect.connectDB()) {
            if (conn == null) {
                System.out.println("Database connection failed.");
                return;
            }

            String checkSql = "SELECT COUNT(*) FROM tbl_admin WHERE a_username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, "admin");
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        String insertSql = "INSERT INTO tbl_admin (a_username, a_password, a_role) VALUES (?, ?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                            insertStmt.setString(1, "admin");
                            insertStmt.setString(2, dbConnect.hashPassword("1234"));
                            insertStmt.setString(3, "Admin");
                            insertStmt.executeUpdate();
                           
                        }
                    } else {
                        
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error creating default admin: " + e.getMessage());
        }
    }

   
    public void viewAdmins() {
        String sql = "SELECT * FROM tbl_admin";
        dbConnect conf = new dbConnect();
        String[] headers = {"ID", "Username", "Role"};
        String[] columns = {"a_id", "a_username", "a_role"};
        conf.viewRecords(sql, headers, columns);
    }

    
    public static void viewEmployee() {
        String query = "SELECT * FROM tbl_employee";
        String[] headers = {"ID", "Name", "Email", "Type", "Status"};
        String[] cols = {"e_id", "e_name", "e_email", "e_type", "e_status"};
        dbConnect conf = new dbConnect();
        conf.viewRecords(query, headers, cols);
    }

    
    public static void showMenu(String loginEmail) throws SQLException {
        Scanner sc = new Scanner(System.in);
        dbConnect conf = new dbConnect();
        boolean adminMenu = true;

        
        createDefaultAdmin();

        while (adminMenu) {
            System.out.println("\n==== ADMIN DASHBOARD ====");
            System.out.println("1. Add Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. View Admins");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
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
                    String sql = "INSERT INTO tbl_employee (e_name, e_email, e_type, e_status, e_pass) VALUES (?, ?, ?, ?, ?)";
                    conf.addRecord(sql, name, email, uType, "Active", hashedPass);
                    System.out.println("Employee added successfully!");
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
                    System.out.println("Employee updated successfully!");
                    break;

                case 4:
                    viewEmployee();
                    System.out.print("Enter Employee ID to Delete: ");
                    int deleteId = sc.nextInt();
                    sc.nextLine();
                    sql = "DELETE FROM tbl_employee WHERE e_id = ?";
                    conf.deleteRecord(sql, deleteId);
                    System.out.println("Employee deleted successfully!");
                    break;

                case 5:
                    AdminDashboard adminDash = new AdminDashboard();
                    adminDash.viewAdmins();
                    break;

                case 6:
                    adminMenu = false;
                    System.out.println("Logged out successfully.");
                    break;

                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }
        }
    }
}

