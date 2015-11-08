package com.backofficer.message.impl;

import com.backofficer.message.Message;
import com.backofficer.message.MessageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageMysqlDAO implements MessageDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Message> getAllMessages(){

        String sql = "SELECT * FROM payments";

        List<Message> messageList =  new ArrayList();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        for (Map row : rows) {
            Message message = new Message();
            message.setId((Integer)(row.get("id")));
            message.setData((String) row.get("data"));
            message.setApproved((Boolean) row.get("approved"));
            messageList.add(message);
        }
        return messageList;

    }

    public Message getMessage(int id){

        String sql = "SELECT * FROM payments WHERE ID = ?";

        Connection conn = null;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            Message customer = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                customer = new Message(
                        rs.getInt("id"),
                        rs.getString("data"),
                        rs.getBoolean("approved")
                );
            }
            rs.close();
            ps.close();
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }

    }

    public void updateMessage(){

        String sql = "insert into payments(data, approved) values  (?, ?)";
        jdbcTemplate.update(sql, new Object[] { 1000,0});

    }

    public void deleteMessage(Message student){

    }
}
