package com.project.fashion.interfaces;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.fashion.exception.ExistMailIdException;
import com.project.fashion.exception.ExistMobileException;
import com.project.fashion.exception.InvalidEmailException;
import com.project.fashion.model.Cart;
import com.project.fashion.model.Order;
import com.project.fashion.model.Payment;
import com.project.fashion.model.Product;
import com.project.fashion.model.User;
import com.project.fashion.model.WishList;

public interface UserInterface {
	// -----User-----
	public int saveDetails(User user, Model model)
			throws ExistMailIdException, ExistMobileException, JsonProcessingException;

	public int findUserDetails(User user) throws InvalidEmailException;

	public List<User> userList(Model model) throws JsonProcessingException;

	public int deleteUserDetails(User user);

	public int updateUserPassword(User user, Model model) throws InvalidEmailException, JsonProcessingException;

	public int findIdByEmail(String email, HttpSession session);

	// ----Cart----
	public Cart getcartUpdateDetails(int cartId);

	public int saveCartDetails(int userId, int id, String productName, int price, String type, int quantity,
			String size);

	public void cancelCartDetails(int id);

	public void clicktoActiveCartDetails(int id);

	public List<Cart> cartList(int customerId);

	public List<Cart> inActiveCartList(int customerId);

	public List<Product> allProductList(String category);

	// -----Order----
	public int updateOrderDetails(int id, String size, int quantity, int amount, int userId);

	public int saveOrderDetails(int userId);

	public int cancelOrder(int id);

	public int reOrder(int id);

	public Order getorderUpdateDetails(int orderId);

	public List<Order> getOrdersList(int userId);

	public List<Order> getCancelledOrdersList(int userId);

	public List<Order> getTotalAmountOrder(int userId, HttpSession session);

	public List<Order> getOrderHistoryList(int userId);

	// ----Wish list-----
	public int saveWishList(int id, int userId) throws IOException;

	public List<WishList> getWishListById(int customerId);

	public int activeAndInActiveWishList(int wishListId);

	// --Payment----
	public void savePaymentDetails(Payment payment, HttpSession session);

	public List<Payment> paymentList();

	public void saveAlterTable(int userId);
}
