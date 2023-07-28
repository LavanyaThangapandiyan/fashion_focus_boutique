package com.project.fashion.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.project.fashion.model.Sales;

public class SalesMapper implements RowMapper<Sales> {

	@Override
	public Sales mapRow(ResultSet rs, int rowNum) throws SQLException {
		Sales sale=new Sales();
		int id=rs.getInt("id");
		long salesAmount=rs.getLong("sales_amount");
		Date salesDate=rs.getDate("Date");
		sale.setId(id);
		sale.setSalesAmount(salesAmount);
		sale.setDate(salesDate);
		return sale;
	}

}
