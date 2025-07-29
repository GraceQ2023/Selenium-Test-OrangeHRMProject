package com.orangehrm.utilities;

import com.orangehrm.pages.HomePage;
import org.apache.logging.log4j.Logger;

import java.sql.*;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: DBConnection
 * Package: com.orangehrm.utilities
 * Description:
 *
 * @Author Grace
 * @Create 21/7/2025 1:06 pm
 * Version 1.0
 */
public class DBConnection {

    private static final String DB_URL="jdbc:mysql://localhost:3306/orangehrm";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    public static final Logger logger = LoggerManager.getLogger(DBConnection.class);

    public static Connection getDBConnection(){
        try {
            logger.info("Starting DB Connection...");
            ExtentManager.logStep("Starting DB Connection...");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            logger.info("DB Connection successfu");
            return conn;
        } catch (SQLException e) {
            logger.info("Error while establishing the DB connection");
            ExtentManager.logStep("Error while establishing the DB connection");
            e.printStackTrace();
            return null;
        }
    }

    // Get employee details from database and store in Map
    public static Map<String, String> getEmployeeDetails(String employee_id) {
        String query = "SELECT emp_firstname, emp_lastname FROM hs_hr_employee WHERE emp_number = " + employee_id + ";";
        Map<String, String> employeeDetails = new HashMap<>();

        try {
            Connection conn = getDBConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            logger.info("Executing the query --> " + query);
            ExtentManager.logStep("Executing the query --> " + query);
            if (rs.next()) {
                String firstName = rs.getString("emp_firstname");
//                String middleName = rs.getString("emp_middlename");
                String lastName = rs.getString("emp_lastname");

                // Store in a map
                employeeDetails.put("firstName", firstName);
                // employeeDetails.put("middleName", middleName !=null? middleName:"" ); // this is good
                employeeDetails.put("lastName", lastName);

                logger.info("Query executed successfully --> " + query);
                logger.info("Employee data fetched: " + employeeDetails);
                ExtentManager.logStep("Employee data fetched: " + employeeDetails);
            } else {
                logger.error("Employee not found");
                ExtentManager.logStep("Employee not found");
            }
        } catch (SQLException e) {
            logger.error("Error while executing query --> " + query);
            e.printStackTrace();
        }
        return employeeDetails;
    }
}
