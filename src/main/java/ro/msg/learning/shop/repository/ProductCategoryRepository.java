package ro.msg.learning.shop.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.msg.learning.shop.model.ProductCategory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class ProductCategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductCategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public List<ProductCategory> findAll() {
        String query = "select * from product_category";
        return jdbcTemplate.query(query, new ProductCategoryRowMapper());
    }

    @Transactional(readOnly = true)
    public ProductCategory findById(int id) {
        String query = "select * from product_category where id=?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, new ProductCategoryRowMapper());
    }

    @Transactional(readOnly = true)
    public ProductCategory insert(final ProductCategory productCategory) {
        String query = "insert into product(name,description) values(?,?)";

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, productCategory.getName());
            ps.setString(2, productCategory.getDescription());
            return ps;
        }, holder);

        int newProductCategoryId = holder.getKey().intValue();
        productCategory.setId(newProductCategoryId);
        return productCategory;
    }

    @Transactional(readOnly = true)
    public int deleteById(int id) {
        String query = "delete from product_category where ID =?";
        return jdbcTemplate.update(query, id);
    }

    @Transactional(readOnly = true)
    public int update(ProductCategory productCategory) {
        String query = "update product_category " +
                " set name = ?, description = ?" +
                " where id = ?";
        return jdbcTemplate.update(query, productCategory.getName(), productCategory.getDescription());
    }
}

class ProductCategoryRowMapper implements RowMapper<ProductCategory> {
    @Override
    public ProductCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(rs.getInt("id"));
        productCategory.setName(rs.getString("name"));
        productCategory.setDescription(rs.getString("description"));
        return productCategory;
    }
}
