package com.foodcampus.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.foodcampus.dto.TopProductDTO;
import com.foodcampus.dto.TopUserDTO;
import com.foodcampus.model.OrderRequest;
import com.foodcampus.model.ProductOrder;

public interface OrderService {

	public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception;

	public List<ProductOrder> getOrdersByUser(Integer userId);

	public ProductOrder updateOrderStatus(Integer id, String status);

	public List<ProductOrder> getAllOrders();

	public ProductOrder getOrdersByOrderId(String orderId);
	
	public Page<ProductOrder> getAllOrdersPagination(Integer pageNo,Integer pageSize);

	// new methods for reports
	public List<TopProductDTO> getTopProducts(int limit);

	public List<TopUserDTO> getTopUsers(int limit);
}
