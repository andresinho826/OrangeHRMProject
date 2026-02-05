package com.orangeHRM.utilities;

import com.orangeHRM.base.BaseClass;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/orangehrm";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    private static final Logger logger = BaseClass.logger;

    /**
     * Establishes and returns a connection to the database.
     *
     * @return A Connection object to interact with the database.
     */
    public static Connection getDBConnection() {
        // Code to establish a database connection
        // This is a placeholder for actual implementation
        try {
            logger.info("Connecting to database at " + DB_URL + " with user " + DB_USERNAME);
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            logger.info("Database connection established successfully.");
            return conn;
        } catch (SQLException e) {
            logger.error("Failed to connect to the database.");
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Fetches employee details from the database based on employee ID.
     *
     * @param employeeId The ID of the employee whose details are to be fetched.
     * @return A map containing employee details such as first name and last name.
     */
    public static Map<String,String> getEmployeeDetails(String employeeId) {

        String query = "SELECT emp_firstname, emp_lastname FROM hs_hr_employee WHERE employee_id =" + employeeId;
        // Code to fetch employee details from the database
        // This is a placeholder for actual implementation
        logger.info("Fetching details for employee ID: " + employeeId);

        Map<String,String> employeeDetails = new HashMap<>();

        try(Connection conn = getDBConnection()) {
            if (conn != null) {
                var stmt = conn.createStatement();
                var rs = stmt.executeQuery(query);
                if (rs.next()) {
                    String firstName = rs.getString("emp_firstname");
                    String lastName = rs.getString("emp_lastname");

                    employeeDetails.put("firstName", firstName);
                    employeeDetails.put("lastName", lastName);
                    logger.info("Employee details fetched successfully.");
                } else {
                    logger.error("No employee found with ID: " + employeeId);
                }
            }
        } catch (SQLException e) {
            logger.error("Error while fetching employee details.");
            e.printStackTrace();
        }
        return employeeDetails;
    }
}
