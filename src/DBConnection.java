import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static void main(String[] args) {

        // JDBC connection URL
        String url = "jdbc:sqlserver://banking-sqlserver.database.windows.net:1433;"
                + "database=BankingDB;"
                + "encrypt=true;"
                + "trustServerCertificate=false;"
                + "loginTimeout=30;";

        String user = "adminUser@banking-sqlserver";
        String password = "bAnkingseRver21";

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to Azure SQL Database successfully!");
            con.close();
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}
