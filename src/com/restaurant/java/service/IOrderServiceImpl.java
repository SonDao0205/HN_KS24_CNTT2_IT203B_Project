package com.restaurant.java.service;

import com.restaurant.java.dao.OrderDao;
import com.restaurant.java.dao.TableDao;
import com.restaurant.java.entity.Menu_Item;
import com.restaurant.java.entity.Order;
import com.restaurant.java.entity.Order_Item;
import com.restaurant.java.entity.Table;
import com.restaurant.java.entity.enums.OrderEnum;
import com.restaurant.java.entity.enums.OrderItemEnum;
import com.restaurant.java.entity.enums.TableEnum;
import com.restaurant.java.utils.Constant;
import com.restaurant.java.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class IOrderServiceImpl implements IOrderService {
    private static IOrderServiceImpl instance;

    private IOrderServiceImpl() {
    }

    public static IOrderServiceImpl getInstance() {
        if (instance == null) {
            instance = new IOrderServiceImpl();
        }
        return instance;
    }

    OrderDao orderDao = OrderDao.getInstance();

    @Override
    public boolean createOrder(int user_id, int table_id) {
        if (user_id <= 0 || table_id <= 0) {
            System.out.println("Dữ liệu không phù hợp!");
            return false;
        }
        return orderDao.createOrder(user_id, table_id);
    }

    @Override
    public List<Order_Item> getOrderItems(int user_id, int table_id, Order order) {
        if(user_id <= 0 || table_id <= 0 || order == null) {
            System.out.println("Dữ liệu không hợp lệ!");
            return null;
        }
        return  orderDao.getOrderItems(user_id, table_id, order);
    }

    public List<Order_Item> getAllOrderItems(int table_id, Order order) {
        if(table_id <= 0 || order == null) {
            System.out.println("Dữ liệu không hợp lệ!");
            return null;
        }
        return  orderDao.getAllOrderItems(table_id, order);
    }

    @Override
    public boolean cancelOrderItem(int order_item_id) {
        if (order_item_id <= 0) {
            System.out.println("Dữ liệu không hợp lệ!");
            return false;
        }
        OrderItemEnum status = orderDao.getOrderItemStatus(order_item_id);
        if(status == null) {
            System.out.println(Constant.INVALID_ID_FOUND);
            return false;
        }
        if(status.equals(OrderItemEnum.pending) ||  status.equals(OrderItemEnum.waiting)) {
            return orderDao.cancelOrderItem(order_item_id);
        } else if (status.equals(OrderItemEnum.cancel)) {
            System.out.println("Món này đã bị huỷ!");
            return false;
        } else{
            System.out.println("Món đang được xử lý, không thể huỷ!");
            return false;
        }
    }

    @Override
    public Order getOrder(int user_id, int table_id) {
        if(user_id <= 0 || table_id <= 0) {
            System.out.println("Dữ liệu không hợp lệ!");
            return null;
        }
        return orderDao.getOrder(user_id, table_id);
    }

    @Override
    public List<Table> getListTableByUser(int user_id){
        if(user_id <= 0){
            System.out.println("Dữ liệu không hợp lệ!");
            return null;
        }
        List<Table> tableList = orderDao.getTableByUserId(user_id);
        if(tableList == null || tableList.isEmpty()){
            System.out.println("Danh sách bàn rỗng!");
            return null;
        }
        return  tableList;
    }

    @Override
    public boolean addItemOrder(int order_id, Menu_Item item, int quantity, String note) {
        if (order_id <= 0 || quantity <= 0 || item == null) {
            System.out.println("Dữ liệu không hợp lệ!");
            return false;
        }
        return orderDao.createOrderItems(order_id, item, quantity, note);
    }


    @Override
    public List<Order_Item> getOrderItemWithStatus(OrderItemEnum orderItemEnum) {
        if(orderItemEnum == null) {
            System.out.println(Constant.VARIABLE_ERR_MGS);
            return null;
        }
        List<Order_Item> orderItemList = orderDao.getOrderItemWithStatus(orderItemEnum);
        if(orderItemList == null || orderItemList.isEmpty()){
            System.out.println("Danh sách dữ liệu trống!");
            return null;
        }
        return orderItemList;
    }

    public List<Order_Item> getOrderItemExcludeStatus() {
        List<Order_Item> orderItemList = orderDao.getOrderItemExcludesStatus();
        if(orderItemList == null || orderItemList.isEmpty()){
            System.out.println("Danh sách dữ liệu trống!");
            return null;
        }
        return orderItemList;
    }

    public OrderItemEnum getOrderItemStatus(int order_item_id) {
        if(order_item_id <= 0) {
            System.out.println(Constant.VARIABLE_ERR_MGS);
            return null;
        }
        return orderDao.getOrderItemStatus(order_item_id);
    }

    public boolean updateOrderItemStatus(Order_Item item, OrderItemEnum currentStatus) {
        if (item == null) {
            System.out.println(Constant.VARIABLE_ERR_MGS);
            return false;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.openConnection();
            conn.setAutoCommit(false);

            boolean success = false;

            switch (currentStatus) {
                case waiting:
                    success = orderDao.updateOrderItemStatus(conn, item.getId(), OrderItemEnum.pending);
                    break;
                case pending:
                    boolean isStatusUpdated = orderDao.updateOrderItemStatus(conn, item.getId(), OrderItemEnum.cooking);

                    double amountToAdd = item.getUnit_price() * item.getQuantity();

                    boolean isAmountUpdated = orderDao.updateOrderAmount(conn, item.getOrder().getId(), amountToAdd);

                    if (isStatusUpdated && isAmountUpdated) {
                        success = true;
                    }
                    break;

                case cooking:
                    success = orderDao.updateOrderItemStatus(conn, item.getId(), OrderItemEnum.ready);
                    break;

                case ready:
                    success = orderDao.updateOrderItemStatus(conn, item.getId(), OrderItemEnum.served);
                    break;

                default:
                    System.out.println("Trạng thái không hợp lệ để cập nhật!");
                    success = false;
            }

            if (success) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.out.println("Lỗi SQL");
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Lỗi SQL");
            }
        }
    }

    public boolean payOrder(Order order, LocalDateTime checkoutAt) {
        if(order == null) {
            System.out.println(Constant.VARIABLE_ERR_MGS);
            return false;
        }
        Connection conn = null;
        try {
            conn = DatabaseConnection.openConnection();

            conn.setAutoCommit(false);

            if(orderDao.updateOrderStatus(conn,order.getId(), OrderEnum.paid)) {
                if(TableDao.getInstance().updateTableStatus(conn,order.getTable().getId(), TableEnum.available)){
                    if(orderDao.updateCheckoutAt(conn,order, checkoutAt)) {
                        conn.commit();
                        return true;
                    }
                }
            }
            conn.rollback();
            return false;

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                return false;
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
