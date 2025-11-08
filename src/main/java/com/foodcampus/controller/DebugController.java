package com.foodcampus.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodcampus.dto.TopProductDTO;
import com.foodcampus.dto.TopUserDTO;
import com.foodcampus.service.OrderService;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/reports")
    public Map<String, Object> reports() {
        int top = 20;
        List<TopProductDTO> topProducts = orderService.getTopProducts(top);
        List<TopUserDTO> topUsers = orderService.getTopUsers(top);
        Map<String, Object> m = new HashMap<>();
        m.put("topProducts", topProducts);
        m.put("topUsers", topUsers);
        return m;
    }
}

