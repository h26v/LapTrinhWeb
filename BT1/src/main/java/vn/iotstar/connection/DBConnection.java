package vn.iotstar.connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private final String url =
            "jdbc:mysql://localhost:3306/servletcrudmvc?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private final String userID = "root";
    private final String password = ""; // Doi mat khau MySQL cua ban tai day

    public Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, userID, password);
    }
}
