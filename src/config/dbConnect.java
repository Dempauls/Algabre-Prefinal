
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class dbConnect {

    public dbConnect() {
    }
    //Connection Method to SQLITE
public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver
            con = DriverManager.getConnection("jdbc:sqlite:emptask.db"); // Establish connection
            System.out.println("Connection Successful");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }
public void addRecord(String sql, Object... values) {
    try (Connection conn = dbConnect.connectDB(); // Use the connectDB method
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Loop through the values and set them in the prepared statement dynamically
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
            } else if (values[i] instanceof Double) {
                pstmt.setDouble(i + 1, (Double) values[i]); // If the value is Double
            } else if (values[i] instanceof Float) {
                pstmt.setFloat(i + 1, (Float) values[i]); // If the value is Float
            } else if (values[i] instanceof Long) {
                pstmt.setLong(i + 1, (Long) values[i]); // If the value is Long
            } else if (values[i] instanceof Boolean) {
                pstmt.setBoolean(i + 1, (Boolean) values[i]); // If the value is Boolean
            } else if (values[i] instanceof java.util.Date) {
                pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime())); // If the value is Date
            } else if (values[i] instanceof java.sql.Date) {
                pstmt.setDate(i + 1, (java.sql.Date) values[i]); // If it's already a SQL Date
            } else if (values[i] instanceof java.sql.Timestamp) {
                pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]); // If the value is Timestamp
            } else {
                pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
            }
        }

        pstmt.executeUpdate();
        System.out.println("Record added successfully!");
    } catch (SQLException e) {
        System.out.println("Error adding record: " + e.getMessage());
    }
}
// Dynamic view method to display records from any table
    public void viewRecords(String sqlQuery, String[] columnHeaders, String[] columnNames) {
        // Check that columnHeaders and columnNames arrays are the same length
        if (columnHeaders.length != columnNames.length) {
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }

        try (Connection conn = dbConnect.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery()) {

            // Print the headers dynamically
            StringBuilder headerLine = new StringBuilder();
            headerLine.append("------------------------------------------------------------------------------------------------------------------------------------\n| ");
            for (String header : columnHeaders) {
                headerLine.append(String.format("%-20s | ", header)); // Adjust formatting as needed
            }
            headerLine.append("\n-----------------------------------------------------------------------------------------------------------------------------------");

            System.out.println(headerLine.toString());

            // Print the rows dynamically based on the provided column names
            while (rs.next()) {
                StringBuilder row = new StringBuilder("| ");
                for (String colName : columnNames) {
                    String value = rs.getString(colName);
                    row.append(String.format("%-20s | ", value != null ? value : "")); // Adjust formatting
                }
                System.out.println(row.toString());
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    }

    //-----------------------------------------------
    // UPDATE METHOD
    //-----------------------------------------------
    
    public void updateRecord(String sql, Object... values) {
        try (Connection conn = dbConnect.connectDB(); // Use the connectDB method
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Loop through the values and set them in the prepared statement dynamically
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
                } else if (values[i] instanceof Double) {
                    pstmt.setDouble(i + 1, (Double) values[i]); // If the value is Double
                } else if (values[i] instanceof Float) {
                    pstmt.setFloat(i + 1, (Float) values[i]); // If the value is Float
                } else if (values[i] instanceof Long) {
                    pstmt.setLong(i + 1, (Long) values[i]); // If the value is Long
                } else if (values[i] instanceof Boolean) {
                    pstmt.setBoolean(i + 1, (Boolean) values[i]); // If the value is Boolean
                } else if (values[i] instanceof java.util.Date) {
                    pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime())); // If the value is Date
                } else if (values[i] instanceof java.sql.Date) {
                    pstmt.setDate(i + 1, (java.sql.Date) values[i]); // If it's already a SQL Date
                } else if (values[i] instanceof java.sql.Timestamp) {
                    pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]); // If the value is Timestamp
                } else {
                    pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
                }
            }

            pstmt.executeUpdate();
            System.out.println("Record updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }
      // Add this method in the config class
public void deleteRecord(String sql, Object... values) {
    try (Connection conn = dbConnect.connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Loop through the values and set them in the prepared statement dynamically
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
            } else {
                pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
            }
        }

        pstmt.executeUpdate();
        System.out.println("Record deleted successfully!");
    } catch (SQLException e) {
        System.out.println("Error deleting package config;\n" +
"\n" +
"import java.sql.Connection;\n" +
"import java.sql.DriverManager;\n" +
"import java.sql.PreparedStatement;\n" +
"import java.sql.ResultSet;\n" +
"import java.sql.SQLException;\n" +
"\n" +
"public class dbConnect {\n" +
"\n" +
"    public dbConnect() {\n" +
"    }\n" +
"    //Connection Method to SQLITE\n" +
"public static Connection connectDB() {\n" +
"        Connection con = null;\n" +
"        try {\n" +
"            Class.forName(\"org.sqlite.JDBC\"); // Load the SQLite JDBC driver\n" +
"            con = DriverManager.getConnection(\"jdbc:sqlite:emptask.db\"); // Establish connection\n" +
"            System.out.println(\"Connection Successful\");\n" +
"        } catch (ClassNotFoundException | SQLException e) {\n" +
"            System.out.println(\"Connection Failed: \" + e);\n" +
"        }\n" +
"        return con;\n" +
"    }\n" +
"public void addRecord(String sql, Object... values) {\n" +
"    try (Connection conn = dbConnect.connectDB(); // Use the connectDB method\n" +
"         PreparedStatement pstmt = conn.prepareStatement(sql)) {\n" +
"\n" +
"        // Loop through the values and set them in the prepared statement dynamically\n" +
"        for (int i = 0; i < values.length; i++) {\n" +
"            if (values[i] instanceof Integer) {\n" +
"                pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer\n" +
"            } else if (values[i] instanceof Double) {\n" +
"                pstmt.setDouble(i + 1, (Double) values[i]); // If the value is Double\n" +
"            } else if (values[i] instanceof Float) {\n" +
"                pstmt.setFloat(i + 1, (Float) values[i]); // If the value is Float\n" +
"            } else if (values[i] instanceof Long) {\n" +
"                pstmt.setLong(i + 1, (Long) values[i]); // If the value is Long\n" +
"            } else if (values[i] instanceof Boolean) {\n" +
"                pstmt.setBoolean(i + 1, (Boolean) values[i]); // If the value is Boolean\n" +
"            } else if (values[i] instanceof java.util.Date) {\n" +
"                pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime())); // If the value is Date\n" +
"            } else if (values[i] instanceof java.sql.Date) {\n" +
"                pstmt.setDate(i + 1, (java.sql.Date) values[i]); // If it's already a SQL Date\n" +
"            } else if (values[i] instanceof java.sql.Timestamp) {\n" +
"                pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]); // If the value is Timestamp\n" +
"            } else {\n" +
"                pstmt.setString(i + 1, values[i].toString()); // Default to String for other types\n" +
"            }\n" +
"        }\n" +
"\n" +
"        pstmt.executeUpdate();\n" +
"        System.out.println(\"Record added successfully!\");\n" +
"    } catch (SQLException e) {\n" +
"        System.out.println(\"Error adding record: \" + e.getMessage());\n" +
"    }\n" +
"}\n" +
"// Dynamic view method to display records from any table\n" +
"    public void viewRecords(String sqlQuery, String[] columnHeaders, String[] columnNames) {\n" +
"        // Check that columnHeaders and columnNames arrays are the same length\n" +
"        if (columnHeaders.length != columnNames.length) {\n" +
"            System.out.println(\"Error: Mismatch between column headers and column names.\");\n" +
"            return;\n" +
"        }\n" +
"\n" +
"        try (Connection conn = dbConnect.connectDB();\n" +
"             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);\n" +
"             ResultSet rs = pstmt.executeQuery()) {\n" +
"\n" +
"            // Print the headers dynamically\n" +
"            StringBuilder headerLine = new StringBuilder();\n" +
"            headerLine.append(\"--------------------------------------------------------------------------------\\n| \");\n" +
"            for (String header : columnHeaders) {\n" +
"                headerLine.append(String.format(\"%-20s | \", header)); // Adjust formatting as needed\n" +
"            }\n" +
"            headerLine.append(\"\\n--------------------------------------------------------------------------------\");\n" +
"\n" +
"            System.out.println(headerLine.toString());\n" +
"\n" +
"            // Print the rows dynamically based on the provided column names\n" +
"            while (rs.next()) {\n" +
"                StringBuilder row = new StringBuilder(\"| \");\n" +
"                for (String colName : columnNames) {\n" +
"                    String value = rs.getString(colName);\n" +
"                    row.append(String.format(\"%-20s | \", value != null ? value : \"\")); // Adjust formatting\n" +
"                }\n" +
"                System.out.println(row.toString());\n" +
"            }\n" +
"            System.out.println(\"--------------------------------------------------------------------------------\");\n" +
"\n" +
"        } catch (SQLException e) {\n" +
"            System.out.println(\"Error retrieving records: \" + e.getMessage());\n" +
"        }\n" +
"    }\n" +
"\n" +
"    //-----------------------------------------------\n" +
"    // UPDATE METHOD\n" +
"    //-----------------------------------------------\n" +
"    \n" +
"    public void updateRecord(String sql, Object... values) {\n" +
"        try (Connection conn = dbConnect.connectDB(); // Use the connectDB method\n" +
"             PreparedStatement pstmt = conn.prepareStatement(sql)) {\n" +
"\n" +
"            // Loop through the values and set them in the prepared statement dynamically\n" +
"            for (int i = 0; i < values.length; i++) {\n" +
"                if (values[i] instanceof Integer) {\n" +
"                    pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer\n" +
"                } else if (values[i] instanceof Double) {\n" +
"                    pstmt.setDouble(i + 1, (Double) values[i]); // If the value is Double\n" +
"                } else if (values[i] instanceof Float) {\n" +
"                    pstmt.setFloat(i + 1, (Float) values[i]); // If the value is Float\n" +
"                } else if (values[i] instanceof Long) {\n" +
"                    pstmt.setLong(i + 1, (Long) values[i]); // If the value is Long\n" +
"                } else if (values[i] instanceof Boolean) {\n" +
"                    pstmt.setBoolean(i + 1, (Boolean) values[i]); // If the value is Boolean\n" +
"                } else if (values[i] instanceof java.util.Date) {\n" +
"                    pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime())); // If the value is Date\n" +
"                } else if (values[i] instanceof java.sql.Date) {\n" +
"                    pstmt.setDate(i + 1, (java.sql.Date) values[i]); // If it's already a SQL Date\n" +
"                } else if (values[i] instanceof java.sql.Timestamp) {\n" +
"                    pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]); // If the value is Timestamp\n" +
"                } else {\n" +
"                    pstmt.setString(i + 1, values[i].toString()); // Default to String for other types\n" +
"                }\n" +
"            }\n" +
"\n" +
"            pstmt.executeUpdate();\n" +
"            System.out.println(\"Record updated successfully!\");\n" +
"        } catch (SQLException e) {\n" +
"            System.out.println(\"Error updating record: \" + e.getMessage());\n" +
"        }\n" +
"    }\n" +
"      // Add this method in the config class\n" +
"public void deleteRecord(String sql, Object... values) {\n" +
"    try (Connection conn = dbConnect.connectDB();\n" +
"         PreparedStatement pstmt = conn.prepareStatement(sql)) {\n" +
"record: " + e.getMessage());
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
        } catch (SQLException e){
            System.out.println("Error checking account: "+ e.getMessage());
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
                loginSuccess = true; // credentials matched
            }
        }

    } catch (SQLException e) {
        System.out.println("Login Error: " + e.getMessage());
    }

    return loginSuccess;
}
    public java.util.List<java.util.Map<String, Object>> fetchRecords(String sqlQuery, Object... values) {
    java.util.List<java.util.Map<String, Object>> records = new java.util.ArrayList<>();

    try (Connection conn = dbConnect.connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]);
        }

        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            java.util.Map<String, Object> row = new java.util.HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnName(i), rs.getObject(i));
            }
            records.add(row);
        }

    } catch (SQLException e) {
        System.out.println("Error fetching records: " + e.getMessage());
    }

    return records;
}
    // Method to hash passwords using SHA-256
public static String hashPassword(String password) {
    try {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        
        // Convert byte array to hex string
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

}


  
       
    

