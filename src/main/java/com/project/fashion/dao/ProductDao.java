package com.project.fashion.dao;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.project.fashion.exception.ExistCategoryException;
import com.project.fashion.exception.ExistProductException;
import com.project.fashion.interfaces.ProductInterface;
import com.project.fashion.mapper.CategoryMapper;
import com.project.fashion.mapper.CategoryMapperSingle;
import com.project.fashion.mapper.CategoryNameMapper;
import com.project.fashion.mapper.ProductMapperAll;
import com.project.fashion.mapper.SalesAmountMapper;
import com.project.fashion.mapper.SingleProductMapper;
import com.project.fashion.model.Category;
import com.project.fashion.model.Product;
import com.project.fashion.model.Sales;
import com.project.fashion.util.ConnectionUtil;
import com.project.fashion.validation.Validation;

@Repository
public class ProductDao implements ProductInterface {

	Logger logger = LoggerFactory.getLogger(ProductDao.class);
	Validation valid = new Validation();
	JdbcTemplate jdbcTemplate = ConnectionUtil.getJdbcTemplate();
	Product product = new Product();
	String productAvailability = "Available";

	// ----Insert Product Details
	public int saveProductDetails(Product product) throws ExistProductException {
		List<Product> productList = allProductList();
		String getProduct = productList.toString();
		String productName = product.getName();
		boolean contains = getProduct.contains(productName);
		if (contains) {
			throw new ExistProductException("Product Name Already Exists.");

		} else {
			String insert = "insert into product(name,price,category,size,quantity,fabric,gender,image,is_available)values(?,?,?,?,?,?,?,?,?)";
			String input = product.getName();
			String productName1 = input.substring(0, 1).toUpperCase() + input.substring(1);
			String inputFabric = product.getFabric();
			String fabricName = inputFabric.substring(0, 1).toUpperCase() + inputFabric.substring(1);
			boolean name = valid.nameValidation(productName1);
			boolean size = valid.nameValidation(product.getSize());
			boolean fabric = valid.nameValidation(product.getFabric());
			if (name && size && fabric) {
				Object[] details = { productName1, product.getPrice(), product.getType(), product.getSize(),
						product.getQuantity(), fabricName, product.getGender(), product.getImage(),
						productAvailability };
				int numberOfRows = jdbcTemplate.update(insert, details);
				logger.info("Inserted Rows : " + numberOfRows);
				return 1;
			} else
				logger.error("Invalid Product Details");
		}
		return 0;
	}

	// ----Update Product Details-----
	public void updateProductDetails(int id, String name, int price, String size, int quantity, String fabric,
			String gender) {
		String update = "update product set name=?,price=?,size=?,quantity=?,fabric=?,gender=? where id=?";
		String input = name;
		String productName = input.substring(0, 1).toUpperCase() + input.substring(1);
		Object[] details = { productName, price, size, quantity, fabric, gender, id };
		int numberOfRows = jdbcTemplate.update(update, details);
		logger.info("Updated rows : " + numberOfRows);
	}

	// Get Product List
	public List<Product> allProductList() 
	{
		List<Product> productList;
		String find = "select id,name,price,category,size,quantity,fabric,gender,image from product where is_available='Available'";
		productList = jdbcTemplate.query(find, new ProductMapperAll());
		return productList;
	}

	// Get Inactive Product List
	public List<Product> inActiveProductList() 
	{
		List<Product> productList;
		String find = "select id,name,price,category,size,quantity,fabric,gender,image from product where is_available='Not Available'";
		productList = jdbcTemplate.query(find, new ProductMapperAll());
		return productList;
	}

	// --Get Product Details Using Product ID---
	public Product getProductById(int productId) {
		Product getDetails;
		String find = "select id,name,price,category,size,quantity,fabric,gender from product where id=?";
		getDetails = jdbcTemplate.queryForObject(find, new SingleProductMapper(), productId);
		return getDetails;
	}

	// --Delete Product---
	public int deleteProduct(int id) {
		String delete = "update product set is_available='Not Available' where id=?";
		Object[] details = { id };
		int update = jdbcTemplate.update(delete, details);
		logger.info("Delete Product : " + update);
		return update;
	}

	// ---Active Product By Id---
	public int activeProduct(int id) {
		String active = "update product set is_available='Available' where id=?";
		Object[] details = { id };
		int update = jdbcTemplate.update(active, details);
		logger.info("Active Product : " + update);
		return update;
	}

	// ------save category details----
	public void saveCategoryDetails(Category category) throws ExistCategoryException {
		List<Category> categoryList = categoryList();
		String getCategory = categoryList.toString();
		String getName = category.getCategoryName();
		boolean name = valid.nameValidation(getName);
		boolean contains = getCategory.contains(getName);
		logger.info("Category contains " + contains);
		if (contains) {
			throw new ExistCategoryException("Category Already Exist");
		} else if (name) {
			String input = category.getCategoryName();
			String categoryName = input.substring(0, 1).toUpperCase() + input.substring(1);
			String save = "insert into category(category_name,is_available)values(?,?)";
			Object[] details = { categoryName, productAvailability };
			int numberOfRows = jdbcTemplate.update(save, details);
			logger.info("Inserted Rows : " + numberOfRows);
		} else
			logger.error("Invalid Category Details ");
	}

	// ----Get Active Category List---------
	public List<Category> categoryList() {
		List<Category> listCategory;
		String categoryList = "select id,category_name,is_available from category where is_available='Available'";
		listCategory = jdbcTemplate.query(categoryList, new CategoryMapper());
		return listCategory;
	}

	// ----Get In Active Category List---------
	public List<Category> inActiveCategoryList() {
		List<Category> listCategory;
		String categoryList = "select id,category_name,is_available from category where is_available='Not Available'";
		listCategory = jdbcTemplate.query(categoryList, new CategoryMapper());
		return listCategory;
	}
   
	//--- Get available category Names---
	public List<Category> getCategoryName() {
		List<Category> getCategoryNameList;
		String getCategoryName = "select category_name from category where is_available='Available'";
		getCategoryNameList = jdbcTemplate.query(getCategoryName, new CategoryNameMapper());
		return getCategoryNameList;
	}

	// --- Update Category Names---
	public void updateCategoryName(int id, String name) {
		String input = name;
		String categoryName = input.substring(0, 1).toUpperCase() + input.substring(1);
		String updateName = "update category set category_name=? where id=?";
		Object[] details = { categoryName, id };
		int updateRows = jdbcTemplate.update(updateName, details);
		logger.info("Update Category : " + updateRows);
	}

	// ---Find category By --using category Id-
	public Category findCategoryById(int id) {
		Category listCategory;
		String find = "select id,category_name,is_available from category where id=?";
		listCategory = jdbcTemplate.queryForObject(find, new CategoryMapperSingle(), id);
		return listCategory;
	}

	// --Delete category Details Using Category Id---
	public int deleteCategoryDetails(int id) {
		String delete = "update category set is_available='Not Available' where id=?";
		Object[] details = { id };
		int deleteRows = jdbcTemplate.update(delete, details);
		logger.info("Deleted Rows :" + deleteRows);
		return 1;
	}

	// ---Update In Active to Active Category ---
	public int activeCategoryDetails(int id) {
		String active = "update category set is_available='Available' where id=?";
		Object[] details = { id };
		int activeRows = jdbcTemplate.update(active, details);
		logger.info("Activated Product : " + activeRows);
		return 1;
	}
	// -----Sales ----

	// --- Get Monthly Sales List----
	public long getSalesList() {
		long salesAmount = 0;
		// --Query For Using Only Take Last Month Of Sales Profit
		String findMonthlySales = "select SUM(counts) from sales where month(Date)=month(now())-1";
		List<Sales> query = jdbcTemplate.query(findMonthlySales, new SalesAmountMapper());
		for (Sales sales : query) {
			salesAmount = sales.getSalesAmount();
		}
		return salesAmount;
	}
    
	public long getCurrentMonthSales() {
		long salesAmount = 0;
		// --Query For Using Only Take Current Month Of Sales Profit
		String findMonthlySales = "select SUM(counts) from sales where month(Date)=month(now())-0";
		List<Sales> query = jdbcTemplate.query(findMonthlySales, new SalesAmountMapper());
		for (Sales sales : query) {
			salesAmount = sales.getSalesAmount();
		}
		return salesAmount;
	}

	// ----Get Product Count Sales List
     
	public long getPreviousProductSales() {
		long quantitySalesCount = 0;
		// --Query For Using Only Take Current Month Of Sales Total Quantity
		String findQuery = "select SUM(counts) from product_sales where month(Date)=month(now())-0";
		List<Sales> getTotalQuantity = jdbcTemplate.query(findQuery, new SalesAmountMapper());
		for (Sales sales : getTotalQuantity) {
			quantitySalesCount = sales.getSalesAmount();
		}
		return quantitySalesCount;
	}

	public long getCurrentMonthProductSales() {
		long quantitySalesCount = 0;
		// --Query For Using Only Take Previous Month Of Sales Total Quantity
		String findQuery = "select SUM(counts) from product_sales where month(Date)=month(now())-1";
		List<Sales> getTotalQuantity = jdbcTemplate.query(findQuery, new SalesAmountMapper());
		for (Sales sales : getTotalQuantity) {
			quantitySalesCount = sales.getSalesAmount();
		}
		return quantitySalesCount;
	}
}
