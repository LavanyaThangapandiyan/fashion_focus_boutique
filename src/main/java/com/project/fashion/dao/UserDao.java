package com.project.fashion.dao;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.fashion.exception.ExistMailIdException;
import com.project.fashion.exception.ExistMobileException;
import com.project.fashion.exception.InvalidEmailException;
import com.project.fashion.interfaces.UserInterface;
import com.project.fashion.mapper.CartMapper;
import com.project.fashion.mapper.OrderAmountMappper;
import com.project.fashion.mapper.OrderMapper;
import com.project.fashion.mapper.PaymentMapper;
import com.project.fashion.mapper.ProductMapperAll;
import com.project.fashion.mapper.UpdateCartMapper;
import com.project.fashion.mapper.UpdateOrderMapper;
import com.project.fashion.mapper.UserMapper;
import com.project.fashion.mapper.UserMapperSingle;
import com.project.fashion.mapper.WishListMapper;
import com.project.fashion.model.Cart;
import com.project.fashion.model.Order;
import com.project.fashion.model.Payment;
import com.project.fashion.model.Product;
import com.project.fashion.model.User;
import com.project.fashion.model.WishList;
import com.project.fashion.util.ConnectionUtil;
import com.project.fashion.validation.Validation;

@Repository

public class UserDao implements UserInterface {
	Logger logger = LoggerFactory.getLogger(UserDao.class);
	Validation valid = new Validation();
	JdbcTemplate jdbcTemplate = ConnectionUtil.getJdbcTemplate();

	// ----Inserting User Details
	public int saveDetails(User user, Model model)
			throws ExistMailIdException, ExistMobileException, JsonProcessingException {
		List<User> userList = userList(model);
		String getUser = userList.toString();
		String userEmail = user.getEmail();
		String mobile = user.getMobile();
		boolean contains = getUser.contains(userEmail);
		boolean mobilecont = getUser.contains(mobile);
		if (contains) {
			throw new ExistMailIdException("Exist Email Exception");

		} else if (mobilecont) {
			throw new ExistMobileException("Exist Mobile Number Exception");
		} else {
			String password = user.getPassword();
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String encodedPassword = encoder.encode(password);

			String insert = "insert into admin_user(username,email,password,phone_number,gender,is_available)values(?,?,?,?,?,'Available')";
			boolean name = valid.nameValidation(user.getName());
			boolean email1 = valid.emailValidation(user.getEmail());
			boolean password1 = valid.passwordValidation(user.getPassword());
			boolean phone = valid.phoneNumberValidation(user.getMobile());
			if (name && email1 && phone && password1) {
				String input = user.getName();
				String userName = input.substring(0, 1).toUpperCase() + input.substring(1);
				Object[] details = { userName, user.getEmail(), encodedPassword, user.getMobile(), user.getGender() };
				int numberOfRows = jdbcTemplate.update(insert, details);
				logger.info("Inserted Rows : " + numberOfRows);
				return 1;
			} else
				logger.error("Invalid Data");
		}
		return 0;
	}

	// --------Find User  Details-----------
	public int findUserDetails(User user) throws InvalidEmailException {
		String userEmail = user.getEmail();
		String password = user.getPassword();
		String check = valid.adminEmailValidation(userEmail);
		String find = "select password,email from admin_user where is_available='Available'";
		List<User> listUser = jdbcTemplate.query(find, new UserMapperSingle());
		List<User> users = listUser.stream().filter(userOne -> userOne.getEmail().equals(user.getEmail()))
				.collect(Collectors.toList());
		for (User userModel : users) {
			String dbpass = userModel.getPassword();
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			boolean match = encoder.matches(password, dbpass);
			if (check.equals("true") && match) {
				return 2;
			} else if (match) {
				return 1;
			}
		}
		throw new InvalidEmailException("Invalid Email Exception");
	}

	// --- Get User List----
	public List<User> userList(Model model) throws JsonProcessingException {
		String userList = "select id,username,email,password,phone_number,gender from admin_user where is_available='Available'";
		List<User> listUser = jdbcTemplate.query(userList, new UserMapper());
		ObjectMapper object = new ObjectMapper();
		String users = object.writeValueAsString(listUser);
		model.addAttribute("listofuser", users);
		return listUser;
	}
	// --Update user Password
	public int updateUserPassword(User user, Model model) throws InvalidEmailException, JsonProcessingException {
		String password = user.getPassword();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(password);
		List<User> userList = userList(model);
		String getUser = userList.toString();
		String userEmail = user.getEmail();
		boolean contains = getUser.contains(userEmail);
		if (contains) {
			String updatePassword = "update admin_user set password=? where email=?";
			Object[] details = { encodedPassword, user.getEmail() };
			int numberOfRows = jdbcTemplate.update(updatePassword, details);
			logger.info("Update Password : " + numberOfRows);
			return 1;
		} else
			throw new InvalidEmailException("Invalid Email ID");
	}

	// ----Find User ID By Email-----
	public int findIdByEmail(String email, HttpSession session) {
		String find = "select id,username,email,password,phone_number,gender from admin_user where email=?";
		List<User> query = jdbcTemplate.query(find, new UserMapper(), email);
		for (User userModel : query) {
			session.setAttribute("id", userModel.getId());
			session.setAttribute("userName", userModel.getName());
			session.setAttribute("email", userModel.getEmail());
		}
		return 0;
	}

	// ----Cart CRUD----
	Cart cart = new Cart();
	// --- Show Update Details---
	public Cart getcartUpdateDetails(int cartId) {
		Cart queryForObject;
		String findCart = "select id,size,quantity,total_amount from cart where id=? and is_available='Available'";
		queryForObject = jdbcTemplate.queryForObject(findCart, new UpdateCartMapper(), cartId);
		return queryForObject;
	}

	// --- Save Cart Details----
	public int saveCartDetails(int userId, int id, String productName, int price, String type, int quantity,
			String size) {
		String findCart = "select id,customer_id,product_id,product_name,price,size,product_type,quantity,total_amount,image,is_available from cart where customer_id=? ";
		List<Cart> query = jdbcTemplate.query(findCart, new CartMapper(), userId);
		for (Cart cartModel : query) {
			int productId = cartModel.getProductId();
			int quantity2 = cartModel.getQuantity();
			int amount = cartModel.getAmount();
			int addQuantity = quantity + quantity2;
			int calculateCurrentAmount = addQuantity * amount;
			if (productId == id) {
				String update = "update cart set quantity=?,total_amount=? where product_id=? and customer_id=? ";
				Object[] details = { addQuantity, calculateCurrentAmount, productId, userId };
				int update2 = jdbcTemplate.update(update, details);
				logger.info("Updated Quantity : " + update2);
				return 1;
			}
		}
		String find = "select id,name,price,category,size,quantity,fabric,gender,image from product where id=?";
		Product getDetails = jdbcTemplate.queryForObject(find, new ProductMapperAll(), id);
		String image = getDetails.getImage();
		int amount = price;
		int totalAmount = amount * quantity;
		String inserts = "insert into cart(customer_id ,product_id ,image,product_name ,price,size ,product_type ,quantity ,total_amount ,is_available )values(?,?,?,?,?,?,?,?,?,'Available')";
		Object[] details = { userId, id, image, productName, price, size, type, quantity, totalAmount };
		int rows = jdbcTemplate.update(inserts, details);
		logger.info("Insert Cart details : " + rows);
		return 2;
	}

	// ---- In active cart details---
	public void cancelCartDetails(int id) {
		String statusUpdate = "update cart set is_available='Not Available' where id=?";
		Object[] details = { id };
		int update = jdbcTemplate.update(statusUpdate, details);
		logger.info("Update status Cart : " + update);
	}

	// -----Active Cart details----
	public void clicktoActiveCartDetails(int id) {
		String statusUpdate = "update cart set is_available='Available' where id=?";
		Object[] details = { id };
		int update = jdbcTemplate.update(statusUpdate, details);
		logger.info("Update status Cart : " + update);
	}

	// --- Get ActiveCart List--------
	public List<Cart> cartList(int customerId) {
		List<Cart> cartList;
		String getCartList = " select id,customer_id,product_id,image,product_name,price,size,product_type,quantity,total_amount,is_available from cart where customer_id=? and is_available='Available'";
		cartList = jdbcTemplate.query(getCartList, new CartMapper(), customerId);
		return cartList;
	}
    
	//---Get In Active Cart List---
	public List<Cart> inActiveCartList(int customerId) {
		List<Cart> query;
		String getInActiveCartList = "select id,customer_id,product_id,image,product_name,price,size,product_type,quantity,total_amount,is_available from cart where customer_id=? and is_available='Not Available'";
		query = jdbcTemplate.query(getInActiveCartList, new CartMapper(), customerId);
		return query;
	}

	// ---Product Filter--
	public List<Product> allProductList(String category) {
		List<Product> productList;
		String find = "select id,name,price,category,size,quantity,fabric,gender,image from product where is_available='Available' and category=?";
		productList = jdbcTemplate.query(find, new ProductMapperAll(), category);
		return productList;
	}
	// --------------------Orders CRUD----------------

	Order order = new Order();

	// ---Save Order Details---
	public int saveOrderDetails(int userId) {
		// --find cart details and by using session id
		String getCartList = " select id,customer_id,product_id,image,product_name,price,size,product_type,quantity,total_amount,is_available from cart where customer_id=? and is_available='Available'";
		List<Cart> cartlist = jdbcTemplate.query(getCartList, new CartMapper(), userId);
		for (Cart cartModel : cartlist) {

			String insert = "insert into orders(customer_id,cart_id,product_id,image,productsname,price,size,category,quantity,total_amount,is_available)values(?,?,?,?,?,?,?,?,?,?,'Available')";
			Object[] details = { userId, cartModel.getId(), cartModel.getProductId(), cartModel.getImage(),
					cartModel.getProductName(), cartModel.getPrice(), cartModel.getSize(), cartModel.getProductType(),
					cartModel.getQuantity(), cartModel.getAmount() };
			int insertRows = jdbcTemplate.update(insert, details);
			logger.info("Inserted Order : " + insertRows);

		}
		return 0;
	}

	// ----update order details------
	public int updateOrderDetails(int id, String size, int quantity, int amount, int userId) {
		String findCart = "select id,size,quantity,total_amount from cart where id=? and is_available='Available' ";
		Cart queryForObject = jdbcTemplate.queryForObject(findCart, new UpdateCartMapper(), id);
		int amount2 = queryForObject.getAmount();
		int quantity2 = queryForObject.getQuantity();
		if (quantity > quantity2) {
			int addAmount = quantity * amount;
			String updateCart = "update cart set size=?,quantity=?,total_amount=? where id=?";
			Object[] details = { size, quantity, addAmount, id };
			String updateOrder = "update orders set size=?,quantity=?,total_amount=? where id=?";
			Object[] orderDetails = { size, quantity, addAmount, id };
			int update = jdbcTemplate.update(updateOrder, orderDetails);
			logger.info("Updated Order Details : " + update);
			int update2 = jdbcTemplate.update(updateCart, details);
			logger.info("Update Cart details : " + update2);
			return 1;
		} else {
			int updateAmount = amount2 - (amount2 / quantity2);
			String updateCart = "update cart set size=?,quantity=?,total_amount=? where id=?";
			Object[] details = { size, quantity, updateAmount, id };
			String updateOrder = "update orders set size=?,quantity=?,total_amount=? where id=?";
			Object[] orderDetails = { size, quantity, updateAmount, id };
			int update = jdbcTemplate.update(updateOrder, orderDetails);
			logger.info("Updated Order Details : " + update);
			int update2 = jdbcTemplate.update(updateCart, details);
			logger.info("Update Cart details : " + update2);
			return 1;
		}
	}
    //---Save Alter Table---
	public void saveAlterTable(int userId) {
		String listQuery = "select id,customer_id,product_id,image,productsname,price,size,category,quantity,total_amount,is_available from orders where customer_id=? and is_available='Available'";
		List<Order> getOrderList = jdbcTemplate.query(listQuery, new OrderMapper(), userId);
		for (Order cartModel : getOrderList) {
			String inserts = "insert into altercart(customer_id,product_id,image,productsname ,price,size ,category ,quantity ,total_amount ,is_available )values(?,?,?,?,?,?,?,?,?,'Available')";
			Object[] detail = { userId, cartModel.getProductId(), cartModel.getImage(), cartModel.getProductName(),
					cartModel.getPrice(), cartModel.getSize(), cartModel.getCategory(), cartModel.getQuantity(),
					cartModel.getAmount() };
			int saveAlter = jdbcTemplate.update(inserts, detail);
			logger.info("Insert Alter : " + saveAlter);

			// ---insert product Sales----
			LocalDate today = LocalDate.now();
			String insertProductSales = "insert into  product_sales(product_id,counts,Date)values(?,?,?)";
			Object[] detailsInsert = { cartModel.getProductId(), cartModel.getQuantity(), today };
			int insertRow = jdbcTemplate.update(insertProductSales, detailsInsert);
			logger.info("Insert Product Sales" + insertRow);

		}
	}

	// ----Cancel Order Details-----
	public int cancelOrder(int id) {
		String cancel = "update orders set is_available='Not Available' where id=?";
		Object[] details = { id };
		int cancelRows = jdbcTemplate.update(cancel, details);
		logger.info("Cancel Order Rows: " + cancelRows);
		return 1;
	}

	//---ReOrder---
	public int reOrder(int id) {
		String reOrder = "update orders set is_available='Available' where id=?";
		Object[] details = { id };
		int cancelRows = jdbcTemplate.update(reOrder, details);
		logger.info("Re Order Rows: " + cancelRows);
		return cancelRows;
	}

	// --Find Order Details Using orderId
	public Order getorderUpdateDetails(int orderId) {
		Order queryForObject;
		String find = "select id,size,quantity,total_amount from orders where id=? and is_available='Available' ";
		queryForObject = jdbcTemplate.queryForObject(find, new UpdateOrderMapper(), orderId);
		return queryForObject;
	}

	// ---- Get Orders List
	public List<Order> getOrdersList(int userId) {
		List<Order> getOrderList;
		String listQuery = "select id,customer_id,product_id,image,productsname,price,size,category,quantity,total_amount,is_available from orders where customer_id=? and is_available='Available'";
		getOrderList = jdbcTemplate.query(listQuery, new OrderMapper(), userId);
		return getOrderList;
	}

	public List<Order> getCancelledOrdersList(int userId) {
		List<Order> getOrderList;
		String listQuery = "select id,customer_id,product_id,image,productsname,price,size,category,quantity,total_amount,is_available from orders where customer_id=? and is_available='Not Available'";
		getOrderList = jdbcTemplate.query(listQuery, new OrderMapper(), userId);
		return getOrderList;
	}
    
	//---Get Total Order Amount----
	public List<Order> getTotalAmountOrder(int userId, HttpSession session) {
		String query = "SELECT SUM(total_amount) from orders where is_available='Available'";
		List<Order> query2 = jdbcTemplate.query(query, new OrderAmountMappper());
		for (Order orderModel : query2) {
			session.setAttribute("amount", orderModel.getAmount());
		}
		return query2;
	}

	// ---get order History-----
	public List<Order> getOrderHistoryList(int userId) {
		List<Order> query2;
		String query = "select id,customer_id,product_id,image,productsname,price,size ,category,quantity,total_amount,is_available from altercart where customer_id=? and  is_available='Available'";
		query2 = jdbcTemplate.query(query, new OrderMapper(), userId);
		return query2;
	}

	// --------Wish List CRUD--------

	WishList wish = new WishList();

	// ----Save Wish List
	public int saveWishList(int id, int userId) throws IOException {
		String find = "select id,name,price,category,size,quantity,fabric,gender,image from product where id=?";
		Product getDetails = jdbcTemplate.queryForObject(find, new ProductMapperAll(), id);
		String image = getDetails.getImage();
		String name = getDetails.getName();
		int price = getDetails.getPrice();
		String size = getDetails.getSize();
		String type = getDetails.getType();
		String insert = "insert into wish_list(customer_id,image,product_id,product_name,price,size,category,is_available)values(?,?,?,?,?,?,?,'Available')";
		Object[] details = { userId, image, id, name, price, size, type };
		int insertRow = jdbcTemplate.update(insert, details);
		logger.info("Insert Wish List : " + insertRow);
		return 1;
	}

	// ---- Find wish list using customer ID---
	public List<WishList> getWishListById(int customerId) {
		List<WishList> query;
		String getRow = "select customer_id,product_id,image,product_name,price,size,category from wish_list where customer_id=?";
		query = jdbcTemplate.query(getRow, new WishListMapper(), customerId);
		return query;
	}
	// -------------------Payment CRUD--------------------
	// ---Save Payment Details---
	public void savePaymentDetails(Payment payment, HttpSession session) {
		LocalDate today = LocalDate.now();
		String insert = "insert into payment(user_name,email,amount,payment_type,card_number,cvv,month,year,Date)values(?,?,?,?,?,?,?,?,?)";
		Object[] details = { payment.getUserName(), payment.getEmail(), payment.getAmount(), payment.getPaymentType(),
				payment.getCardNumber(), payment.getCvv(), payment.getMonth(), payment.getYear(), today };
		int numberOfRows = jdbcTemplate.update(insert, details);
		logger.info("Payment Inserted Rows : " + numberOfRows);

		// ---Complete Payment And add to Sales Table---
		String insertSales = "insert into sales (counts,Date)values(?,?)";
		Object[] inserts = { payment.getAmount(), today };
		int salesUpdate = jdbcTemplate.update(insertSales, inserts);
		logger.info("Inserted Sales Details : " + salesUpdate);
	}

	public void afterOrderClearCart(HttpSession session) {
		int userId = (int) session.getAttribute("id");
		// --After Order Process Complete Delete Orders Details---
		String delete = "delete from orders where customer_id=?";
		Object[] detail = { userId };
		int deleteOrder = jdbcTemplate.update(delete, detail);
		logger.info(" After Payment Clear Orders : " + deleteOrder);

		// ---After Order Empty to Cart----
		String clearCart = "delete from cart where customer_id=?";
		Object[] deleteCart = { userId };
		int deletesCart = jdbcTemplate.update(clearCart, deleteCart);
		logger.info("After Payment Clear Cart : " + deletesCart);

	}
}
