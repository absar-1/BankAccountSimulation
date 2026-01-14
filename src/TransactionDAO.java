// account_id in the Transactions table is the foreign key to AccountHolders table.

import java.sql.*;
import java.util.ArrayList;

public class TransactionDAO {

    public static String insertTransactionRecord(Connection conn, String accountId, String description, double amount, String transactionType) throws SQLException {
        // Step 1: Insert row with a temporary transaction_id, then update it based on the generated id

        // Insert with temporary placeholder transaction_id
        String insertSql = "INSERT INTO Transactions (account_id, transaction_id, description, amount, transaction_type, transaction_date, transaction_time) " +
                "VALUES (?, ?, ?, ?, ?, CAST(GETDATE() AS DATE), CAST(GETDATE() AS TIME)); SELECT SCOPE_IDENTITY() AS id";

        int generatedId;
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setString(1, accountId);
            ps.setString(2, "TEMP");
            ps.setString(3, description);
            ps.setDouble(4, amount);
            ps.setString(5, transactionType);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    generatedId = rs.getInt("id");
                } else {
                    throw new SQLException("Failed to get generated id for transaction.");
                }
            }
        }

        // Step 2: Update with properly formatted transaction_id based on the generated id
        String transactionId = String.format("TID-%05d", generatedId);
        String updateSql = "UPDATE Transactions SET transaction_id = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setString(1, transactionId);
            ps.setInt(2, generatedId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update transaction_id.");
            }
        }

        return transactionId;
    }

    // Return transaction rows as String[][] with columns:
    // account_id, transaction_id, description, amount, transaction_type, transaction_date, transaction_time
    public static String[][] getTransactionsByAccountId(String accountId) {
        ArrayList<String[]> result = new ArrayList<>();
        String sql = "SELECT account_id, COALESCE(transaction_id, ''), COALESCE(description, ''), amount, COALESCE(transaction_type, ''), " +
                "CONVERT(VARCHAR(10), transaction_date, 103) AS tdate, CONVERT(VARCHAR(8), transaction_time, 108) AS ttime " +
                "FROM Transactions WHERE account_id = ? ORDER BY transaction_date DESC, transaction_time DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] row = new String[7];
                    row[0] = rs.getString(1);
                    row[1] = rs.getString(2);
                    row[2] = rs.getString(3);
                    row[3] = String.valueOf(rs.getDouble(4));
                    row[4] = rs.getString(5);
                    row[5] = rs.getString(6);
                    row[6] = rs.getString(7);
                    result.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.toArray(new String[0][]);
    }

    // deposit: atomic update of balance + insert transaction (in same DB transaction)
    public static boolean deposit(String accountNumber, double amount, boolean isAdmin) throws SQLException {
        String selectAccountSql = "SELECT id FROM AccountHolders WHERE account_number = ?";
        String updateBalanceSql = "UPDATE AccountHolders SET balance = balance + ? WHERE account_number = ?";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String accountId;
            try (PreparedStatement ps = conn.prepareStatement(selectAccountSql)) {
                ps.setString(1, accountNumber);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    accountId = rs.getString("id");
                }
            }

            try (PreparedStatement psUpd = conn.prepareStatement(updateBalanceSql)) {
                psUpd.setDouble(1, amount);
                psUpd.setString(2, accountNumber);
                int rows = psUpd.executeUpdate();
                if (rows == 0) {
                    conn.rollback();
                    return false;
                }
            }

            String desc = isAdmin ? "Money Deposited into Account by Admin" : "Money Deposited into Account";
            insertTransactionRecord(conn, accountId, desc, amount, "Deposit");

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw e;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // withdraw: atomic update with balance check + insert transaction
    public static boolean withdraw(String accountNumber, double amount) throws SQLException {
        String selectAccountSql = "SELECT id FROM AccountHolders WHERE account_number = ?";
        String withdrawSql = "UPDATE AccountHolders SET balance = balance - ? WHERE account_number = ? AND balance >= ?";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String accountId;
            try (PreparedStatement ps = conn.prepareStatement(selectAccountSql)) {
                ps.setString(1, accountNumber);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) { conn.rollback(); return false; }
                    accountId = rs.getString("id");
                }
            }

            try (PreparedStatement psW = conn.prepareStatement(withdrawSql)) {
                psW.setDouble(1, amount);
                psW.setString(2, accountNumber);
                psW.setDouble(3, amount);
                int rows = psW.executeUpdate();
                if (rows == 0) {
                    conn.rollback();
                    return false;
                }
            }

            insertTransactionRecord(conn, accountId, "Money Withdrawn from Account", amount, "Withdrawal");
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw e;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // store receive: insert transaction for receiver (no balance change here — receiver already credited)
    public static boolean storeNewReceive(String receiverId, String senderName, double amount) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            insertTransactionRecord(conn, receiverId, "Money Received from " + senderName, amount, "Transfer Received");
            return true;
        } catch (SQLException e) {
            throw e;
        }
    }

    // transfer: internal if receiver in AccountHolders, else check OtherBankAccountHolders
    public static boolean transfer(String senderAccNum, String receiverAccNum, String bankName, double amount, String receiverName) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // get sender id
            String getAccountSql = "SELECT id FROM AccountHolders WHERE account_number = ?";
            String senderId;
            try (PreparedStatement ps = conn.prepareStatement(getAccountSql)) {
                ps.setString(1, senderAccNum);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) { conn.rollback(); return false; }
                    senderId = rs.getString("id");
                }
            }

            if (bankName.equalsIgnoreCase("Scam Bank Limited") || bankName.equalsIgnoreCase("Habib Bank Limited") /* or other internal bank names */) {
                // internal transfer: ensure receiver exists in our table
                String recvId;
                try (PreparedStatement ps = conn.prepareStatement(getAccountSql)) {
                    ps.setString(1, receiverAccNum);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) { conn.rollback(); return false; }
                        recvId = rs.getString("id");
                    }
                }

                // debit sender atomically
                String debitSql = "UPDATE AccountHolders SET balance = balance - ? WHERE account_number = ? AND balance >= ?";
                try (PreparedStatement ps = conn.prepareStatement(debitSql)) {
                    ps.setDouble(1, amount);
                    ps.setString(2, senderAccNum);
                    ps.setDouble(3, amount);
                    if (ps.executeUpdate() == 0) { conn.rollback(); return false; } // insufficient funds
                }

                // credit receiver
                String creditSql = "UPDATE AccountHolders SET balance = balance + ? WHERE account_number = ?";
                try (PreparedStatement ps = conn.prepareStatement(creditSql)) {
                    ps.setDouble(1, amount);
                    ps.setString(2, receiverAccNum);
                    if (ps.executeUpdate() == 0) { conn.rollback(); return false; }
                }

                // insert transaction rows for sender and receiver
                insertTransactionRecord(conn, senderId, "Money Transferred to " + receiverName, amount, "Transfer");
                insertTransactionRecord(conn, recvId, "Money Received from " + AccountHolderDAO.getNameByAccountNumber(senderAccNum), amount, "Transfer Received");

            } else {
                // external transfer: verify external account exists in OtherBankAccountHolders
                String extCheck = "SELECT 1 FROM OtherBankAccountHolders WHERE bank_name = ? AND account_number = ?";
                try (PreparedStatement ps = conn.prepareStatement(extCheck)) {
                    ps.setString(1, bankName);
                    ps.setString(2, receiverAccNum);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) { conn.rollback(); return false; } // external account not found
                    }
                }
                // debit sender
                String debitSql = "UPDATE AccountHolders SET balance = balance - ? WHERE account_number = ? AND balance >= ?";
                try (PreparedStatement ps = conn.prepareStatement(debitSql)) {
                    ps.setDouble(1, amount);
                    ps.setString(2, senderAccNum);
                    ps.setDouble(3, amount);
                    if (ps.executeUpdate() == 0) { conn.rollback(); return false; } // insufficient funds
                }

                // insert transaction for sender only
                insertTransactionRecord(conn, senderId, "Money Transferred to " + receiverName, amount, "Transfer");
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw e;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // bill payment - atomic update with balance check + insert transaction (single transaction record)
    public static boolean billPayment(String accountNumber, double amount, String billType) throws SQLException {
        String selectAccountSql = "SELECT id FROM AccountHolders WHERE account_number = ?";
        String withdrawSql = "UPDATE AccountHolders SET balance = balance - ? WHERE account_number = ? AND balance >= ?";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String accountId;
            try (PreparedStatement ps = conn.prepareStatement(selectAccountSql)) {
                ps.setString(1, accountNumber);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) { conn.rollback(); return false; }
                    accountId = rs.getString("id");
                }
            }

            try (PreparedStatement psW = conn.prepareStatement(withdrawSql)) {
                psW.setDouble(1, amount);
                psW.setString(2, accountNumber);
                psW.setDouble(3, amount);
                int rows = psW.executeUpdate();
                if (rows == 0) {
                    conn.rollback();
                    return false; // insufficient funds
                }
            }

            // Insert only ONE transaction record for bill payment
            insertTransactionRecord(conn, accountId, billType + " paid", amount, "Bill Payment");
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw e;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // issue chequebook - deduct fee and insert transaction (single transaction record)
    public static boolean issueChequebook(String accountNumber) throws SQLException {
        final double chequeBookAmount = 500.0;
        String selectAccountSql = "SELECT id FROM AccountHolders WHERE account_number = ?";
        String withdrawSql = "UPDATE AccountHolders SET balance = balance - ? WHERE account_number = ? AND balance >= ?";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String accountId;
            try (PreparedStatement ps = conn.prepareStatement(selectAccountSql)) {
                ps.setString(1, accountNumber);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) { conn.rollback(); return false; }
                    accountId = rs.getString("id");
                }
            }

            try (PreparedStatement psW = conn.prepareStatement(withdrawSql)) {
                psW.setDouble(1, chequeBookAmount);
                psW.setString(2, accountNumber);
                psW.setDouble(3, chequeBookAmount);
                int rows = psW.executeUpdate();
                if (rows == 0) {
                    conn.rollback();
                    return false; // insufficient funds
                }
            }

            // Insert only ONE transaction record for chequebook
            insertTransactionRecord(conn, accountId, "Cheque Book Issued", chequeBookAmount, "Cheque Book");
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw e;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

}