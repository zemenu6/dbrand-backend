package com.dbrand.repository;

import com.dbrand.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PaymentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Payment> paymentRowMapper = (rs, rowNum) -> {
        Payment payment = new Payment();
        payment.setId(UUID.fromString(rs.getString("id")));
        payment.setOrderId(UUID.fromString(rs.getString("order_id")));
        payment.setStatus(rs.getString("status"));
        payment.setTransactionReference(rs.getString("payment_reference"));
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setPaymentDate(rs.getTimestamp("payment_date") != null ? 
            rs.getTimestamp("payment_date").toLocalDateTime() : null);
        payment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        payment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return payment;
    };

    public Optional<Payment> findByOrderId(UUID orderId) {
        String sql = "SELECT * FROM payments WHERE order_id = ?";
        try {
            Payment payment = jdbcTemplate.queryForObject(sql, paymentRowMapper, orderId.toString());
            return Optional.ofNullable(payment);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Payment save(Payment payment) {
        if (payment.getId() == null) {
            // Insert new payment
            String sql = "INSERT INTO payments (id, order_id, status, payment_reference, amount, payment_date, created_at, updated_at) " +
                        "VALUES (uuid_generate_v4(), ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) RETURNING id";
            
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, payment.getOrderId().toString());
                ps.setString(2, payment.getStatus());
                ps.setString(3, payment.getTransactionReference());
                ps.setBigDecimal(4, payment.getAmount());
                ps.setTimestamp(5, payment.getPaymentDate() != null ? 
                    java.sql.Timestamp.valueOf(payment.getPaymentDate()) : null);
                return ps;
            }, keyHolder);

            // Get the generated ID
            String idSql = "SELECT id FROM payments WHERE order_id = ? ORDER BY created_at DESC LIMIT 1";
            String generatedId = jdbcTemplate.queryForObject(idSql, String.class, payment.getOrderId().toString());
            payment.setId(UUID.fromString(generatedId));
        } else {
            // Update existing payment
            String sql = "UPDATE payments SET status = ?, payment_reference = ?, payment_date = ?, " +
                        "updated_at = CURRENT_TIMESTAMP WHERE id = ?";
            jdbcTemplate.update(sql, payment.getStatus(), payment.getTransactionReference(),
                payment.getPaymentDate() != null ? java.sql.Timestamp.valueOf(payment.getPaymentDate()) : null,
                payment.getId().toString());
        }
        return payment;
    }

    public List<Payment> findAll() {
        String sql = "SELECT * FROM payments ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, paymentRowMapper);
    }

    public BigDecimal getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE status = 'PAID'";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class);
    }

    public Optional<Payment> findById(String id) {
        String sql = "SELECT * FROM payments WHERE id = ?";
        try {
            Payment payment = jdbcTemplate.queryForObject(sql, paymentRowMapper, id);
            return Optional.ofNullable(payment);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
