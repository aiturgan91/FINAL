package org.example.employeedb;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class HelloController {
    @FXML private TextField textFieldName;
    @FXML private TextField textFieldPosition;
    @FXML private TextField textFieldSalary;
    @FXML private DatePicker datePickerHireDate;
    @FXML private ComboBox<String> comboBoxEmploymentType;
    @FXML private TextField textFieldPhoneNumber;
    @FXML private TextField textFieldEmail;
    @FXML private ComboBox<String> comboBoxGender;
    @FXML private TextField textFieldSearch;
    @FXML private TableView<Employee> tableViewEmployees;
    @FXML private TableColumn<Employee, String> columnEmployeeName;
    @FXML private TableColumn<Employee, String> columnEmployeePosition;
    @FXML private TableColumn<Employee, Double> columnEmployeeSalary;
    @FXML private TableColumn<Employee, LocalDate> columnEmployeeHireDate;
    @FXML private TableColumn<Employee, EmploymentType> columnEmploymentType;
    @FXML private TableColumn<Employee, String> columnPhoneNumber;
    @FXML private TableColumn<Employee, String> columnEmail;
    @FXML private TableColumn<Employee, String> columnGender;
    @FXML private TableColumn<Employee, Double> columnCalculatedSalary;

    private final EmployeeDAO employeeDAO;

    public HelloController() {
        this.employeeDAO = new EmployeeDAO();
    }

    @FXML
    private void initialize() {
        // Bind columns to properties
        columnEmployeeName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnEmployeePosition.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
        columnEmployeeSalary.setCellValueFactory(cellData -> cellData.getValue().salaryProperty().asObject());
        columnEmployeeHireDate.setCellValueFactory(cellData -> cellData.getValue().hireDateProperty());
        columnEmploymentType.setCellValueFactory(cellData -> cellData.getValue().employmentTypeProperty());
        columnPhoneNumber.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
        columnEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        columnGender.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        columnCalculatedSalary.setCellValueFactory(cellData -> cellData.getValue().calculatedSalaryProperty().asObject());

        // Populate ComboBoxes
        comboBoxEmploymentType.setItems(FXCollections.observableArrayList("FULL_TIME", "PART_TIME", "CONTRACTOR"));
        comboBoxGender.setItems(FXCollections.observableArrayList("Male", "Female"));

        // Add listener to TableView selection
        tableViewEmployees.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> populateFields(newValue));

        refreshTableView();
    }

    private void populateFields(Employee selectedEmployee) {
        if (selectedEmployee != null) {
            textFieldName.setText(selectedEmployee.getName());
            textFieldPosition.setText(selectedEmployee.getPosition());
            textFieldSalary.setText(String.valueOf(selectedEmployee.getSalary()));
            datePickerHireDate.setValue(selectedEmployee.getHireDate());
            textFieldPhoneNumber.setText(selectedEmployee.getPhoneNumber());
            textFieldEmail.setText(selectedEmployee.getEmail());
            comboBoxGender.setValue(selectedEmployee.getGender());
            comboBoxEmploymentType.setValue(selectedEmployee.getEmploymentType().toString());
        } else {
            clearFields();
        }
    }

    private void clearFields() {
        textFieldName.clear();
        textFieldPosition.clear();
        textFieldSalary.clear();
        datePickerHireDate.setValue(null);
        comboBoxEmploymentType.setValue(null);
        textFieldPhoneNumber.clear();
        textFieldEmail.clear();
        comboBoxGender.setValue(null);
    }

    @FXML
    private void handleAddEmployee() {
        // Your code to handle adding an employee
        String name = textFieldName.getText();
        String position = textFieldPosition.getText();
        String salaryText = textFieldSalary.getText();
        LocalDate hireDate = datePickerHireDate.getValue();
        String employmentTypeText = comboBoxEmploymentType.getValue();
        String phoneNumber = textFieldPhoneNumber.getText();
        String email = textFieldEmail.getText();
        String gender = comboBoxGender.getValue();

        // Validate input
        if (name.isEmpty() || position.isEmpty() || salaryText.isEmpty() || hireDate == null || employmentTypeText == null || phoneNumber.isEmpty() || email.isEmpty() || gender == null) {
            System.out.println("Please fill in all fields.");
            return;
        }

        // Parse the salary input
        double salary;
        try {
            salary = Double.parseDouble(salaryText);
        } catch (NumberFormatException e) {
            System.out.println("Invalid salary. Please enter a valid number.");
            return;
        }

        // Create the employee
        EmploymentType employmentType = EmploymentType.valueOf(employmentTypeText);
        int nextId = employeeDAO.getNextId();
        Employee employee = new Employee(nextId, name, position, salary, hireDate, employmentType, phoneNumber, email, gender);
        double calculatedSalary = calculateSalary(employee);
        employee.setCalculatedSalary(calculatedSalary);
        employeeDAO.createEmployee(employee);
        refreshTableView();
        clearFields();
        System.out.println("Employee added: " + employee);
    }


    @FXML
    private void handleUpdateEmployee() {
        Employee selectedEmployee = tableViewEmployees.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            System.out.println("No employee selected.");
            return;
        }

        String name = textFieldName.getText();
        String position = textFieldPosition.getText();
        String salaryText = textFieldSalary.getText();
        LocalDate hireDate = datePickerHireDate.getValue();
        String employmentTypeText = comboBoxEmploymentType.getValue();
        String phoneNumber = textFieldPhoneNumber.getText();
        String email = textFieldEmail.getText();
        String gender = comboBoxGender.getValue();

        if (name.isEmpty() || position.isEmpty() || salaryText.isEmpty() || hireDate == null || employmentTypeText == null || phoneNumber.isEmpty() || email.isEmpty() || gender == null) {
            System.out.println("Please fill in all fields.");
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryText);
        } catch (NumberFormatException e) {
            System.out.println("Invalid salary. Please enter a valid number.");
            return;
        }

        selectedEmployee.setName(name);
        selectedEmployee.setPosition(position);
        selectedEmployee.setSalary(salary);
        selectedEmployee.setHireDate(hireDate);
        selectedEmployee.setEmploymentType(EmploymentType.valueOf(employmentTypeText));
        selectedEmployee.setPhoneNumber(phoneNumber);
        selectedEmployee.setEmail(email);
        selectedEmployee.setGender(gender);
        double calculatedSalary = calculateSalary(selectedEmployee);
        selectedEmployee.setCalculatedSalary(calculatedSalary);
        employeeDAO.updateEmployee(selectedEmployee);
        refreshTableView();
        clearFields();
        System.out.println("Employee updated: " + selectedEmployee);
    }

    private void refreshTableView() {
        List<Employee> employees = employeeDAO.getAllEmployees();
        tableViewEmployees.getItems().setAll(employees);  // Update the TableView items with the new data
        tableViewEmployees.refresh();  // Refresh the TableView to show the updated data
    }

    @FXML
    private void handleRemoveEmployee() {
        Employee selectedEmployee = tableViewEmployees.getSelectionModel().getSelectedItem();

        if (selectedEmployee != null) {
            employeeDAO.deleteEmployee(selectedEmployee.getId());
            refreshTableView();
            System.out.println("Employee removed: " + selectedEmployee);
        } else {
            System.out.println("Please select an employee to remove.");
        }
    }

    @FXML
    private void handleSearch() {
        String searchText = textFieldSearch.getText();
        List<Employee> filteredEmployees = employeeDAO.getAllEmployees().stream()
                .filter(emp -> String.valueOf(emp.getId()).equals(searchText) || emp.getName().equalsIgnoreCase(searchText))
                .collect(Collectors.toList());
        tableViewEmployees.getItems().setAll(filteredEmployees);
    }

    @FXML
    private void handleShowAll() {
        refreshTableView();
    }

    @FXML
    private void handleDeleteAll() {
        employeeDAO.deleteAllEmployees();
        refreshTableView();
        System.out.println("All employees deleted.");
    }

    @FXML
    public void handleCalculateSalaries() {
        Employee selectedEmployee = tableViewEmployees.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null) {
            System.out.println("No employee selected.");
            return;
        }

        double calculatedSalary = calculateSalary(selectedEmployee);
        selectedEmployee.setCalculatedSalary(calculatedSalary);
        System.out.println(selectedEmployee + " - Calculated Salary: " + calculatedSalary);
        tableViewEmployees.refresh();
    }

    private double calculateSalary(Employee employee) {
        double baseSalary = employee.getSalary();
        switch (employee.getEmploymentType()) {
            case FULL_TIME:
                return baseSalary;
            case PART_TIME:
                return baseSalary * 0.5;
            case CONTRACTOR:
                return baseSalary * 1.2;
            default:
                return baseSalary;
        }
    }

}

