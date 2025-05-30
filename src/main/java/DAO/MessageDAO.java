package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    public List<Message> getAllMessages() {
        Connection conn = ConnectionUtil.getConnection();

        List<Message> messages = new ArrayList<>();

        try {
            String sql = "sELECT * FROM message";
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message msg = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );

                messages.add(msg);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    public Message getMessageByID(int msg_id) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "sELECT * FROM message WHERE message_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, msg_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message msg = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );

                return msg;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Message> getAllMessagesFromAccount(int account_id) {
        Connection conn = ConnectionUtil.getConnection();

        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message WHERE posted_by=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message msg = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );

                messages.add(msg);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    } 

    public Message createMessage(Message message) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, message.posted_by);
            ps.setString(2, message.message_text);
            ps.setLong(3, message.time_posted_epoch);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if(rs.next()) {
                int msg_id = rs.getInt(1);
                return new Message(msg_id, message.posted_by, message.message_text, message.time_posted_epoch);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message deleteMessage(Message message) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "DELETE FROM message WHERE message_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message.message_id);

            return message;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message updateMessage(Message message) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE message SET message_text=? WHERE message_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, message.message_text);
            ps.setInt(2, message.message_id);

            ps.executeUpdate();

            return message;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
