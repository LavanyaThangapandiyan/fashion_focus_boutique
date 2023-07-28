package com.project.fashion.service;


import java.util.List;
import org.springframework.stereotype.Service;
import com.project.fashion.dao.ProductDao;
import com.project.fashion.exception.ExistCategoryException;
import com.project.fashion.exception.ExistProductException;
import com.project.fashion.model.Category;
import com.project.fashion.model.Product;
@Service

public class ProductService 
{
	ProductDao productDao=new ProductDao();
	
	public int saveProducts(Product product) throws ExistProductException
	{
		return productDao.saveProductDetails(product);
	}
	
	
	public void updateProductDetails(int id, String name, int price, String size, int quantity, String fabric,
			String gender)
	{
		productDao.updateProductDetails(id, name, price, size, quantity, fabric, gender);
	}
	public List<Product> inActiveProductList()
	{
		return productDao.inActiveProductList();
		
	}
		
	public List<Product> allProductList()
	{
		return productDao.allProductList();
	}
	
	
	public Product getProductById(int productId)
	{
		return productDao.getProductById(productId);
	}
	
	public void deleteProduct(int id)
	{
		productDao.deleteProduct(id);
	}
	
	public void activeProduct(int id)
	{
		productDao.activeProduct(id);
	}
	
	public void saveCategoryDetails(Category category) throws ExistCategoryException 
	{
		productDao.saveCategoryDetails(category);
	}
	public List<Category> categoryList()
	{
		return productDao.categoryList();
	}
	
	public List<Category> inActiveCategoryList()
	{
		return productDao.inActiveCategoryList();
	}
	
	public List<Category> getCategoryName()
	{
		return productDao.getCategoryName();	
	}
	
	public void updateCategoryName(int id, String name)
	{
		productDao.updateCategoryName(id, name);
	}
	public Category findCategoryById(int id)
	{
		return productDao.findCategoryById(id);
	}
	
	public void deleteCategoryDetails(int id)
	{
		productDao.deleteCategoryDetails(id);
	}
	public void activeCategoryDetails(int id)
	{
		productDao.activeCategoryDetails(id);
	}
	public long getCurrentMonthSales()
	{
		return productDao.getCurrentMonthSales();
		
	}
	
	public long getSalesList()
	{
		return productDao.getSalesList();
	}
	
	public long getPreviousProductSales()
	{
		return productDao.getCurrentMonthProductSales();
	}
	public long getCurrentMonthProductSales()
	{
		return productDao.getPreviousProductSales();
		
	}
}