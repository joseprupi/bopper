package com.backofficer.payment.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.backofficer.payment.Payment;
import com.backofficer.payment.PaymentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaymentMysqlDAO implements PaymentDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Payment> getAllPayments(){

        String sql = "SELECT * FROM payments";

        List<Payment> paymentList =  new ArrayList();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        for (Map row : rows) {
            Payment payment = new Payment();
            payment.setId((Integer)(row.get("id")));
            payment.setData((String) row.get("data"));
            payment.setApproved((Boolean) row.get("approved"));
            paymentList.add(payment);
        }
        return paymentList;

    }

    public Payment getPayment(int id){

        String sql = "SELECT * FROM payments WHERE ID = ?";

        Connection conn = null;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            Payment customer = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                customer = new Payment(
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

    public void updatePayment(Payment student){

        String sql = "insert into payments(data, approved) values  (?, ?)";
        jdbcTemplate.update(sql, new Object[] { 1000,0});

    }

    public void deletePayment(Payment student){

    }
}
