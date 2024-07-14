package telran.employees;

import org.json.JSONObject;
import telran.io.Persistable;

import java.io.*;
import java.util.*;

public class CompanyMapsImpl implements Company, Persistable {

    private TreeMap<Long, Employee> employees = new TreeMap<>();
    private HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
    private TreeMap<Float, List<Manager>> factorManagers = new TreeMap<>(Collections.reverseOrder());

    @Override
    public void addEmployee(Employee empl) {
        if (employees.containsKey(empl.getId())) {
            throw new IllegalStateException();
        }
        employees.put(empl.getId(), empl);
        addEmployeeToDepartment(empl);
        addFactorManager(empl);
    }

    private void addFactorManager(Employee empl) {
        if (empl instanceof Manager) {
            Manager manager = (Manager) empl;
            factorManagers
                    .computeIfAbsent(manager.factor, k -> new ArrayList<>())
                    .add(manager);
        }
    }

    private void addEmployeeToDepartment(Employee empl) {
        employeesDepartment
                .computeIfAbsent(empl.getDepartment(), k -> new ArrayList<>())
                .add(empl);
    }

    @Override
    public Employee getEmployee(long id) {
        return employees.get(id);
    }

    @Override
    public Employee removeEmployee(long id) {
        Employee employee = employees.remove(id);
        if (employee == null) {
            throw new NoSuchElementException();
        }

        String department = employee.getDepartment();
        List<Employee> departmentList = employeesDepartment.get(department);
        if (departmentList != null) {
            departmentList.remove(employee);
            if (departmentList.isEmpty()) {
                employeesDepartment.remove(department);
            }
        }

        if (employee instanceof Manager) {
            removeFactorManager((Manager) employee);
        }
        return employee;
    }

    private void removeFactorManager(Manager manager) {
        float factor = manager.factor;
        List<Manager> managerList = factorManagers.get(factor);
        if (managerList != null) {
            managerList.remove(manager);
            if (managerList.isEmpty()) {
                factorManagers.remove(factor);
            }
        }
    }

    @Override
    public int getDepartmentBudget(String department) {
        return employeesDepartment
                .getOrDefault(department, Collections.emptyList())
                .stream()
                .mapToInt(Employee::computeSalary)
                .sum();
    }

    @Override
    public String[] getDepartments() {
        return employeesDepartment.keySet().stream().sorted().toArray(String[]::new);
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        return factorManagers.isEmpty()
                ? new Manager[0]
                : factorManagers.firstEntry().getValue().toArray(new Manager[0]);
    }

    @Override
    public void save(String filePathStr) {
        try (PrintWriter jsonFile = new PrintWriter(filePathStr)){
            for (Employee employee : employees.values()) jsonFile.write(employee.getJSON() + "\n");
        } catch (IOException e) {
            throw new RuntimeException("Can't save file: wrong file name or file type", e);
        }
    }

    @Override
    public void restore(String filePathStr) {
        try (BufferedReader readFromJsonFile = new BufferedReader(new FileReader(filePathStr))) {
            readFromJsonFile.lines().map(jsonStringLine -> (Employee) new Employee().setObject(jsonStringLine)).forEach(this::addEmployee);
        } catch (IOException e) {
            throw new RuntimeException("Can't restore file: wrong file name or file type", e);
        }
    }

    @Override
    public Iterator<Employee> iterator() {
        return employees.values().iterator();
    }
}