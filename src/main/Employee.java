package main;

import config.dbConnect;
import java.util.Scanner;

public class Employee {

    public static void viewEmployee() {
        String query = "SELECT * FROM tbl_employee";
        String[] headers = {"ID", "Name", "Email", "Type", "Status"};
        String[] cols = {"e_id", "e_name", "e_email", "e_type", "e_status"};
        dbConnect conf = new dbConnect();
        conf.viewRecords(query, headers, cols);
    }

    
    public static void viewTasks() {
        String query = "SELECT * FROM tbl_task";
        String[] headers = {"ID", "Task Title", "Description", "Status"};
        String[] cols = {"t_id", "t_taskTitle", "t_desc", "t_status"};
        dbConnect conf = new dbConnect();
        conf.viewRecords(query, headers, cols);
    }

    public static void showMenu(String loginEmail) {
        Scanner sc = new Scanner(System.in);
        dbConnect conf = new dbConnect();
        boolean empMenu = true;

        while (empMenu) {
            System.out.println("\n==== EMPLOYEE MENU ====");
            System.out.println("1. View Employees");
            System.out.println("2. View Tasks");
            System.out.println("3. Update My Info");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewEmployee();
                    break;

                case 2:
                    viewTasks();
                    break;

                case 3:
                    System.out.print("Enter New Name: ");
                    String newName = sc.nextLine();
                    System.out.print("Enter New Email: ");
                    String newEmail = sc.nextLine();
                    System.out.print("Enter New Password: ");
                    String newPass = sc.nextLine();

                    String hashedEmpPass = dbConnect.hashPassword(newPass);
                    String sql = "UPDATE tbl_employee SET e_name = ?, e_email = ?, e_pass = ? WHERE e_email = ?";
                    conf.updateRecord(sql, newName, newEmail, hashedEmpPass, loginEmail);
                    System.out.println("Your info updated successfully!");
                    break;

                case 4:
                    empMenu = false;
                    break;

                default:
                    System.out.println("Invalid choice. Try again!");
            }
        }
    }
}
