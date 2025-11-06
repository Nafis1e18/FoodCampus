package com.foodcampus.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.foodcampus.model.Cart;
import com.foodcampus.model.OrderAddress;
import com.foodcampus.model.OrderRequest;
import com.foodcampus.model.Product;
import com.foodcampus.model.ProductOrder;
import com.foodcampus.model.UserDtls;
import com.foodcampus.repository.CartRepository;
import com.foodcampus.repository.ProductOrderRepository;
import com.foodcampus.service.OrderService;
import com.foodcampus.util.CommonUtil;
import com.foodcampus.util.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private ProductOrderRepository orderRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CommonUtil commonUtil;

	@Override
	public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception {

		List<Cart> carts = cartRepository.findByUserId(userid);

		for (Cart cart : carts) {

			ProductOrder order = new ProductOrder();

			order.setOrderId(UUID.randomUUID().toString());
			order.setOrderDate(LocalDate.now());

			order.setProduct(cart.getProduct());
			// use helper to compute a safe unit price (prefers discountPrice, falls back to compute)
			double unitPrice = getEffectivePrice(cart.getProduct());
			order.setPrice(unitPrice);

			order.setQuantity(cart.getQuantity());
			order.setUser(cart.getUser());

			order.setStatus(OrderStatus.IN_PROGRESS.getName());
			order.setPaymentType(orderRequest.getPaymentType());

			OrderAddress address = new OrderAddress();
			address.setFirstName(orderRequest.getFirstName());
			address.setLastName(orderRequest.getLastName());
			address.setEmail(orderRequest.getEmail());
			address.setMobileNo(orderRequest.getMobileNo());
			address.setAddress(orderRequest.getAddress());
			address.setCity(orderRequest.getCity());
			address.setState(orderRequest.getState());
			address.setPincode(orderRequest.getPincode());

			order.setOrderAddress(address);

			ProductOrder saveOrder = orderRepository.save(order);
			resetCart(cart.getUser());
			commonUtil.sendMailForProductOrder(saveOrder, "success");
		}
	}

	private double getEffectivePrice(Product product) {
		double price = product.getPrice() == null ? 0.0 : product.getPrice();
		Integer discount = null;
		try {
			discount = product.getDiscount();
		} catch (Exception e) {
			discount = 0;
		}

		Double discountPrice = product.getDiscountPrice();
		if (discountPrice != null) {
			return discountPrice.doubleValue();
		}

		double disc = (discount == null) ? 0.0 : (discount / 100.0);
		double computed = price - (price * disc);
		return computed;
	}

	private void resetCart(UserDtls user) {
		cartRepository.deleteByUser(user);
	}
	@Override
	public List<ProductOrder> getOrdersByUser(Integer userId) {
		List<ProductOrder> orders = orderRepository.findByUserId(userId);
		return orders;
	}

	@Override
	public ProductOrder updateOrderStatus(Integer id, String status) {
		Optional<ProductOrder> findById = orderRepository.findById(id);
		if (findById.isPresent()) {
			ProductOrder productOrder = findById.get();
			productOrder.setStatus(status);
			ProductOrder updateOrder = orderRepository.save(productOrder);
			return updateOrder;
		}
		return null;
	}

	@Override
	public List<ProductOrder> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		return orderRepository.findAll(pageable);

	}

	@Override
	public ProductOrder getOrdersByOrderId(String orderId) {
		return orderRepository.findByOrderId(orderId);
	}

}
