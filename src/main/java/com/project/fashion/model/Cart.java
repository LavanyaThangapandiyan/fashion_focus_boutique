package com.project.fashion.model;

public class Cart {
	private String image;
	private int id;
	private int orderId;
	private int customerId;
	private int productId;
	private String productName;
	private int price;
	private String size;
	private String productType;
	private String fabric;
	private String gender;
	private int quantity;
	private int amount;
	private String status;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
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

	public void setProductName(String productName) {
		this.productName = productName;
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

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getFabric() {
		return fabric;
	}

	public void setFabric(String fabric) {
		this.fabric = fabric;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Cart() {
		super();
	}

	@Override
	public String toString() {
		return "Cart [image=" + image + ", id=" + id + ", orderId=" + orderId + ", customerId=" + customerId
				+ ", productId=" + productId + ", productName=" + productName + ", price=" + price + ", size=" + size
				+ ", productType=" + productType + ", fabric=" + fabric + ", gender=" + gender + ", quantity="
				+ quantity + ", amount=" + amount + ", status=" + status + "]";
	}
}