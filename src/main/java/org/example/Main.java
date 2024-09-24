package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:test");
        config.setUsername("sa");
        config.setPassword("");

        try (HikariDataSource ds = new HikariDataSource(config)) {
            executeSqlScript(ds);
            List<EmployeeDTO> employees = selectAllEmployees(ds);
            employees.forEach(System.out::println);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void executeSqlScript(HikariDataSource dataSource) throws SQLException {
        final String path = "src/main/resources/schema.sql";

        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            FileInputStream fis = new FileInputStream(path);

            String script = new Scanner(fis, StandardCharsets.UTF_8)
                    .useDelimiter("\\A")
                    .next();

            statement.execute(script); // inicializando script en DB

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private static List<EmployeeDTO> selectAllEmployees(HikariDataSource dataSource) throws SQLException {
        List<EmployeeDTO> employees = new ArrayList<>();

        final String query = "SELECT " +
                "e.id, e.name, e.email, e.department_id, d.name AS department_name " +
                "FROM employees e " +
                "JOIN departments d ON e.department_id = d.id";

        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                EmployeeDTO employeeDTO = new EmployeeDTO();
                employeeDTO.setId(resultSet.getInt("id"));
                employeeDTO.setName(resultSet.getString("name"));
                employeeDTO.setEmail(resultSet.getString("email"));
                employeeDTO.setDepartmentId(resultSet.getInt("department_id"));
                employeeDTO.setDepartmentName(resultSet.getString("department_name"));

                employees.add(employeeDTO);
            }

        }

        return employees;
    }


}
