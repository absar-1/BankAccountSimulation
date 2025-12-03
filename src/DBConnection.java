import java.sql.Connection;
import java. sql.DriverManager;
import java. sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:sqlserver://banking-sqlserver.database.windows.net:1433;"
            + "database=BankingDB;"
            + "encrypt=true;"
            + "trustServerCertificate=false;"
            + "loginTimeout=30;";

    private static final String USER = "adminUser";
    private static final String PASSWORD = "bAnkingseRver21";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            System. out.println("Connected to Azure SQL Database successfully!");
        } catch (SQLException e) {
            System. out.println("Connection failed!");
            e. printStackTrace();
        }
    }
}