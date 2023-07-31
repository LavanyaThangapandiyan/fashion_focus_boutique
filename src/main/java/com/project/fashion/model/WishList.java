package com.project.fashion.model;

public class WishList {
	private int customerId;
	private int productId;
	private String productName;
	private int price;
	private String size;
	private String category;
	private String status;
	private String image;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName2) {
		this.productName = productName2;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public WishList() {
		super();
	}

	@Override
	public String toString() {
		return "WishList [customerId=" + customerId + ", productId=" + productId + ", productName=" + productName
				+ ", price=" + price + ", size=" + size + ", category=" + category + ", status=" + status + ", image="
				+ image + "]";
	}

}
