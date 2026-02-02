package com.dbrand.repository;

import com.dbrand.dto.ShoeResponse;
import com.dbrand.dto.ShoeSizeResponse;
import com.dbrand.model.Shoe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ShoeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Shoe> shoeRowMapper = (rs, rowNum) -> {
        Shoe shoe = new Shoe();
        shoe.setId(UUID.fromString(rs.getString("id")));
        shoe.setName(rs.getString("name"));
        shoe.setBrand(rs.getString("brand"));
        shoe.setDescription(rs.getString("description"));
        shoe.setPrice(rs.getBigDecimal("price"));
        shoe.setActive(rs.getBoolean("is_active"));
        shoe.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        shoe.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return shoe;
    };

    private final RowMapper<ShoeResponse> shoeResponseRowMapper = (rs, rowNum) -> {
        ShoeResponse response = new ShoeResponse();
        response.setId(rs.getString("id"));
        response.setName(rs.getString("name"));
        response.setBrand(rs.getString("brand"));
        response.setDescription(rs.getString("description"));
        response.setPrice(rs.getBigDecimal("price"));
        response.setImageUrl(rs.getString("primary_image_url"));
        return response;
    };

    private final RowMapper<ShoeSizeResponse> shoeSizeResponseRowMapper = (rs, rowNum) -> {
        return new ShoeSizeResponse(rs.getInt("size"), rs.getInt("stock_count"));
    };

    public List<ShoeResponse> findAll(String brand, Integer minSize, Integer maxSize, BigDecimal minPrice, BigDecimal maxPrice) {
        StringBuilder sql = new StringBuilder(
            "SELECT DISTINCT s.id, s.name, s.brand, s.description, s.price, s.primary_image_url " +
            "FROM active_shoes s " +
            "LEFT JOIN shoe_sizes ss ON s.id = ss.shoe_id " +
            "WHERE 1=1"
        );

        if (brand != null && !brand.isEmpty()) {
            sql.append(" AND LOWER(s.brand) = LOWER(?)");
        }
        if (minSize != null) {
            sql.append(" AND ss.size >= ?");
        }
        if (maxSize != null) {
            sql.append(" AND ss.size <= ?");
        }
        if (minPrice != null) {
            sql.append(" AND s.price >= ?");
        }
        if (maxPrice != null) {
            sql.append(" AND s.price <= ?");
        }
        sql.append(" ORDER BY s.name");

        Object[] params = buildFilterParams(brand, minSize, maxSize, minPrice, maxPrice);
        return jdbcTemplate.query(sql.toString(), shoeResponseRowMapper, params);
    }

    public List<Shoe> findAllAdmin() {
        String sql = "SELECT id, name, brand, description, price, is_active, created_at, updated_at FROM shoes WHERE is_deleted = false ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, shoeRowMapper);
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM shoes WHERE is_deleted = false";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public Optional<Shoe> findByIdAdmin(UUID id) {
        String sql = "SELECT id, name, brand, description, price, is_active, created_at, updated_at FROM shoes WHERE id = ? AND is_deleted = false";
        try {
            Shoe shoe = jdbcTemplate.queryForObject(sql, shoeRowMapper, id.toString());
            return Optional.ofNullable(shoe);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Object[] buildFilterParams(String brand, Integer minSize, Integer maxSize, BigDecimal minPrice, BigDecimal maxPrice) {
        List<Object> params = new java.util.ArrayList<>();
        if (brand != null && !brand.isEmpty()) params.add(brand);
        if (minSize != null) params.add(minSize);
        if (maxSize != null) params.add(maxSize);
        if (minPrice != null) params.add(minPrice);
        if (maxPrice != null) params.add(maxPrice);
        return params.toArray();
    }

    public Optional<ShoeResponse> findById(UUID id) {
        String sql = "SELECT id, name, brand, description, price, primary_image_url FROM active_shoes WHERE id = ?";
        try {
            ShoeResponse shoe = jdbcTemplate.queryForObject(sql, shoeResponseRowMapper, id.toString());
            return Optional.ofNullable(shoe);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<ShoeSizeResponse> findSizesByShoeId(UUID shoeId) {
        String sql = "SELECT size, stock_count FROM available_sizes WHERE shoe_id = ? ORDER BY size";
        return jdbcTemplate.query(sql, shoeSizeResponseRowMapper, shoeId.toString());
    }

    public Shoe save(Shoe shoe) {
        if (shoe.getId() == null) {
            // Insert new shoe
            String sql = "INSERT INTO shoes (id, name, brand, description, price, is_active, created_at, updated_at) " +
                        "VALUES (uuid_generate_v4(), ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) RETURNING id";
            
            String generatedId = jdbcTemplate.queryForObject(sql, String.class, 
                shoe.getName(), shoe.getBrand(), shoe.getDescription(), 
                shoe.getPrice(), shoe.isActive());
            shoe.setId(UUID.fromString(generatedId));
        } else {
            // Update existing shoe
            String sql = "UPDATE shoes SET name = ?, brand = ?, description = ?, price = ?, is_active = ?, " +
                        "updated_at = CURRENT_TIMESTAMP WHERE id = ?";
            jdbcTemplate.update(sql, shoe.getName(), shoe.getBrand(), shoe.getDescription(), 
                               shoe.getPrice(), shoe.isActive(), shoe.getId().toString());
        }
        return shoe;
    }

    public void deleteById(UUID id) {
        String sql = "DELETE FROM shoes WHERE id = ?";
        jdbcTemplate.update(sql, id.toString());
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM shoes WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id.toString());
        return count != null && count > 0;
    }

    public void updateShoeSize(UUID shoeId, Integer size, Integer stockCount) {
        String sql = "INSERT INTO shoe_sizes (id, shoe_id, size, stock_count, created_at) " +
                    "VALUES (uuid_generate_v4(), ?, ?, ?, CURRENT_TIMESTAMP) " +
                    "ON CONFLICT (shoe_id, size) DO UPDATE SET stock_count = EXCLUDED.stock_count";
        jdbcTemplate.update(sql, shoeId.toString(), size, stockCount);
    }

    public void deleteShoeSize(UUID shoeId, Integer size) {
        String sql = "DELETE FROM shoe_sizes WHERE shoe_id = ? AND size = ?";
        jdbcTemplate.update(sql, shoeId.toString(), size);
    }
}
