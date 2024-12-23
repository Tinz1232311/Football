package Method;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Content.SQLContent;

public class DataConnect {
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(SQLContent.url, SQLContent.user, SQLContent.password);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
