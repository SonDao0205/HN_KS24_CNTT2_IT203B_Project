package com.restaurant.java.dao;

import com.restaurant.java.entity.*;
import com.restaurant.java.entity.enums.OrderEnum;
import com.restaurant.java.entity.enums.OrderItemEnum;
import com.restaurant.java.entity.enums.TableEnum;
import com.restaurant.java.entity.enums.UserRoleEnum;
import com.restaurant.java.utils.Constant;
import com.restaurant.java.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    private static OrderDao instance;

    private OrderDao() {
    }

    public static OrderDao getInstance() {
        if (instance == null) {
            instance = new OrderDao();
        }
        return instance;
    }

    public boolean createOrder(int user_id, int table_id) {
        String sql = "INSERT INTO Orders(customer_id,table_id) VALUES (?,?)";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, user_id);
            pstmt.setInt(2, table_id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi tạo đơn!");
            return false;
        }
    }

    public List<Table> getTableByUserId(int user_id) {
        String sql = """
                SELECT t.id,t.number,t.capacity,t.status
                FROM Orders o
                JOIN Tables t ON o.table_id = t.id
                WHERE o.customer_id = ? AND o.status = 'pending'; 
                """;
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();
            List<Table> tables = new ArrayList<>();
            while (rs.next()) {
                tables.add(new Table(
                        rs.getInt("id"),
                        rs.getString("number"),
                        rs.getInt("capacity"),
                        TableEnum.valueOf(rs.getString("status"))
                ));
            }
            return tables;
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy order!");
            return null;
        }
    }

    public Order getOrder(int user_id, int table_id) {
        String sql = """
                SELECT o.id as order_id, o.total_amount as total_amount, o.status as order_status, o.created_at as created_at, 
                       t.id as table_id, t.number as table_number, t.capacity as table_capacity, t.status as table_status, 
                       u.id as user_id, u.username as username, u.role as user_role, u.status as user_status
                  FROM Orders o
                  JOIN Tables t ON t.id = o.table_id
                  JOIN Users u ON u.id = o.customer_id
                  WHERE table_id = ? AND o.status = 'pending'
                """;
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, table_id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        UserRoleEnum.valueOf(rs.getString("user_role")),
                        rs.getBoolean("user_status")
                );

                Table table = new Table(
                        rs.getInt("table_id"),
                        rs.getString("table_number"),
                        rs.getInt("table_capacity"),
                        TableEnum.valueOf(rs.getString("table_status"))
                );
                return new Order(
                        rs.getInt("order_id"),
                        user,
                        table,
                        rs.getDouble("total_amount"),
                        OrderEnum.valueOf(rs.getString("order_status")),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
            }
            return null;
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy order!");
            return null;
        }
    }

    public boolean createOrderItems(int order_id, Menu_Item item, int quantity, String note) {
        String sql = "INSERT INTO Order_Items(order_id,menu_item,unit_price,quantity,note) VALUES ( ?, ?, ?, ?, ? )";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, order_id);
            pstmt.setInt(2, item.getId());
            pstmt.setDouble(3, item.getPrice());
            pstmt.setInt(4, quantity);
            pstmt.setString(5, note);

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm món!");
            return false;
        }
    }

    public boolean updateOrderAmount(Connection conn, int order_id, double amount) {
        String sql = """
                UPDATE Orders
                SET total_amount = total_amount + ?
                WHERE id = ?
                """;

        try (
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, order_id);

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật tổng tiền!");
            return false;
        }
    }

    public List<Order_Item> getOrderItems(int user_id, int table_id, Order item) {
        String sql = """
                 SELECT
                oi.id as od_id, oi.menu_item as od_item, oi.unit_price as od_price, oi.quantity as od_quantity, oi.status as od_status , oi.note as od_note,
                m.id as m_id, m.name as m_name, m.price as m_price, m.status as m_status
                FROM Orders o
                JOIN Order_Items oi ON oi.order_id = o.id
                JOIN Menu_Items m ON m.id = oi.menu_item
                WHERE o.id = ? AND o.customer_id = ? AND o.table_id = ? AND o.status != 'cancel' AND oi.status != 'cancel'""";

        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, item.getId());
            pstmt.setInt(2, user_id);
            pstmt.setInt(3, table_id);
            ResultSet rs = pstmt.executeQuery();
            List<Order_Item> order_items = new ArrayList<>();
            while (rs.next()) {
                Menu_Item menu_item = new Menu_Item(
                        rs.getInt("m_id"),
                        null,
                        rs.getString("m_name"),
                        rs.getDouble("m_price"),
                        rs.getBoolean("m_status")
                );

                order_items.add(new Order_Item(
                        rs.getInt("od_id"),
                        item,
                        menu_item,
                        rs.getDouble("od_price"),
                        rs.getInt("od_quantity"),
                        OrderItemEnum.valueOf(rs.getString("od_status")),
                        rs.getString("od_note")
                ));
            }
            return order_items;
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy chi tiết order");
            return null;
        }
    }

    public List<Order_Item> getAllOrderItems(int table_id, Order item) {
        String sql = """
                 SELECT
                oi.id as od_id, oi.menu_item as od_item, oi.unit_price as od_price, oi.quantity as od_quantity, oi.status as od_status , oi.note as od_note,
                m.id as m_id, m.name as m_name, m.price as m_price, m.status as m_status
                FROM Orders o
                JOIN Order_Items oi ON oi.order_id = o.id
                JOIN Menu_Items m ON m.id = oi.menu_item
                WHERE o.table_id = ? AND oi.status = 'waiting'""";

        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, table_id);
            ResultSet rs = pstmt.executeQuery();
            List<Order_Item> order_items = new ArrayList<>();
            while (rs.next()) {
                Menu_Item menu_item = new Menu_Item(
                        rs.getInt("m_id"),
                        null,
                        rs.getString("m_name"),
                        rs.getDouble("m_price"),
                        rs.getBoolean("m_status")
                );

                order_items.add(new Order_Item(
                        rs.getInt("od_id"),
                        item,
                        menu_item,
                        rs.getDouble("od_price"),
                        rs.getInt("od_quantity"),
                        OrderItemEnum.valueOf(rs.getString("od_status")),
                        rs.getString("od_note")
                ));
            }
            return order_items;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi lấy chi tiết order");
            return null;
        }
    }

    public OrderItemEnum getOrderItemStatus(int order_item_id) {
        String sql = """
                SELECT *
                FROM Order_Items
                WHERE id = ?""";

        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, order_item_id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return OrderItemEnum.valueOf(rs.getString("status"));
            }
            return null;
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy trạng thái!");
            return null;
        }
    }

    public boolean cancelOrderItem(int order_item_id) {
        String sql = """
                UPDATE Order_Items
                SET status = 'cancel'
                WHERE id = ? AND status = 'pending'""";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, order_item_id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi khi huỷ món!");
            return false;
        }
    }

    public List<Order_Item> getOrderItemWithStatus(OrderItemEnum statusFilter) {
        List<Order_Item> list = new ArrayList<>();
        String sql = """
                SELECT 
                    oi.id AS oi_id, oi.unit_price AS oi_price, oi.quantity AS oi_quantity, 
                    oi.status AS oi_status, oi.note AS oi_note,
                    o.id AS o_id,
                    m.id AS m_id, m.name AS m_name, m.price AS m_price, m.status AS m_status
                FROM Order_Items oi
                JOIN Orders o ON oi.order_id = o.id
                JOIN Menu_Items m ON oi.menu_item = m.id
                WHERE oi.status = ?
                """;

        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, statusFilter.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = new Order(rs.getInt("o_id"), null, null, 0, null, null);

                Menu_Item menuItem = new Menu_Item(
                        rs.getInt("m_id"),
                        rs.getString("m_name"),
                        rs.getDouble("m_price"),
                        rs.getBoolean("m_status")
                );

                Order_Item orderItem = new Order_Item(
                        rs.getInt("oi_id"),
                        order,
                        menuItem,
                        rs.getDouble("oi_price"),
                        rs.getInt("oi_quantity"),
                        OrderItemEnum.valueOf(rs.getString("oi_status")),
                        rs.getString("oi_note")
                );

                list.add(orderItem);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách Order Item theo trạng thái!");
            return null;
        }
        return list;
    }

    public List<Order_Item> getOrderItemExcludesStatus() {
        List<Order_Item> list = new ArrayList<>();
        String sql = """
                SELECT 
                    oi.id AS oi_id, oi.unit_price AS oi_price, oi.quantity AS oi_quantity, 
                    oi.status AS oi_status, oi.note AS oi_note,
                    o.id AS o_id,
                    m.id AS m_id, m.name AS m_name, m.price AS m_price, m.status AS m_status
                FROM Order_Items oi
                JOIN Orders o ON oi.order_id = o.id
                JOIN Menu_Items m ON oi.menu_item = m.id
                WHERE oi.status <> ? AND oi.status <> ? AND oi.status <> ?
                """;

        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, OrderItemEnum.cancel.name());
            pstmt.setString(2, OrderItemEnum.waiting.name());
            pstmt.setString(3, OrderItemEnum.served.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = new Order(rs.getInt("o_id"), null, null, 0, null, null);

                Menu_Item menuItem = new Menu_Item(
                        rs.getInt("m_id"),
                        rs.getString("m_name"),
                        rs.getDouble("m_price"),
                        rs.getBoolean("m_status")
                );

                Order_Item orderItem = new Order_Item(
                        rs.getInt("oi_id"),
                        order,
                        menuItem,
                        rs.getDouble("oi_price"),
                        rs.getInt("oi_quantity"),
                        OrderItemEnum.valueOf(rs.getString("oi_status")),
                        rs.getString("oi_note")
                );

                list.add(orderItem);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách Order Item theo trạng thái!");
            return null;
        }
        return list;
    }

    public boolean updateOrderItemStatus(Connection conn, int order_item_id, OrderItemEnum status) {
        String sql = """
                UPDATE Order_Items
                SET status = ?
                WHERE id = ? """;

        try (
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, status.name());
            pstmt.setInt(2, order_item_id);

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật trạng thái đơn!");
            return false;
        }
    }

    public boolean updateOrderStatus(Connection conn, int order_id, OrderEnum status) {
        String sql = """
                UPDATE Orders
                SET status = ?
                WHERE id = ? """;
        try (
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, status.name());
            pstmt.setInt(2, order_id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(Constant.RED_CODE + "Lỗi khi cập nhật trạng thái Order" + Constant.RESET_CODE);
            return false;
        }
    }

    public boolean updateCheckoutAt(Connection conn,Order order, LocalDateTime checkoutAt) {
        String sql = """
                UPDATE Orders
                SET checkout_at = ?
                WHERE id = ? """;
        try (
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setTimestamp(1, Timestamp.valueOf(checkoutAt));
            pstmt.setInt(2, order.getId());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật giờ checkout!");
            return false;
        }
    }
}

