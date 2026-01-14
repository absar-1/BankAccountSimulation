import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoanRequestDAO {

    // add loan request
    public static String addLoanRequest(String accountId, String accountHolderName, String loanType, double loanAmount, String status) {
        String query = "INSERT INTO LoanRequests(account_id, account_holder_name, loan_type, loan_amount, status) " +
                       "VALUES (?,?,?,?,?); SELECT SCOPE_IDENTITY() AS id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmnt = conn.prepareStatement(query)) {
            stmnt.setString(1, accountId);
            stmnt.setString(2, accountHolderName);
            stmnt.setString(3, loanType);
            stmnt.setDouble(4, loanAmount);
            stmnt.setString(5, status);

            // Execute the query and get the generated ID
            try (ResultSet rs = stmnt.executeQuery()) {
                if (rs.next()) {
                    int generatedID = rs.getInt("id");
                    return String.valueOf(generatedID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[][] getPendingRequests() {
        ArrayList<String[]> rows = new ArrayList<>();
        String sql = "SELECT id, account_id, account_holder_name, loan_type, CONVERT(VARCHAR(20), loan_amount) AS loan_amount, status " +
                "FROM LoanRequests WHERE status = 'Pending' ORDER BY id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String[] r = new String[6];
                r[0] = rs.getString("id");
                r[1] = rs.getString("account_id");
                r[2] = rs.getString("account_holder_name");
                r[3] = rs.getString("loan_type");
                r[4] = rs.getString("loan_amount");
                r[5] = rs.getString("status");
                rows.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows.toArray(new String[0][]);
    }

    /**
     * Get all requests (same column shape as pending).
     */
    public static String[][] getAllRequests() {
        ArrayList<String[]> rows = new ArrayList<>();
        String sql = "SELECT id, account_id, account_holder_name, loan_type, CONVERT(VARCHAR(20), loan_amount) AS loan_amount, status " +
                "FROM LoanRequests ORDER BY id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String[] r = new String[6];
                r[0] = rs.getString("id");
                r[1] = rs.getString("account_id");
                r[2] = rs.getString("account_holder_name");
                r[3] = rs.getString("loan_type");
                r[4] = rs.getString("loan_amount");
                r[5] = rs.getString("status");
                rows.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows.toArray(new String[0][]);
    }

    /**
     * Get a single request by id. Returns null if not found.
     */
    public static String[] getRequestById(String id) {
        String sql = "SELECT id, account_id, account_holder_name, loan_type, CONVERT(VARCHAR(20), loan_amount) AS loan_amount, status " +
                "FROM LoanRequests WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String[] r = new String[6];
                    r[0] = rs.getString("id");
                    r[1] = rs.getString("account_id");
                    r[2] = rs.getString("account_holder_name");
                    r[3] = rs.getString("loan_type");
                    r[4] = rs.getString("loan_amount");
                    r[5] = rs.getString("status");
                    return r;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update status of a loan request (e.g., 'Accepted', 'Rejected', 'Pending').
     */
    public static boolean updateStatus(String id, String newStatus) {
        String sql = "UPDATE LoanRequests SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setString(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
