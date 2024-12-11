package org.example.employeedb;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

import java.io.IOException;

public class DataController {

    @FXML private HBox Hbox;
    @FXML private AnchorPane aproch;
    @FXML private Button btnActiveEmployees, btnAttendance, btnDepartmentBudgets, btnHighSalaries, btnInactiveEmployees, btnJuniorSalaries, btnMiddleSalaries, btnSearch, btnTeamLeads, btnVacations;
    @FXML private TableColumn<?, ?> colAddress, colDepartment, colExperience, colId, colName, colPerformance, colSalary, colStatus;
    @FXML private VBox dfghjk;
    @FXML private TableView<Employee> employeeTable;
    @FXML private PieChart genderDistributionChart;
    @FXML private HBox hbox, hbox3;
    @FXML private Label lblTotalEmployees, lblTotalMen, lblTotalWomen;
    @FXML private BarChart<String, Number> salaryDistributionChart;
    @FXML private TextField txtSearchEmployee;
    @FXML private CategoryAxis xAxisSalary;
    @FXML private NumberAxis yAxisSalary;

    @FXML private Button backButton;

    // Event Handlers for Button Clicks
    @FXML
    private void handleEmployeeData(String dataType) {
        switch (dataType) {
            case "active":
                System.out.println("Displaying active employees...");
                break;
            case "inactive":
                System.out.println("Displaying inactive employees...");
                break;
            case "junior":
                System.out.println("Displaying junior salaries...");
                break;
            case "middle":
                System.out.println("Displaying middle salaries...");
                break;
            case "high":
                System.out.println("Displaying high salaries...");
                break;
            case "teamLeads":
                System.out.println("Displaying team leads...");
                break;
            case "vacations":
                System.out.println("Displaying employees on vacation...");
                break;
            case "attendance":
                System.out.println("Displaying attendance data...");
                break;
            default:
                System.out.println("Unknown data type.");
        }
        refreshEmployeeTable(dataType);
    }

    // Refresh Employee Table with New Data
    private void refreshEmployeeTable(String dataType) {
        // Fetch appropriate data based on the dataType and update the table
        // Example: employeeTable.setItems(fetchData(dataType));
    }

    // Search Employee Method
    @FXML
    private void handleSearch() {
        String searchQuery = txtSearchEmployee.getText();
        if (searchQuery.isEmpty()) {
            System.out.println("No search query entered.");
            return;
        }
        // Assuming fetchDataForSearch is a method that returns filtered data
        System.out.println("Searching for: " + searchQuery);
        employeeTable.setItems(fetchDataForSearch(searchQuery));
    }

    private ObservableList<Employee> fetchDataForSearch(String searchQuery) {
        return FXCollections.observableArrayList(); // Replace with actual search logic
    }

    // Update the Gender Distribution Pie Chart
    private void updateGenderDistributionChart() {
        // Example data for demonstration
        PieChart.Data male = new PieChart.Data("Male", 70);
        PieChart.Data female = new PieChart.Data("Female", 30);
        genderDistributionChart.getData().setAll(male, female);
    }

    // Update Salary Distribution Bar Chart
    private void updateSalaryDistributionChart() {
        // Example salary distribution
        salaryDistributionChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Junior", 3000));
        series.getData().add(new XYChart.Data<>("Middle", 5000));
        series.getData().add(new XYChart.Data<>("High", 7000));
        salaryDistributionChart.getData().add(series);
    }

    // Update Employee Statistics Labels
    private void updateEmployeeStatistics() {
        // Assuming total employees and gender statistics are fetched from a service
        lblTotalEmployees.setText("Total Employees: 100");
        lblTotalMen.setText("Total Men: 60");
        lblTotalWomen.setText("Total Women: 40");
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/employeedb/hello-view.fxml"));
            Parent newPage = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(newPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methods for employee analysis
    @FXML
    private void performAnalysis() {
        // Analysis logic
    }

    // Helper method to fetch data for various categories (active, inactive, etc.)
    private ObservableList<Employee> fetchData(String dataType) {
        return FXCollections.observableArrayList(); // Replace with actual data fetching logic
    }

    @FXML
    private void handleAnalyzeEmployeeButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/employeedb/employee_HelloController.fxml"));
            Parent analysisPage = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(analysisPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void main() {
    }
}
