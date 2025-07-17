package dao;

import model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class EmployeeDAOImpl implements EmployeeDAO {

    private Connection connection;

    public EmployeeDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
     public boolean addEmployee(Employee employee) {
        String query = "INSERT INTO employees (id, name, position, salary, department, entry_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, employee.getId());
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getPosition());
            stmt.setDouble(4, employee.getSalary());
            stmt.setString(5, employee.getDepartment());

           
            if (employee.getEntryDate() != null) {
                stmt.setDate(6, new java.sql.Date(employee.getEntryDate().getTime())); 
            } else {
                stmt.setNull(6, Types.DATE);
            }

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; 
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // MySQL error code for duplicate entry
                JOptionPane.showMessageDialog(null, "Duplicate ID! Employee adding unsuccessful.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                logError(e);
            }
            return false;
        }
    }




    @Override
    public Employee getEmployee(int id) {
        String query = "SELECT * FROM employees WHERE id=?";
        Employee employee = null;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                employee = new Employee(rs.getInt("id"), rs.getString("name"), rs.getString("position"),
                        rs.getDouble("salary"), rs.getString("department"), rs.getDate("entry_date"));
            }
        } catch (SQLException e) {
            logError(e);
        }
        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String position = resultSet.getString("position");
                double salary = resultSet.getDouble("salary");
                String department = resultSet.getString("department");
                Date entryDate = resultSet.getDate("entry_date");

                Employee employee = new Employee(id, name, position, salary, department, entryDate);
                employees.add(employee);
            }
        } catch (SQLException e) {
            logError(e);
        }
        return employees;
    }

    @Override
    public void updateEmployee(Employee employee) {
        String query = "UPDATE employees SET name=?, position=?, salary=?, department=?, entry_date=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getPosition());
            stmt.setDouble(3, employee.getSalary());
            stmt.setString(4, employee.getDepartment());
            if (employee.getEntryDate() != null) {
                stmt.setDate(5, new java.sql.Date(employee.getEntryDate().getTime()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            stmt.setInt(6, employee.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logError(e);
        }
    }

    @Override
    public void deleteEmployee(int id) {
        String query = "DELETE FROM employees WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logError(e);
        }
    }

   
    private void logError(SQLException e) {
        // Replace with a real logging framework in production, such as SLF4J, Log4j, etc.
        System.err.println("SQL Error: " + e.getMessage());
        e.printStackTrace();
    }
}
