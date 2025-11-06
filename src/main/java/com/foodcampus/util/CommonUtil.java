package com.foodcampus.util;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.foodcampus.model.Product;
import com.foodcampus.model.ProductOrder;
import com.foodcampus.model.UserDtls;
import com.foodcampus.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private UserService userService;

	public Boolean sendMail(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("nirobnafis3@gmail.com", "Nafis");
		helper.setTo(reciepentEmail);

		String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + url
				+ "\">Change my password</a></p>";
		helper.setSubject("Password Reset");
		helper.setText(content, true);
		mailSender.send(message);
		return true;
	}

	public static String generateUrl(HttpServletRequest request) {

		// http://localhost:8080/forgot-password
		String siteUrl = request.getRequestURL().toString();

		return siteUrl.replace(request.getServletPath(), "");
	}
	
	String msg=null;;
	
	public Boolean sendMailForProductOrder(ProductOrder order,String status) throws Exception
	{
		// build message template with safe values
		String productName = "";
		String category = "";
		String quantity = "0";
		String priceStr = "0.00";
		String paymentType = "";
		String customerName = "Customer";

		if (order != null) {
			if (order.getOrderAddress() != null && order.getOrderAddress().getFirstName() != null) {
				customerName = order.getOrderAddress().getFirstName();
			}
			if (order.getProduct() != null) {
				Product p = order.getProduct();
				productName = p.getTitle() == null ? "" : p.getTitle();
				category = p.getCategory() == null ? "" : p.getCategory();
				// determine price: prefer order.price, then product.discountPrice, then compute
				double computedPrice = 0.0;
				if (order.getPrice() != null) {
					computedPrice = order.getPrice();
				} else if (p.getDiscountPrice() != null) {
					computedPrice = p.getDiscountPrice();
				} else if (p.getPrice() != null) {
					Integer disc = p.getDiscount();
					double discVal = (disc == null) ? 0.0 : (disc / 100.0);
					computedPrice = p.getPrice() - (p.getPrice() * discVal);
				}
				priceStr = String.format("%.2f", computedPrice);
			}
			if (order.getQuantity() != null) {
				quantity = order.getQuantity().toString();
			}
			if (order.getPaymentType() != null) {
				paymentType = order.getPaymentType();
			}
		}

		msg="<p>Hello [[name]],</p>"
				+ "<p>Thank you order <b>[[orderStatus]]</b>.</p>"
				+ "<p><b>Product Details:</b></p>"
				+ "<p>Name : [[productName]]</p>"
				+ "<p>Category : [[category]]</p>"
				+ "<p>Quantity : [[quantity]]</p>"
				+ "<p>Price : [[price]]</p>"
				+ "<p>Payment Type : [[paymentType]]</p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("nirobnafis3@gmail.com", "Nafis");
		if (order != null && order.getOrderAddress() != null && order.getOrderAddress().getEmail() != null) {
			helper.setTo(order.getOrderAddress().getEmail());
		} else {
			// if no recipient, return false gracefully
			return false;
		}

		msg=msg.replace("[[name]]",customerName);
		msg=msg.replace("[[orderStatus]]",status == null ? "" : status);
		msg=msg.replace("[[productName]]", productName);
		msg=msg.replace("[[category]]", category);
		msg=msg.replace("[[quantity]]", quantity);
		msg=msg.replace("[[price]]", priceStr);
		msg=msg.replace("[[paymentType]]", paymentType);

		helper.setSubject("Product Order Status");
		helper.setText(msg, true);
		mailSender.send(message);
		return true;
	}
	
	public UserDtls getLoggedInUserDetails(Principal p) {
		String email = p.getName();
		UserDtls userDtls = userService.getUserByEmail(email);
		return userDtls;
	}
	

}
