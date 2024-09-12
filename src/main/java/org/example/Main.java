package org.example;

import com.zaxxer.hikari.HikariDataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {



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

}
