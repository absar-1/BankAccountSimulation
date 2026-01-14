import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoanOptionsDAO {

    /**
     * Get all loan options/packages from database.
     * Returns: [id, package_name, loan_amount, minimum_balance, duration_months, interest_percentage]
     */
    public static String[][] getAllLoanOptions() {
        ArrayList<String[]> rows = new ArrayList<>();
        String sql = "SELECT id, package_name, CONVERT(VARCHAR(20), loan_amount) AS loan_amount, " +
                "CONVERT(VARCHAR(20), minimum_balance) AS minimum_balance, duration_months, " +
                "CONVERT(VARCHAR(10), interest_percentage) AS interest_percentage " +
                "FROM LoanOptions ORDER BY minimum_balance ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String[] row = new String[6];
                row[0] = rs.getString("id");
                row[1] = rs.getString("package_name");
                row[2] = rs.getString("loan_amount");
                row[3] = rs.getString("minimum_balance");
                row[4] = rs.getString("duration_months");
                row[5] = rs.getString("interest_percentage");
                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows.toArray(new String[0][]);
    }

    /**
     * Get loan option details by package name (SILVER, GOLD, PLATINIUM).
     * Returns: [id, package_name, loan_amount, minimum_balance, duration_months, interest_percentage]
     * Returns null if not found.
     */
    public static String[] getLoanOptionByPackage(String packageName) {
        String sql = "SELECT id, package_name, CONVERT(VARCHAR(20), loan_amount) AS loan_amount, " +
                "CONVERT(VARCHAR(20), minimum_balance) AS minimum_balance, duration_months, " +
                "CONVERT(VARCHAR(10), interest_percentage) AS interest_percentage " +
                "FROM LoanOptions WHERE package_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, packageName.toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String[] row = new String[6];
                    row[0] = rs.getString("id");
                    row[1] = rs.getString("package_name");
                    row[2] = rs.getString("loan_amount");
                    row[3] = rs.getString("minimum_balance");
                    row[4] = rs.getString("duration_months");
                    row[5] = rs.getString("interest_percentage");
                    return row;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

