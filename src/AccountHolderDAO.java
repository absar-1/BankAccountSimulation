import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class AccountHolderDAO {
    public static String[] loginAndgetAccountDetails(String username, String password) {
        String query = "SELECT * FROM Accounts WHERE username=? AND password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new String[]{
                        rs.getString("id"),
                        rs.getString("balance"),
                        rs.getString("gender"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("account_number"),
                        rs.getString("account_type"),
                        rs.getString("title")
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // using it for transactions to get sender name by account number
    public static String getTitleByAccountNumber(String senderAccNum) {
        String query = "SELECT account_holder_name FROM AccountHolders WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, senderAccNum);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("account_holder_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get account holder details by account number (for viewing statements, etc.)
    public static String[] getAccountDetailsByAccountNumber(String accountNumber) {
        String query = "SELECT * FROM Accounts WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new String[]{
                        rs.getString("id"),
                        rs.getString("balance"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("account_number"),
                        rs.getString("account_type"),
                        rs.getString("title"),
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getAccountDetailsByID(String accountID) {
        String query = "SELECT * FROM Accounts WHERE account_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, accountID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new String[]{
                        rs.getString("id"),
                        rs.getString("balance"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("account_number"),
                        rs.getString("account_type"),
                        rs.getString("title"),
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addBeneficiary(String accHolderID, String beneficiaryAccNum, String beneficiaryName, String beneficiaryBankName) throws IOException {
        if (beneficiaryBankName.equalsIgnoreCase("Scam Bank Limited")) {
            String checkBenificiaryQuery = "SELECT * FROM Accounts WHERE account_number = ?";
            try (
                    Connection conn = DBConnection.getConnection();
                    PreparedStatement checkStmt = conn.prepareStatement(checkBenificiaryQuery);
            ) {
                checkStmt.setString(1, beneficiaryAccNum);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        String checkQuery = "SELECT * FROM Beneficiaries WHERE account_id = ? AND beneficiary_account_number = ?";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
        ) {
            checkStmt.setString(1, accHolderID);
            checkStmt.setString(2, beneficiaryAccNum);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return false;
            }
            String insertQuery = "INSERT INTO Beneficiaries VALUES (?,?,?,?)";
            try (
                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            ) {
                insertStmt.setString(1, accHolderID);
                insertStmt.setString(2, beneficiaryAccNum);
                insertStmt.setString(3, beneficiaryName);
                insertStmt.setString(4, beneficiaryBankName);
                int rowsAffected = insertStmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<String> getBeneficiaries(String accID) throws IOException {
        ArrayList<String> beneficiaries = new ArrayList<>();
        String query = "SELECT * FROM Beneficiaries WHERE account_id = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, accID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String beneficiaryDetails = rs.getString("beneficiary_account_number") + "/" +
                        rs.getString("beneficiary_name") + "/" +
                        rs.getString("beneficiary_bank_name");
                beneficiaries.add(beneficiaryDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return beneficiaries;
    }

    public static String getAccountType(String id) throws IOException {
        String query = "SELECT account_type FROM Accounts WHERE id = ?";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("account_type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkAccount(String accountNumber) throws IOException {
        String query = "SELECT * FROM Accounts WHERE account_number = ?";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updatePassword(String accountNumber, String newPassword) throws IOException {
        String query = "UPDATE Accounts SET password = ? WHERE account_number = ?";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setString(1, newPassword);
            stmt.setString(2, accountNumber);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



}
