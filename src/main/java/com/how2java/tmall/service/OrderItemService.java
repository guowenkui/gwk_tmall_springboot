package com.how2java.tmall.service;

import com.how2java.tmall.dao.OrderItemDAO;
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemDAO orderItemDAO;

    @Autowired
    private ProductImageSercice productImageSercice;


    public void fill(List<Order> orders) {
        for (Order order:orders){
            fill(order);
        }
    }

    private void fill(Order order) {
        List<OrderItem> orderItems = listByOrder(order);
        float total = 0;
        int totalNumber = 0;
        for (OrderItem item:orderItems){
            total +=item.getNumber()*item.getProduct().getPromotePrice();
            totalNumber+=item.getNumber();
            this.productImageSercice.setFirstProductImage(item.getProduct());
        }

        order.setTotal(total);
        order.setTotalNumber(totalNumber);
        order.setOrderItems(orderItems);
    }

    private List<OrderItem> listByOrder(Order order) {
        List<OrderItem> items =this.orderItemDAO.findByOrderOrderByIdDesc(order);
        return  items;
    }
}
