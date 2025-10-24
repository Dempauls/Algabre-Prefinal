package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dbConnect {

    public dbConnect() {
    }

  
    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:emptask.db");
            System.out.println("Connection Successful");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }

    
    public static void createDefaultAdmin() {
        String checkSql = "SELECT COUNT(*) FROM tbl_admin WHERE a_username = ?";
        String insertSql = "INSERT INTO tbl_admin (a_username, a_password, a_role) VALUES (?, ?, ?)";

        try (Connection conn = connectDB();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, "admin");
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    String hashedPassword = hashPassword("1234");
                    insertStmt.setString(1, "admin");
                    insertStmt.setString(2, hashedPassword);
                    insertStmt.setString(3, "Admin");
                    insertStmt.executeUpdate();
                    System.out.println("✅ Default admin created successfully!");
                }
            } else {
                System.out.println("ℹ️ Admin already exists.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error creating default admin: " + e.getMessage());
        }
    }

    
    public void addRecord(String sql, Object... values) {
        try (Connection conn = dbConnect.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            pstmt.executeUpdate();
            System.out.println("Record added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }

    
    public void viewRecords(String sqlQuery, String[] columnHeaders, String[] columnNames) {
        if (columnHeaders.length != columnNames.length) {
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }

        try (Connection conn = dbConnect.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery()) {

            StringBuilder headerLine = new StringBuilder();
            headerLine.append("---------------------------------------------------------------\n| ");
            for (String header : columnHeaders) {
                headerLine.append(String.format("%-20s | ", header));
            }
            headerLine.append("\n---------------------------------------------------------------");
            System.out.println(headerLine.toString());

            while (rs.next()) {
                StringBuilder row = new StringBuilder("| ");
                for (String colName : columnNames) {
                    String value = rs.getString(colName);
                    row.append(String.format("%-20s | ", value != null ? value : ""));
                }
                System.out.println(row.toString());
            }
            System.out.println("---------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    }

    
    public void updateRecord(String sql, Object... values) {
        try (Connection conn = dbConnect.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            pstmt.executeUpdate();
            System.out.println("Record updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }

    
    public void deleteRecord(String sql, Object... values) {
        try (Connection conn = dbConnect.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            pstmt.executeUpdate();
            System.out.println("Record deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }

   
    public static String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println("Error hashing password: " + e.getMessage());
            return null;
        }
    }

    
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

    public boolean login(String loginSql, String loginEmail, String loginPass) {
        boolean loginSuccess = false;
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(loginSql)) {
            pstmt.setString(1, loginEmail.trim());
            pstmt.setString(2, loginPass.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    loginSuccess = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }
        return loginSuccess;
    }

   
    }



