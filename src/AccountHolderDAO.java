import java.io.*;
import java.sql.*;
import java.util.Random;

public class AccountHolderDAO {
    public static String[] loginAndgetAccountHolderdetails(String username, String password){
        String query="SELECT * FROM AccountHolders WHERE username=? AND password=?";
        try(Connection conn= DBConnection.getConnection();
            PreparedStatement stmt=conn.prepareStatement(query);)
        {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs=stmt.executeQuery();

            if(rs.next()){
                return new String[]{
                        rs.getString("id"),
                        rs.getString("account_holder_name"),
                        rs.getString("balance"),
                        rs.getString("age"),
                        rs.getString("gender"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("account_number"),
                        rs.getString("account_type"),
                        rs.getString("address"),
                        rs.getString("contact"),
                        rs.getString("cnic"),
                        rs.getString("email")
                };
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
