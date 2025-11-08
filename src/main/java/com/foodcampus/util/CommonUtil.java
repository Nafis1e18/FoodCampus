package com.foodcampus.util;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
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

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    // Read mail username to know if mail is configured
    @Value("${spring.mail.username:}")
    private String mailUsername;

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

    String msg=null;

    // Make this method resilient: don't throw on mail failures so order saving still succeeds
    public Boolean sendMailForProductOrder(ProductOrder order, String status) throws MessagingException, UnsupportedEncodingException {

        // If mail username is not configured, skip sending and log (prevents auth failures)
        if (mailUsername == null || mailUsername.trim().isEmpty()) {
            logger.warn("Mail username (spring.mail.username) not configured — skipping order email for orderId={}", order == null ? null : order.getOrderId());
            return false;
        }

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
        try {
            // The MimeMessageHelper constructor and helper methods can throw MessagingException
            // but we already created helper above; however ensure any messaging exceptions are caught
            mailSender.send(message);
            return true;
        } catch (MailException mex) {
            // Log and swallow mail exceptions so order processing doesn't fail
            logger.error("Failed to send order email for orderId={}: {}", order == null ? null : order.getOrderId(), mex.getMessage());
            return false;
        } catch (Exception ex) {
            // Catch any MessagingException or other exceptions thrown by helper
            logger.error("Messaging error while preparing/sending order email for orderId={}: {}", order == null ? null : order.getOrderId(), ex.getMessage());
            return false;
        }
    }

    public UserDtls getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        UserDtls userDtls = userService.getUserByEmail(email);
        return userDtls;
    }


}
