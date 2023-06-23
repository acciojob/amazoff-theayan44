package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {
    //for storing orders list --> <OrderId, order>
    HashMap<String, Order> ordersMap = new HashMap<>();

    //for storing partners list --> <PartnerId, partner>
    HashMap<String, DeliveryPartner> partnersMap = new HashMap<>();

    //for storing which order assigned to which partner --> <OrderId, PartnerId>
    HashMap<String, String> orderPartnerMap = new HashMap<>();

    //for storing the orders list assigned to a specific partner --> <PartnerId, List<OrdersId>>
    HashMap<String, ArrayList<String>> partnerOrderMap = new HashMap<>();

    public String addOrder(Order order) {
        //Add this order in the Orders Map
        String orderId = order.getId();
        ordersMap.put(orderId, order);
        return "New order added successfully";
    }

    public String addPartner(String partnerId) {
        //Add this partner in the Partners Map
        DeliveryPartner newPartner = new DeliveryPartner(partnerId);
        partnersMap.put(partnerId, newPartner);
        return "New delivery partner added successfully";
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        //*******check the order and partner is present or not***************
        if(ordersMap.containsKey(orderId) && partnersMap.containsKey(partnerId)) {
            //First add this pair in the Orders-Partners Map
            orderPartnerMap.put(orderId, partnerId);

            //Now Add this pair in the Partner-Orders Map
            if (!partnerOrderMap.containsKey(partnerId))
                partnerOrderMap.put(partnerId, new ArrayList<>());
            partnerOrderMap.get(partnerId).add(orderId);

            //now update the number of order count of that particular partner
            int currOrder = partnersMap.get(partnerId).getNumberOfOrders();
            partnersMap.get(partnerId).setNumberOfOrders(currOrder + 1);
        }
    }

    public Order getOrderById(String orderId) {
        return ordersMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnersMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return partnerOrderMap.get(partnerId).size();
//        if(!partnersMap.containsKey(partnerId))
//            return 0;
//        return partnersMap.get(partnerId).getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerOrderMap.get(partnerId);
    }

    public List<String> getAllOrders() {
        List<String> list = new ArrayList<>();
        for(String order : ordersMap.keySet()){
            list.add(order);
        }
        return list;
    }

    public Integer getCountOfUnassignedOrders() {
        return ordersMap.size() - orderPartnerMap.size();
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId) {
        if(!partnerOrderMap.containsKey(partnerId))
            return -1;
        List<String> list = partnerOrderMap.get(partnerId);
        int maxTime = 0;
        for(String orderId : list){
            int currTime = ordersMap.get(orderId).getDeliveryTime();
            maxTime = Math.max(maxTime, currTime);
        }
        return maxTime;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int givenTime, String partnerId) {
        List<String> list = partnerOrderMap.get(partnerId);
        int count = 0;
        for(String orderId : list){
            if(ordersMap.get(orderId).getDeliveryTime() > givenTime)
                count++;
        }
        return Integer.valueOf(count);
    }

    public void deletePartnerById(String partnerId) {
        if(partnersMap.containsKey(partnerId)) {
            if (partnersMap.get(partnerId).getNumberOfOrders() > 0) {
                List<String> orders = partnerOrderMap.get(partnerId);

                //Remove from Partner-Order Map
                partnerOrderMap.remove(partnerId);

                //Remove from Order-Partner Map
                for (String order : orders) {
                    orderPartnerMap.remove(order);
                }
            }

            //Remove from Partner Map
            partnersMap.remove(partnerId);
        }
    }

    public void deleteOrderById(String orderId) {
        if(ordersMap.containsKey(orderId)) {
            String partnerId = orderPartnerMap.get(orderId);
            if (partnerId != null) {
                //Remove from Order-Partner Map
                orderPartnerMap.remove(orderId);

                //Remove form Partner-Order Map
                partnerOrderMap.get(partnerId).remove(orderId);
                int currOrder = partnersMap.get(partnerId).getNumberOfOrders();
                partnersMap.get(partnerId).setNumberOfOrders(currOrder - 1);

            }

            //Remove from Orders Map
            ordersMap.remove(orderId);
        }
    }
}
