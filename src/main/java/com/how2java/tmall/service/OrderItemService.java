package com.how2java.tmall.service;

import com.how2java.tmall.dao.OrderItemDAO;
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemDAO orderItemDAO;

    @Autowired
    private ProductImageSercice productImageSercice;


    public  void add(OrderItem item){
        this.orderItemDAO.save(item);
    }



    public void fill(List<Order> orders) {
        for (Order order:orders){
            fill(order);
        }
    }

    public void fill(Order order) {
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

    public int getSaleCount(Product product) {
        List<OrderItem> orderItems = listByProduct(product);
        int count = 0;
        for (OrderItem item:orderItems){
            if (item.getOrder()!=null){
                if (item.getOrder()!=null&&item.getOrder().getPayDate()!=null){
                    count+=item.getNumber();
                }
            }
        }
        return count;
    }

    private List<OrderItem> listByProduct(Product product) {
        List<OrderItem> items = this.orderItemDAO.findByProduct(product);
        return items;
    }

    public List<OrderItem> listByUser(User user){
        List<OrderItem> items = this.orderItemDAO.findAll();
        return items;
    }

    public void update(OrderItem item) {
        this.orderItemDAO.save(item);
    }

    public OrderItem get(int oiid) {
       return   this.orderItemDAO.getOne(oiid);
    }

    public void delete(int oiid){
        this.orderItemDAO.delete(oiid);
    }
}
