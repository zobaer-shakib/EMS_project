import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import dao.EmployeeDAOImpl;
import model.Employee;

public class EmployeeManagementGUI extends JFrame {

    private JTextField itf, ntf, stf;
    private JComboBox<String> pbox, deptbox;
    private JSpinner entrydate;
    private EmployeeDAOImpl employeeDAO;

    public EmployeeManagementGUI() {
        setTitle("Employee Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        Container c = getContentPane();
        c.setLayout(null);

        JLabel idLabel = new JLabel("Employee ID:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 16));
        idLabel.setBounds(50, 30, 150, 30);
        c.add(idLabel);

        itf = new JTextField();
        itf.setBounds(200, 30, 250, 30);
        c.add(itf);

        JLabel nl = new JLabel("Name:");
        nl.setFont(new Font("Arial", Font.BOLD, 16));
        nl.setBounds(50, 80, 150, 30);
        c.add(nl);

        ntf = new JTextField();
        ntf.setBounds(200, 80, 250, 30);
        c.add(ntf);

        JLabel pl = new JLabel("Position:");
        pl.setFont(new Font("Arial", Font.BOLD, 16));
        pl.setBounds(50, 130, 150, 30);
        c.add(pl);

        pbox = new JComboBox<>(new String[]{"Engineer","GM", "Developer","Foreign Cadre","Admin Cadre","Designer", "Tester","Network Engineer","Data Scientist","swiper"});
        pbox.setBounds(200, 130, 250, 30);
        c.add(pbox);

        JLabel deptl = new JLabel("Department:");
        deptl.setFont(new Font("Arial", Font.BOLD, 16));
        deptl.setBounds(50, 180, 150, 30);
        c.add(deptl);

        deptbox = new JComboBox<>(new String[]{"Software Development", "IT", "Finance", "Marketing","BCS","Human Resource","Tax"});
        deptbox.setBounds(200, 180, 250, 30);
        c.add(deptbox);
        
        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        salaryLabel.setBounds(50, 230, 150, 30);
        c.add(salaryLabel);

        stf = new JTextField();
        stf.setBounds(200, 230, 250, 30);
        c.add(stf);

        JLabel entryDateLabel = new JLabel("Entry Date:");
        entryDateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        entryDateLabel.setBounds(50, 280, 150, 30);
        c.add(entryDateLabel);

        entrydate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(entrydate, "yyyy-MM-dd");
        entrydate.setEditor(dateEditor);
        entrydate.setBounds(200, 280, 250, 30);
        c.add(entrydate);

        JButton ab = new JButton("Add Employee");
        ab.setBounds(50, 330, 200, 40);
        ab.setBackground(new Color(66, 103, 178));
        ab.setForeground(Color.WHITE);
        ab.addActionListener(e -> addEmployee());
        c.add(ab);

        JButton vb = new JButton("View Employee");
        vb.setBounds(270, 330, 200, 40);
        vb.setBackground(new Color(66, 103, 178));
        vb.setForeground(Color.WHITE);
        vb.addActionListener(e -> viewEmployee());
        c.add(vb);

        JButton ub = new JButton("Update Employee");
        ub.setBounds(490, 330, 200, 40);
        ub.setBackground(new Color(66, 103, 178));
        ub.setForeground(Color.WHITE);
        ub.addActionListener(e -> updateEmployee());
        c.add(ub);

        JButton delb = new JButton("Delete Employee");
        delb.setBounds(50, 380, 200, 40);
        delb.setBackground(new Color(66, 103, 178));
        delb.setForeground(Color.WHITE);
        delb.addActionListener(e -> deleteEmployee());
        c.add(delb);

        JButton viewAllButton = new JButton("View All Employees");
        viewAllButton.setBounds(270, 380, 200, 40);
        viewAllButton.setBackground(new Color(66, 103, 178));
        viewAllButton.setForeground(Color.WHITE);
        viewAllButton.addActionListener(e -> viewAllEmployees());
        c.add(viewAllButton);

        JButton exb = new JButton("Exit");
        exb.setBounds(490, 380, 200, 40);
        exb.setBackground(new Color(66, 103, 178));
        exb.setForeground(Color.WHITE);
        exb.addActionListener(e -> exitApplication());
        c.add(exb);

        // Initialize the database 
        initializeDatabase();
        setVisible(true);
    }

    private void initializeDatabase() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/EmployeeDB";
        String username = "root"; 
        String password = "Hypopharynx7580#";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            employeeDAO = new EmployeeDAOImpl(connection); 
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
        }
    }

   private void addEmployee() {
    String idText = itf.getText();  
    String name = ntf.getText();
    String position = (String) pbox.getSelectedItem(); 
    String salaryText = stf.getText();
    String department = (String) deptbox.getSelectedItem(); 
    Date entryDate = (Date) entrydate.getValue(); 

    if (idText.isEmpty() || name.isEmpty() || position == null || salaryText.isEmpty() || department == null) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields, including Employee ID.");
        return;
    }

    try {
        int id = Integer.parseInt(idText); 
        double salary = Double.parseDouble(salaryText);
        Employee employee = new Employee(id, name, position, salary, department, entryDate);
        
        
        if (employeeDAO.addEmployee(employee)) {  
            JOptionPane.showMessageDialog(this, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        }
        clearFields();
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid input. Please enter numeric values for Employee ID and Salary.");
    }
}


    private void viewEmployee() {
        String idText = itf.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Employee ID.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            Employee employee = employeeDAO.getEmployee(id);  
            if (employee != null) {
                ntf.setText(employee.getName());
                pbox.setSelectedItem(employee.getPosition());
                stf.setText(String.valueOf(employee.getSalary()));
                deptbox.setSelectedItem(employee.getDepartment());
                entrydate.setValue(employee.getEntryDate()); 
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found!");  
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Employee ID. Please enter a numeric ID.");
        }
    }

    private void viewAllEmployees() {
        JFrame viewAllFrame = new JFrame("All Employees");
        viewAllFrame.setSize(800, 400);
        viewAllFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewAllFrame.setLocationRelativeTo(this);

        String[] coln = {"ID", "Name", "Position", "Salary", "Department", "Entry Date"};
        DefaultTableModel tableModel = new DefaultTableModel(coln, 0);
        JTable employeeTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        viewAllFrame.add(scrollPane, BorderLayout.CENTER);

        List<Employee> employees = employeeDAO.getAllEmployees();
        if (employees.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employees found.");
            return;
        }

        for (Employee employee : employees) {
            Object[] row = {employee.getId(), employee.getName(), employee.getPosition(),
                    employee.getSalary(), employee.getDepartment(), employee.getEntryDate()};
            tableModel.addRow(row);
        }

        viewAllFrame.setVisible(true);
    }

    private void updateEmployee() {
        String idText = itf.getText();
        String name = ntf.getText();
        String position = (String) pbox.getSelectedItem();
        String salaryText = stf.getText();
        String department = (String) deptbox.getSelectedItem();
        Date entryDate = (Date) entrydate.getValue();

        if (idText.isEmpty() || name.isEmpty() || position == null || salaryText.isEmpty() || department == null) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields, including Employee ID.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            Employee employee = employeeDAO.getEmployee(id); 
            if (employee == null) {
                JOptionPane.showMessageDialog(this, "Invalid updating! This ID isn't registered."); 
                return;
            }

            double salary = Double.parseDouble(salaryText);
            Employee updatedEmployee = new Employee(id, name, position, salary, department, entryDate);
            employeeDAO.updateEmployee(updatedEmployee); 
            JOptionPane.showMessageDialog(this, "Employee updated successfully!");
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter numeric values for Employee ID and Salary.");
        }
    }

    private void deleteEmployee() {
        String idText = itf.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Employee ID.");
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            Employee employee = employeeDAO.getEmployee(id); 
            if (employee == null) {
                JOptionPane.showMessageDialog(this, "Invalid deletion! This ID isn't registered.");
                return;
            }

            employeeDAO.deleteEmployee(id);
            JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Employee ID. Please enter a numeric ID.");
        }
    }

    private void exitApplication() {
        System.exit(0);
    }

    private void clearFields() {
        itf.setText("");
        ntf.setText("");
        pbox.setSelectedIndex(0);
        stf.setText("");
        deptbox.setSelectedIndex(0);
        entrydate.setValue(new Date());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginClass::new);
    }
}
