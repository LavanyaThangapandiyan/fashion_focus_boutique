package com.project.fashion.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.project.fashion.model.Product;

public class SingleProductMapper implements RowMapper<Product> {

	@Override
	public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
		Product product = new Product();
		int id = rs.getInt("id");
		String name = rs.getString("name");
		int price = rs.getInt("price");
		String type = rs.getString("category");
		String size = rs.getString("size");
		int quantity = rs.getInt("quantity");
		String fabric = rs.getString("fabric");
		String gender = rs.getString("gender");
		product.setId(id);
		product.setName(name);
		product.setPrice(price);
		product.setType(type);
		product.setSize(size);
		product.setQuantity(quantity);
		product.setFabric(fabric);
		product.setGender(gender);
		return product;
	}

}
