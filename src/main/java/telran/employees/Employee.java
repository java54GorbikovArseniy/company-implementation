package telran.employees;

import org.json.JSONObject;
import telran.io.JSONable;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class Employee implements Comparable<Employee>, JSONable {
    private long id;
    private int basicSalary;
    private String department;

    public Employee() {
    }

    public Employee(long id, int basicSalary, String department) {
        this.id = id;
        this.basicSalary = basicSalary;
        this.department = department;
    }

    @Override
    public int compareTo(Employee o) {
        return Long.compare(this.id, o.id);
    }

    public long getId() {
        return id;
    }

    public int getBasicSalary() {
        return basicSalary;
    }

    public String getDepartment() {
        return department;
    }

    public int computeSalary() {
        return basicSalary;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Employee other = (Employee) obj;
        return id == other.id;
    }

    @Override
    public String getJSON() {
        JSONObject jsonObject = new JSONObject();
        fillJSONObject(jsonObject);
        return jsonObject.toString();
    }

    protected void fillJSONObject(JSONObject jsonObject) {
        fillClassName(jsonObject);
        jsonObject.put("id", id);
        jsonObject.put("department", department);
        jsonObject.put("basicSalary", basicSalary);
    }

    protected void fillClassName(JSONObject jsonObject) {
        if (!jsonObject.has("className")) {
            jsonObject.put("className", getClass().getName());
        }
    }

    @Override
    public JSONable setObject(String json) {
        JSONObject jsonObject = new JSONObject(json);
        String className = jsonObject.getString("className");
        try {
            Employee employee = (Employee) Class.forName(className).getDeclaredConstructor().newInstance();
            employee.fillEmployee(jsonObject);
            return employee;
        } catch (Exception e) {
            throw new RuntimeException("Wrong parameters for setObject " + e);
        }
    }

    protected void fillEmployee(JSONObject jsonObject) {
        id = jsonObject.getLong("id");
        department = jsonObject.getString("department");
        basicSalary = jsonObject.getInt("basicSalary");
    }
}
