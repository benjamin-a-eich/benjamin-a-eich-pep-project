package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {
    // Register a user account

    public Account registerAccount(Account account) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";

            // Okay, so we have to use RETURN_GENERATED_KEYS here to get the account id back
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, account.username);
            ps.setString(2, account.password);

            // Run the query
            ps.executeUpdate();

            // Get the list of generated keys
            ResultSet rs = ps.getGeneratedKeys();

            if(rs.next()) {
                int generated_account_id = rs.getInt(1);
                return new Account(generated_account_id, account.username, account.password);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
    
    // Log a user into their account. Validate the password and the username are in the DB.
    public Account validateAccount(Account account) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * WHERE username=? AND password=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, account.username);
            ps.setString(2, account.password);

            ResultSet rs = ps.executeQuery();

            // Check the results set
            if(rs.next()) {
                // Return true if the user exists
                Account validAccount = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                    );
                return validAccount;
            } 
        } catch (SQLException e) {
            System.out.println(e);
        }

        return null;
    }

    // Method to check if a usedrname for an account is already taken.
    // This is really similar to the login function, but is very important that it is it's own function
    // As here, we only care if the username matches, we don't care about the password.
    public boolean queryUsername (Account account) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * WHERE username=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, account.username);

            ResultSet rs = ps.executeQuery();

            // Check the results set
            if(rs.next()) {
                // Return true if the user exists
                return true;
            } else {
                // Return false if the user does not exist. ie the results set has no next
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        
        return false;
    }

    public boolean queryAccountID(int act_id) {

        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * WHERE account_id=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, act_id);

            ResultSet rs = ps.executeQuery();

            // Check the results set
            if(rs.next()) {
                // Return true if an account with that account id exists
                return true;
            } else {
                // Return false if the account does not exist. ie the results set has no next
                return false;
            }
            
        } catch (SQLException e) {
            System.out.println(e);
        }

        return false;
    }
    
}
