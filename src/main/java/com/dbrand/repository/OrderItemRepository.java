package com.dbrand.repository;

import com.dbrand.dto.OrderItemResponse;
import com.dbrand.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

@Repository
public class OrderItemRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<OrderItem> orderItemRowMapper = (rs, rowNum) -> {
        OrderItem item = new OrderItem();
        item.setId(UUID.fromString(rs.getString("id")));
        item.setOrderId(UUID.fromString(rs.getString("order_id")));
        item.setShoeId(UUID.fromString(rs.getString("shoe_id")));
        item.setSize(rs.getInt("size"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return item;
    };

    private final RowMapper<OrderItemResponse> orderItemResponseRowMapper = (rs, rowNum) -> {
        OrderItemResponse response = new OrderItemResponse();
        response.setShoeId(rs.getString("shoe_id"));
        response.setShoeName(rs.getString("shoe_name"));
        response.setShoeBrand(rs.getString("shoe_brand"));
        response.setSize(rs.getInt("size"));
        response.setQuantity(rs.getInt("quantity"));
        response.setPrice(rs.getBigDecimal("price"));
        response.setImageUrl(rs.getString("image_url"));
        return response;
    };

    public List<OrderItemResponse> findByOrderId(UUID orderId) {
        String sql = "SELECT oi.shoe_id, s.name as shoe_name, s.brand as shoe_brand, " +
                    "oi.size, oi.quantity, oi.price, s.image_url " +
                    "FROM order_items oi " +
                    "JOIN shoes s ON oi.shoe_id = s.id " +
                    "WHERE oi.order_id = ? ORDER BY oi.created_at";
        return jdbcTemplate.query(sql, orderItemResponseRowMapper, orderId.toString());
    }

    public OrderItem save(OrderItem orderItem) {
        if (orderItem.getId() == null) {
            // Insert new order item
            String sql = "INSERT INTO order_items (id, order_id, shoe_id, size, quantity, price, created_at) " +
                        "VALUES (uuid_generate_v4(), ?, ?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING id";
            
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, orderItem.getOrderId().toString());
                ps.setString(2, orderItem.getShoeId().toString());
                ps.setInt(3, orderItem.getSize());
                ps.setInt(4, orderItem.getQuantity());
                ps.setBigDecimal(5, orderItem.getPrice());
                return ps;
            }, keyHolder);

            // Get the generated ID
            String idSql = "SELECT id FROM order_items WHERE order_id = ? AND shoe_id = ? AND size = ? ORDER BY created_at DESC LIMIT 1";
            String generatedId = jdbcTemplate.queryForObject(idSql, String.class, 
                orderItem.getOrderId().toString(), 
                orderItem.getShoeId().toString(), 
                orderItem.getSize());
            orderItem.setId(UUID.fromString(generatedId));
        }
        return orderItem;
    }

    public void saveAll(List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            save(item);
        }
    }

    public void deleteByOrderId(UUID orderId) {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        jdbcTemplate.update(sql, orderId.toString());
    }
}
