package com.dbrand.repository;

import com.dbrand.model.Order;
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
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Order> orderRowMapper = (rs, rowNum) -> {
        Order order = new Order();
        order.setId(UUID.fromString(rs.getString("id")));
        order.setUserId(UUID.fromString(rs.getString("user_id")));
        order.setOrderNumber(rs.getString("order_number"));
        order.setTotalPrice(rs.getBigDecimal("total_price"));
        order.setStatus(rs.getString("status"));
        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        order.setDeliveryDate(rs.getTimestamp("delivery_date") != null ? 
            rs.getTimestamp("delivery_date").toLocalDateTime() : null);
        order.setDeliveryDays(rs.getInt("delivery_days"));
        order.setTrackingNumber(rs.getString("tracking_number"));
        order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        order.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return order;
    };

    public List<Order> findByUserId(UUID userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        return jdbcTemplate.query(sql, orderRowMapper, userId.toString());
    }

    public Optional<Order> findById(UUID id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try {
            Order order = jdbcTemplate.queryForObject(sql, orderRowMapper, id.toString());
            return Optional.ofNullable(order);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Order> findAll() {
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";
        return jdbcTemplate.query(sql, orderRowMapper);
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM orders";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public Order save(Order order) {
        if (order.getId() == null) {
            // Insert new order
            String sql = "INSERT INTO orders (id, user_id, total_price, status, order_date, delivery_date, delivery_days, created_at, updated_at) " +
                        "VALUES (uuid_generate_v4(), ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) RETURNING id";
            
            LocalDateTime deliveryDate = order.getDeliveryDays() != null ? 
                LocalDateTime.now().plusDays(order.getDeliveryDays()) : null;
            
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, order.getUserId().toString());
                ps.setBigDecimal(2, order.getTotalPrice());
                ps.setString(3, order.getStatus());
                ps.setTimestamp(4, deliveryDate != null ? java.sql.Timestamp.valueOf(deliveryDate) : null);
                ps.setInt(5, order.getDeliveryDays());
                return ps;
            }, keyHolder);

            // Get the generated ID
            String idSql = "SELECT id FROM orders WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
            String generatedId = jdbcTemplate.queryForObject(idSql, String.class, order.getUserId().toString());
            order.setId(UUID.fromString(generatedId));
            order.setOrderDate(LocalDateTime.now());
            order.setDeliveryDate(deliveryDate);
        } else {
            // Update existing order
            String sql = "UPDATE orders SET status = ?, delivery_date = ?, delivery_days = ?, " +
                        "updated_at = CURRENT_TIMESTAMP WHERE id = ?";
            
            LocalDateTime deliveryDate = order.getDeliveryDays() != null ? 
                order.getOrderDate().plusDays(order.getDeliveryDays()) : null;
            
            jdbcTemplate.update(sql, order.getStatus(), 
                deliveryDate != null ? java.sql.Timestamp.valueOf(deliveryDate) : null,
                order.getDeliveryDays(), order.getId().toString());
            
            order.setDeliveryDate(deliveryDate);
        }
        return order;
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM orders WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id.toString());
        return count != null && count > 0;
    }
}
