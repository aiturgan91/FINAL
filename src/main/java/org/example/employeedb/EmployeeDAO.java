package org.example.employeedb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/employee";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1359";
    private static final Logger LOGGER = Logger.getLogger(EmployeeDAO.class.getName());

    // Establish a connection to the database
    private Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            LOGGER.info("Connected to Database.");
            return connection;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Connection failed", e);
            throw e;
        }
    }

    // Get the next available ID for a new employee
    public int getNextId() {
        String sql = "SELECT MAX(id) FROM employee";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting next ID", e);
        }
        return 1; // Return 1 if no records are found
    }

    // Create a new employee record in the database
    public void createEmployee(Employee employee) {
        String sql = "INSERT INTO employee (id, name, position, salary, hire_date, employment_type, phone_number, email, gender) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employee.getId());
            pstmt.setString(2, employee.getName());
            pstmt.setString(3, employee.getPosition());
            pstmt.setDouble(4, employee.getSalary());
            pstmt.setDate(5, Date.valueOf(employee.getHireDate()));
            pstmt.setString(6, employee.getEmploymentType().name());
            pstmt.setString(7, employee.getPhoneNumber());
            pstmt.setString(8, employee.getEmail());
            pstmt.setString(9, employee.getGender());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating employee", e);
        }
    }

    // Fetch an employee by their ID
    public Employee getEmployeeById(int id) {
        LOGGER.info("Fetching Employee with ID: " + id);
        String sql = "SELECT * FROM employee WHERE id = ?";
        Employee employee = null;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    employee = mapToEmployee(rs);
                    LOGGER.info("Employee Found: " + employee);
                } else {
                    LOGGER.warning("No employee found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching employee by ID", e);
        }
        return employee;
    }

    // Fetch all employees from the database
    public List<Employee> getAllEmployees() {
        String sql = "SELECT * FROM employee";
        List<Employee> employees = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employees.add(mapToEmployee(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all employees", e);
        }
        return employees;
    }

    // Update an existing employee's data
    public void updateEmployee(Employee employee) {
        String sql = "UPDATE employee SET name = ?, position = ?, salary = ?, hire_date = ?, employment_type = ?, " +
                "phone_number = ?, email = ?, gender = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getPosition());
            pstmt.setDouble(3, employee.getSalary());
            pstmt.setDate(4, Date.valueOf(employee.getHireDate()));
            pstmt.setString(5, employee.getEmploymentType().name());
            pstmt.setString(6, employee.getPhoneNumber());
            pstmt.setString(7, employee.getEmail());
            pstmt.setString(8, employee.getGender());
            pstmt.setInt(9, employee.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating employee", e);
        }
    }

    // Delete an employee by their ID
    public void deleteEmployee(int id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            LOGGER.info("Employee with ID " + id + " deleted from the database.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting employee", e);
        }
    }

    // Delete all employees from the database
    public void deleteAllEmployees() {
        String sql = "DELETE FROM employee";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            LOGGER.info("All employees deleted from the database.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting all employees", e);
        }
    }

    // Helper method to map ResultSet to Employee object
    private Employee mapToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("position"),
                rs.getDouble("salary"),
                rs.getDate("hire_date").toLocalDate(),
                EmploymentType.valueOf(rs.getString("employment_type")),
                rs.getString("phone_number"),
                rs.getString("email"),
                rs.getString("gender")
        );
    }
}
