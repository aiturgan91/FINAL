package org.example.employeedb;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HelloController {
    @FXML private TextField textFieldName, textFieldPosition, textFieldSalary,
            textFieldPhoneNumber, textFieldEmail, textFieldSearch;
    @FXML private DatePicker datePickerHireDate;
    @FXML private ComboBox<String> comboBoxEmploymentType, comboBoxGender;
    @FXML private TableView<Employee> tableViewEmployees;
    @FXML private TableColumn<Employee, String> columnEmployeeName, columnEmployeePosition,
            columnPhoneNumber, columnEmail, columnGender;
    @FXML private TableColumn<Employee, Double> columnEmployeeSalary, columnCalculatedSalary;
    @FXML private TableColumn<Employee, LocalDate> columnEmployeeHireDate;
    @FXML private TableColumn<Employee, EmploymentType> columnEmploymentType;
    @FXML private Button btnGoToPage3;

    @FXML
    private Button analyzeEmployeeButton;
    private final EmployeeDAO employeeDAO;

    public HelloController() {
        this.employeeDAO = new EmployeeDAO();
    }

    @FXML
    private void initialize() {

    }

    private void refreshTableView() {
    }

    private void initializeColumns() {
        // Use method references and lambda for more concise column value factories
        columnEmployeeName.setCellValueFactory(cell -> cell.getValue().nameProperty());
        columnEmployeePosition.setCellValueFactory(cell -> cell.getValue().positionProperty());
        columnEmployeeSalary.setCellValueFactory(cell -> cell.getValue().salaryProperty().asObject());
        columnEmployeeHireDate.setCellValueFactory(cell -> cell.getValue().hireDateProperty());
        columnEmploymentType.setCellValueFactory(cell -> cell.getValue().employmentTypeProperty());
        columnPhoneNumber.setCellValueFactory(cell -> cell.getValue().phoneNumberProperty());
        columnEmail.setCellValueFactory(cell -> cell.getValue().emailProperty());
        columnGender.setCellValueFactory(cell -> cell.getValue().genderProperty());
        columnCalculatedSalary.setCellValueFactory(cell -> cell.getValue().calculatedSalaryProperty().asObject());
    }

    private void setupComboBoxes() {
        comboBoxEmploymentType.setItems(FXCollections.observableArrayList(
                EmploymentType.FULL_TIME.toString(),
                EmploymentType.PART_TIME.toString(),
                EmploymentType.CONTRACTOR.toString()
        ));
        comboBoxGender.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
    }

    private void setupTableSelectionListener() {
        tableViewEmployees.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) ->
                        populateFields(newSelection));
    }

    private void populateFields(Employee employee) {
        if (employee != null) {
            textFieldName.setText(employee.getName());
            textFieldPosition.setText(employee.getPosition());
            textFieldSalary.setText(String.valueOf(employee.getSalary()));
            datePickerHireDate.setValue(employee.getHireDate());
            textFieldPhoneNumber.setText(employee.getPhoneNumber());
            textFieldEmail.setText(employee.getEmail());
            comboBoxGender.setValue(employee.getGender());
            comboBoxEmploymentType.setValue(employee.getEmploymentType().toString());
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
        try {
            Employee employee = createEmployeeFromFields();
            if (employee != null) {
                employeeDAO.createEmployee(employee);
                refreshTableView();
                clearFields();
                showSuccessNotification("Employee added successfully");
            }
        } catch (ValidationException e) {
            showAlert(e.getMessage());
        }
    }

    private Employee createEmployeeFromFields() throws ValidationException {
        String name = validateTextField(textFieldName, "Name");
        String position = validateTextField(textFieldPosition, "Position");
        double salary = validateSalary(textFieldSalary);
        LocalDate hireDate = validateHireDate(datePickerHireDate);
        String phoneNumber = validateTextField(textFieldPhoneNumber, "Phone Number");
        String email = validateEmail(textFieldEmail);
        String gender = validateComboBox(comboBoxGender, "Gender");
        String employmentTypeText = validateComboBox(comboBoxEmploymentType, "Employment Type");

        EmploymentType employmentType = EmploymentType.valueOf(employmentTypeText);
        int nextId = employeeDAO.getNextId();

        Employee employee = new Employee(nextId, name, position, salary, hireDate,
                employmentType, phoneNumber, email, gender);
        employee.setCalculatedSalary(calculateSalary(employee));

        return employee;
    }

    private String validateTextField(TextField field, String fieldName) throws ValidationException {
        String value = field.getText().trim();
        if (value.isEmpty()) {
            throw new ValidationException(fieldName + " cannot be empty");
        }
        return value;
    }

    private double validateSalary(TextField field) throws ValidationException {
        try {
            return Double.parseDouble(field.getText());
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid salary. Please enter a valid number.");
        }
    }

    private LocalDate validateHireDate(DatePicker datePicker) throws ValidationException {
        LocalDate date = datePicker.getValue();
        if (date == null) {
            throw new ValidationException("Hire date must be selected");
        }
        return date;
    }

    private String validateEmail(TextField field) throws ValidationException {
        String email = field.getText().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new ValidationException("Invalid email format");
        }
        return email;
    }

    private String validateComboBox(ComboBox<String> comboBox, String fieldName) throws ValidationException {
        String value = comboBox.getValue();
        if (value == null || value.isEmpty()) {
            throw new ValidationException(fieldName + " must be selected");
        }
        return value;
    }

    @FXML
    private void handleUpdateEmployee() {
        try {
            Employee selectedEmployee = tableViewEmployees.getSelectionModel().getSelectedItem();
            if (selectedEmployee == null) {
                showAlert("Please select an employee to update");
                return;
            }

            Employee updatedEmployee = createEmployeeFromFields();
            if (updatedEmployee != null) {
                // Preserve the original ID
                updatedEmployee.setId(selectedEmployee.getId());

                employeeDAO.updateEmployee(updatedEmployee);
                refreshTableView();
                clearFields();
                showSuccessNotification("Employee updated successfully");
            }
        } catch (ValidationException e) {
            showAlert(e.getMessage());
        }
    }

    @FXML
    private void handleRemoveEmployee() {
        Employee selectedEmployee = tableViewEmployees.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            employeeDAO.deleteEmployee(selectedEmployee.getId());
            refreshTableView();
            showSuccessNotification("Employee removed successfully");
        } else {
            showAlert("Please select an employee to remove");
        }
    }

    @FXML
    private void handleSearch() {
        String searchText = textFieldSearch.getText().trim();
        List<Employee> filteredEmployees = employeeDAO.getAllEmployees().stream()
                .filter(emp -> searchMatches(emp, searchText))
                .collect(Collectors.toList());

        tableViewEmployees.getItems().setAll(filteredEmployees);

        if (filteredEmployees.isEmpty()) {
            showAlert("No employees found matching the search criteria");
        }
    }

    private boolean searchMatches(Employee emp, String searchText) {
        return String.valueOf(emp.getId()).equals(searchText) ||
                emp.getName().toLowerCase().contains(searchText.toLowerCase());
    }

    @FXML
    private void handleShowAll() {
        refreshTableView();
    }

    @FXML
    private void handleDeleteAll() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete All Employees");
        confirmAlert.setContentText("Are you sure you want to delete all employees? This action cannot be undone.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            employeeDAO.deleteAllEmployees();
            refreshTableView();
            showSuccessNotification("All employees deleted");
        }
    }

    @FXML
    private void handleCalculateSalaries() {
        Employee selectedEmployee = tableViewEmployees.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("Please select an employee to calculate salary");
            return;
        }

        double calculatedSalary = calculateSalary(selectedEmployee);
        selectedEmployee.setCalculatedSalary(calculatedSalary);
        employeeDAO.updateEmployee(selectedEmployee);
        refreshTableView();

        showSuccessNotification("Salary calculated: $" + String.format("%.2f", calculatedSalary));
    }

    // Implemented separately based on business logic
    private double calculateSalary(Employee employee) {
        // Placeholder implementation - replace with actual calculation logic
        return employee.getSalary();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void handleGoToPage3(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/employeedb/data-view.fxml"));
            AnchorPane dataControllerPage = loader.load();

            Stage stage = new Stage();
            stage.setTitle("DataController");
            stage.setScene(new Scene(dataControllerPage));
            stage.show();

            // Close current window
            Stage currentStage = (Stage) btnGoToPage3.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to load DataController page: " + e.getMessage());
        }
    }

    public void handleAnalyzeEmployeeButton(ActionEvent actionEvent) {
    }

    public class MainController {
        @FXML
        private Button analyzeEmployeeButton;

        @FXML
        private void handleAnalyzeEmployeeButton(ActionEvent event) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/employeedb/data-view.fxml"));
                Parent newPage = loader.load();
                Stage stage = new Stage();  //(Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(newPage);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void handleGoToEmployeeData(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("data-view.fxml"));
            Parent employeeDataPage = loader.load();
            Stage stage = new Stage(); //(Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(employeeDataPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @FXML
    private void handleExitApplication() {
        System.exit(0);
    }
}


// Custom exception for validation
class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}