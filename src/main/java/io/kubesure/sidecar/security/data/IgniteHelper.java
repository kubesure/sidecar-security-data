package io.kubesure.sidecar.security.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class IgniteHelper {

    public static Customer getCustomerByID(String ID) throws Exception {
        Class.forName("org.apache.ignite.IgniteJdbcDriver");
        // Open JDBC connection
        Connection conn = DriverManager.getConnection(
                "jdbc:ignite:thin://127.0.0.1/");

        // Get data
        try (Statement stmt = conn.createStatement()) {
            try (ResultSet rs =
                         stmt.executeQuery("SELECT p.name, c.name " +
                            " FROM person p, city c " +
                            " WHERE p.city_id = c.id")) {

                System.out.println("Query results:");

                while (rs.next())
                    System.out.println(">>>    " +
                            rs.getString(1) +
                            ", " +
                            rs.getString(2));
            }
        }
        return null;
    }
}