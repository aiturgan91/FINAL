package org.example.employeedb;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import org.example.employeedb.EmploymentType;

import java.time.LocalDate;

public class Employee {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty position;
    private final DoubleProperty salary;
    private final ObjectProperty<LocalDate> hireDate;
    private final ObjectProperty<EmploymentType> employmentType;
    private final StringProperty phoneNumber;
    private final StringProperty email;
    private final StringProperty gender;
    private final DoubleProperty calculatedSalary; // Calculated Salary Property

    // New properties
    private final StringProperty address;
    private final StringProperty department;
    private final StringProperty performance;
    private final StringProperty status;
    private final StringProperty experience; // New experience property

    // Constructor 1
    public Employee(int id, String name, String position, double salary, LocalDate hireDate, EmploymentType employmentType, String phoneNumber, String email, String gender) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.position = new SimpleStringProperty(position);
        this.salary = new SimpleDoubleProperty(salary);
        this.hireDate = new SimpleObjectProperty<>(hireDate);
        this.employmentType = new SimpleObjectProperty<>(EmploymentType.FULL_TIME); // Default value
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.email = new SimpleStringProperty(email);
        this.gender = new SimpleStringProperty(gender);
        this.calculatedSalary = new SimpleDoubleProperty(0.0); // Initialize calculated salary
        this.address = new SimpleStringProperty("");
        this.department = new SimpleStringProperty("");
        this.performance = new SimpleStringProperty("");
        this.status = new SimpleStringProperty("");
        this.experience = new SimpleStringProperty(""); // Initialize experience property
    }

    // Constructor 2
    public Employee(int id, String name, String position, double salary, LocalDate hireDate,
                    EmploymentType employmentType, String phoneNumber, String email, String gender,
                    String address, String department, String performance, String status, String experience) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.position = new SimpleStringProperty(position);
        this.salary = new SimpleDoubleProperty(salary);
        this.hireDate = new SimpleObjectProperty<>(hireDate);
        this.employmentType = new SimpleObjectProperty<>(employmentType);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.email = new SimpleStringProperty(email);
        this.gender = new SimpleStringProperty(gender);
        this.calculatedSalary = new SimpleDoubleProperty(0.0); // Initialize calculated salary
        this.address = new SimpleStringProperty(address);
        this.department = new SimpleStringProperty(department);
        this.performance = new SimpleStringProperty(performance);
        this.status = new SimpleStringProperty(status);
        this.experience = new SimpleStringProperty(experience); // Initialize experience property
    }

    // Getters and setters for all properties
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getPosition() {
        return position.get();
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public StringProperty positionProperty() {
        return position;
    }

    public double getSalary() {
        return salary.get();
    }

    public void setSalary(double salary) {
        this.salary.set(salary);
    }

    public DoubleProperty salaryProperty() {
        return salary;
    }

    public LocalDate getHireDate() {
        return hireDate.get();
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate.set(hireDate);
    }

    public ObjectProperty<LocalDate> hireDateProperty() {
        return hireDate;
    }

    public EmploymentType getEmploymentType() {
        return employmentType.get();
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType.set(employmentType);
    }

    public ObjectProperty<EmploymentType> employmentTypeProperty() {
        return employmentType;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public double getCalculatedSalary() {
        return calculatedSalary.get();
    }

    public void setCalculatedSalary(double calculatedSalary) {
        this.calculatedSalary.set(calculatedSalary);
    }

    public DoubleProperty calculatedSalaryProperty() {
        return calculatedSalary;
    }

    // New getters and setters for additional properties
    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public StringProperty addressProperty() {
        return address;
    }

    public String getDepartment() {
        return department.get();
    }

    public void setDepartment(String department) {
        this.department.set(department);
    }

    public StringProperty departmentProperty() {
        return department;
    }

    public String getPerformance() {
        return performance.get();
    }

    public void setPerformance(String performance) {
        this.performance.set(performance);
    }

    public StringProperty performanceProperty() {
        return performance;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }

    // Experience Property
    public String getExperience() {
        return experience.get();
    }

    public void setExperience(String experience) {
        this.experience.set(experience);
    }

    public StringProperty experienceProperty() {
        return experience;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id.get() +
                ", name='" + name.get() + '\'' +
                ", position='" + position.get() + '\'' +
                ", salary=" + salary.get() +
                ", hireDate=" + hireDate.get() +
                ", employmentType=" + employmentType.get() +
                ", phoneNumber='" + phoneNumber.get() + '\'' +
                ", email='" + email.get() + '\'' +
                ", gender='" + gender.get() + '\'' +
                ", calculatedSalary=" + calculatedSalary.get() +
                ", address='" + address.get() + '\'' +
                ", department='" + department.get() + '\'' +
                ", performance='" + performance.get() + '\'' +
                ", status='" + status.get() + '\'' +
                ", experience='" + experience.get() + '\'' +
                '}';
    }
}
