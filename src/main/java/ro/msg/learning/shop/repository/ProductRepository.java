package ro.msg.learning.shop.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        String query = "select * from product";
        return jdbcTemplate.query(query, new ProductRowMapper());
    }

    @Transactional(readOnly = true)
    public Product findById(int id) {
        String query = "select * from product where id=?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, new ProductRowMapper());
    }

    @Transactional
    public Product insert(final Product product) {
        String query = "insert into product(name,description,price,weight,category,supplier) values(?,?,?,?,?,?)";

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getPrice());
            ps.setDouble(4, product.getWeight());
            ps.setInt(5, product.getCategory());
            ps.setInt(6, product.getSupplier());
            return ps;
        }, holder);

        int newProductId = holder.getKey().intValue();
        product.setId(newProductId);
        return product;
    }

    @Transactional
    public int deleteById(int id) {
        String query = "delete from product where ID =?";
        return jdbcTemplate.update(query, id);
    }

    @Transactional
    public int update(Product product) {
        String query = "update product " +
                " set name = ?, description = ?, price = ?, weight = ?, category = ?, supplier = ?" +
                " where id = ?";
        return jdbcTemplate.update(query,
                product.getName(), product.getDescription(), product.getPrice(), product.getWeight(),
                product.getCategory(), product.getSupplier());
    }
}

class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setWeight(rs.getDouble("weight"));
        product.setCategory(rs.getInt("category"));
        product.setSupplier(rs.getInt("supplier"));
        return product;
    }
}