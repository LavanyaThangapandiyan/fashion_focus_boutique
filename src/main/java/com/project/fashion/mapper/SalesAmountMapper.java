package com.project.fashion.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.project.fashion.model.Sales;

public class SalesAmountMapper implements RowMapper<Sales> {

	@Override
	public Sales mapRow(ResultSet rs, int rowNum) throws SQLException {
		Sales sale = new Sales();
		int salesAmount = rs.getInt("SUM(counts)");
		sale.setSalesAmount(salesAmount);
		return sale;
	}

}
