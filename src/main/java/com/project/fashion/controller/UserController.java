package com.project.fashion.controller;

import java.io.IOException;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.fashion.exception.ExistMailIdException;
import com.project.fashion.exception.ExistMobileException;
import com.project.fashion.exception.InvalidEmailException;
import com.project.fashion.model.Payment;
import com.project.fashion.model.Product;
import com.project.fashion.model.User;
import com.project.fashion.service.ProductService;
import com.project.fashion.service.UserService;
import com.project.fashion.validation.Validation;

@Controller
public class UserController {

	UserService userService = new UserService();
	ProductService productService = new ProductService();
	Validation valid = new Validation();
	Product product = new Product();
	User users = new User();
	@Value("${email:}")
	String email;

	@GetMapping("/")
	public String showHome() {
		return "index";
	}

	// method to get register form
	@GetMapping("/login")
	public String getLoginForm() {
		return "login";
	}

	@GetMapping(path = "/forgot")
	public String showFormForforgotPassword() {

		return "forgot";
	}

	@GetMapping("/registerland")
	public String afterRegisterSuccess() {
		return "redirect:login";
	}

	@GetMapping("/adminland")
	public String afterLoginSuccess() {
		return "redirect:/products";
	}

	@PostMapping(path = "/registers")
	public String saveUserDetails(@RequestParam("username") String name, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("mobile") String mobile,
			@RequestParam("gender") String gender, Model model)
			throws ExistMailIdException, ExistMobileException, JsonProcessingException {
		users.setName(name);
		users.setEmail(email);
		users.setPassword(password);
		users.setMobile(mobile);
		users.setGender(gender);
		int number = userService.saveDetails(users, model);
		if (number == 1)
			return "registerpopup";
		else
			return "register";
	}

	// method to get register form details

	@PostMapping(path = "/loginsubmit")
	public String submitUserRegisterForm(@RequestParam("email") String email, @RequestParam("password") String password,
			@ModelAttribute User user, HttpSession session) throws InvalidEmailException {
		user.setEmail(email);
		user.setPassword(password);
		int number = userService.findUserDetails(user);
		if (number == 2)
			return "redirect:/list";
		else if (number == 1) {
			userService.findIdByEmail(email, session);
			return "loginpopup";
		} else
			return "";
	}
	
	@GetMapping("/list")
	public String showSalesinList(Model model )
	{
		long salesList = productService.getSalesList();
		model.addAttribute("salesamount",salesList);
		long currentMonthSales = productService.getCurrentMonthSales();
		model.addAttribute("currentMonthSales",currentMonthSales);
		long currentMonthProductSales = productService.getCurrentMonthProductSales();
		model.addAttribute("currentMonthProductSalesCount",currentMonthProductSales);
		long previousProductSales = productService.getPreviousProductSales();
		model.addAttribute("previousMonthProductSalesCount",previousProductSales);
		return "list";
	}
	
	
	

	// -----Handling Invalid Email Exception-----
	@ExceptionHandler(InvalidEmailException.class)
	public String invalidException(InvalidEmailException email, Model model) {
		model.addAttribute("errormessage", "Invalid login or password. Please try again.");
		return "error";
	}

	@GetMapping(path = "/register")
	public String getRegisterForm() {
		return "register";
	}

	// ---Handling Exist Mail ID Exception ----
	@ExceptionHandler(ExistMailIdException.class)
	public String existMailException(ExistMailIdException exist, Model model) {
		model.addAttribute("errormessage", "Email Id Already Exist.");
		return "error";
	}

	// ---Handling Exist Mobile Number Exception----
	@ExceptionHandler(ExistMobileException.class)
	public String existMobileNumberException(ExistMobileException existMobile, Model model) {
		model.addAttribute("errormessage", "Mobile Number Already Exist.");
		return "error";
	}

	@GetMapping("forgotpassword")
	public String resetPassword(@RequestParam("email") String email, @RequestParam("password") String password,
			Model model) throws InvalidEmailException, JsonProcessingException {
		users.setEmail(email);
		users.setPassword(password);
		int number = userService.updateUserPassword(users, model);
		if (number == 1)
			return "login";
		else
			return "forgot";
	}

	@GetMapping("/customer")
	public String getAllUsers(Model model) throws JsonProcessingException {
		model.addAttribute("userlist", userService.userList(model));
		return "customer";
	}

	@GetMapping("/cartupdate")
	public String updateCartDetails(@RequestParam("id") int id, @RequestParam("size") String size,
			@RequestParam("quantity") int quantity, @RequestParam("amount") int amount, HttpSession session) {
		int userId = (int) session.getAttribute("id");
		userService.updateOrderDetails(id, size, quantity, amount, userId);
		return "redirect:/mycart";
	}

	@GetMapping("/updatecart/{id}")
	public String updateSizeQuantity(@PathVariable("id") int id, Model model) {
		model.addAttribute("updatedata", userService.getcartUpdateDetails(id));

		return "updatepopup";
	}

	@GetMapping(path = "/addcart")
	public String saveCartDetails(@RequestParam("userId") int userId, @RequestParam("id") int id,
			@RequestParam("productname") String name, @RequestParam("price") int price,
			@RequestParam("type") String type, @RequestParam("quantity") int quantity,
			@RequestParam("size") String size) {
		userService.saveCartDetails(userId, id, name, price, type, quantity, size);
		return "redirect:/mycart";
	}

	@GetMapping(path = "/deletecart/{id}")
	public String cancelCartDetails(@PathVariable(value = "id") int id) {
		userService.cancelCartDetails(id);
		return "redirect:/mycart";
	}

	@GetMapping(path = "/activecart/{id}")
	public String acitveCartDetails(@PathVariable(value = "id") int id) {
		userService.clicktoActiveCartDetails(id);
		return "redirect:/mycart";
	}

	@GetMapping("/mycart")
	public String getmyCart(Model model, HttpSession session) {
		int userId = (int) session.getAttribute("id");
		model.addAttribute("mycartlist", userService.cartList(userId));
		model.addAttribute("inactivelist", userService.inActiveCartList(userId));
		return "mycart";
	}

	@GetMapping("/inactive")
	public String showInActiveCartList(Model model, HttpSession session) {
		int userId = (int) session.getAttribute("id");
		model.addAttribute("inactivelist", userService.inActiveCartList(userId));
		return "inactive";

	}

	@GetMapping("/products")
	public String viewProductList(Model model) {
		model.addAttribute("card", productService.allProductList());
		return "productlist";
	}

	@GetMapping(path = "/addwish/{id}")
	public String saveWIshListDetails(@PathVariable("id") int id, HttpSession session, Model model) throws IOException {
		model.addAttribute("card", productService.allProductList());
		int userId = (int) session.getAttribute("id");
		userService.saveWishList(id, userId);
		return "redirect:/products";
	}

	@GetMapping(path = "/deleteorder/{id}")
	public String deleteOrderDetails(@PathVariable(value = "id") int id) {
		userService.cancelOrder(id);
		return "redirect:/myorder";
	}

	@GetMapping("/updateorder/{id}")
	public String updateOrderDetails(@PathVariable("id") int id, Model model) {
		model.addAttribute("updatedata", userService.getorderUpdateDetails(id));
		return "updatepopup";
	}

	@GetMapping("/placeorder")
	public String getOrderDetails(@RequestParam("userId") int userId) {
		userService.saveOrderDetails(userId);
		return "reorderpopup";
	}

	@GetMapping("/addorder")
	public String redirectOrderPage() {
		return "redirect:/myorder";
	}

	@GetMapping("/payment")
	public String showPaymentForm() {
		return "payment";
	}

	@GetMapping("/confirmorder")
	public String confirmOrder(HttpSession session, Model model) {
		int userId = (int) session.getAttribute("id");
		userService.saveAlterTable(userId);

		return "payment";
	}

	@GetMapping("/mywish")
	public String showWishList(Model model, HttpSession session) {
		int userId = (int) session.getAttribute("id");
		model.addAttribute("wishlist", userService.getWishListById(userId));
		return "wishlist";
	}

	@GetMapping("/myorder")
	public String showMyOrderList(Model model, HttpSession session) {
		int userId = (int) session.getAttribute("id");
		model.addAttribute("orderlist", userService.getOrdersList(userId));
		model.addAttribute("cancelorder", userService.getCancelledOrdersList(userId));
		model.addAttribute("amount", userService.getTotalAmountOrder(userId, session));
		return "myorder";
	}

	@GetMapping(path = "/activeInactive/{id}")
	public String activeAndInActiveWishList(@PathVariable(value = "id") int id) {
		userService.activeAndInActiveWishList(id);
		return "wish_list";
	}

	@GetMapping(path = "/cancelorder/{id}")
	public String cancelOrder(@PathVariable(value = "id") int id) {
		userService.cancelOrder(id);
		return "redirect:order";
	}

	@GetMapping(path = "/reorder/{id}")
	public String reOrder(@PathVariable(value = "id") int id) {
		userService.reOrder(id);
		return "redirect:/myorder";
	}

	@GetMapping("/payland")
	public String afterPaymentSuccess() {
		return "thank";
	}

	@PostMapping("/pay")
	public String getPaymentDetails(@RequestParam("userName") String name, @RequestParam("email") String email,
			@RequestParam("amount") int amount, @RequestParam("pay") String type,
			@RequestParam("cardnumber") String card, @RequestParam("cvv") int cvv, @RequestParam("month") String month,
			@RequestParam("year") String year, HttpSession session) {
		Payment payment = new Payment();
		payment.setUserName(name);
		payment.setEmail(email);
		payment.setAmount(amount);
		payment.setPaymentType(type);
		Long number = Long.parseLong(card);
		payment.setCardNumber(number);
		payment.setCvv(cvv);
		payment.setMonth(month);
		payment.setYear(year);
		userService.savePaymentDetails(payment, session);
		return "success";
	}

	@GetMapping("/history")
	public String getHistoryList(Model model, HttpSession session) {
		int userId = (int) session.getAttribute("id");
		model.addAttribute("historylist", userService.getOrderHistoryList(userId));
		return "history";
	}
	
	@GetMapping("/logout")
	public String getLogOutRequest(Model model,HttpSession session)
	{	
		return "logoutpopup";	
	}
	
	@GetMapping("/logoutland")
	public String getLogoutConfirmation(HttpSession session)
	{	
		userService.afterOrderClearCart(session);
		session.invalidate();
		return "index";
	}
	
	@GetMapping("/cod")
	public String showCodForm(HttpSession session)
	{	
		return "cod";
	}
	@GetMapping("/codland")
	public String showEndPage(HttpSession session)
	{
		return "thank";
	}
}
