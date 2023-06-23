package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
//    @Autowired
//    OrderRepository orderRepository;
    public void addOrder(Order order) {
        new OrderRepository().addOrder(order);
    }

    public void addPartner(String partnerId) {
        new OrderRepository().addPartner(partnerId);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        new OrderRepository().addOrderPartnerPair(orderId, partnerId);
    }

    public Order getOrderById(String orderId) {
        return new OrderRepository().getOrderById(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return new OrderRepository().getPartnerById(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return new OrderRepository().getOrderCountByPartnerId(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return new OrderRepository().getOrdersByPartnerId(partnerId);
    }

    public List<String> getAllOrders() {
        return new OrderRepository().getAllOrders();
    }

    public Integer getCountOfUnassignedOrders() {
        return new OrderRepository().getCountOfUnassignedOrders();
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int lastDeliveryTime = new OrderRepository().getLastDeliveryTimeByPartnerId(partnerId);
        if(lastDeliveryTime == -1)
            return null;
        String MM = String.valueOf(lastDeliveryTime % 60);
        String HH = String.valueOf(lastDeliveryTime / 60);
        if(HH.length() < 2)
            HH = "0" + HH;
        if(MM.length() < 2)
            MM = "0" + MM;
        return (HH + ":" + MM);
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int HH = Integer.parseInt(time.substring(0, 2));
        int MM = Integer.parseInt(time.substring(3, 5));
        int givenTime  = (HH * 60) + MM;
        return new OrderRepository().getOrdersLeftAfterGivenTimeByPartnerId(givenTime, partnerId);
    }

    public void deletePartnerById(String partnerId) {
        new OrderRepository().deletePartnerById(partnerId);
    }

    public void deleteOrderById(String orderId) {
        new OrderRepository().deleteOrderById(orderId);
    }
}
