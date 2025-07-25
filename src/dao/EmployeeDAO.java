package dao;

import model.Employee;
import java.util.List;

public interface EmployeeDAO { 
    boolean addEmployee(Employee employee);
    Employee getEmployee(int id);
    List<Employee> getAllEmployees();
    void updateEmployee(Employee employee);
    void deleteEmployee(int id);
}
