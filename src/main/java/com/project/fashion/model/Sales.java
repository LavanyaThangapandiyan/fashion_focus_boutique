package com.project.fashion.model;

import java.sql.Date;

public class Sales {
	
	private int id;
	private long salesAmount;
	private Date date;
	public Sales() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getSalesAmount() {
		return salesAmount;
	}
	public void setSalesAmount(long salesAmount) {
		this.salesAmount = salesAmount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "Sales [id=" + id + ", salesAmount=" + salesAmount + ", date=" + date + "]";
	}
	public Sales(int id, long salesAmount, Date date) {
		super();
		this.id = id;
		this.salesAmount = salesAmount;
		this.date = date;
	}
	
	
}
