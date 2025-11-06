/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import db.DBContext;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import model.Order;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
public class OrderDAO extends DBContext {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final VoucherDAO voucherDAO = new VoucherDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();

    public List<Order> getAll() {

        List<Order> list = new ArrayList<>();

        try {
            String query = "SELECT order_id, reservation_id, emp_id, voucher_id, order_date, order_time, payment_method, status\n"
                    + "FROM     [order]\n"
                    + "WHERE  (LOWER(status) <> LOWER('Deleted'))\n"
                    + "ORDER BY order_date DESC";

            ResultSet rs = this.executeSelectionQuery(query, null);

            while (rs.next()) {
                int orderId = rs.getInt(1);
                int reservationId = rs.getInt(2);
                int empId = rs.getInt(3);
                int voucherId = rs.getInt(4);
                Date orderDate = rs.getDate(5);
                Time orderTime = rs.getTime(6);
                String paymentMethod = rs.getString(7);
                String status = rs.getString(8);

                Order order = new Order(orderId, reservationDAO.getElementByID(reservationId),
                        employeeDAO.getElementByID(empId), voucherDAO.getById(voucherId),
                        orderDate, orderTime, paymentMethod, status);

                list.add(order);
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load list");
        }

        return list;
    }

    public List<Order> getAll(int page, int maxElement) {

        List<Order> list = new ArrayList<>();

        try {
            String query = "SELECT order_id, reservation_id, emp_id, voucher_id, order_date, order_time, payment_method, status\n"
                    + "FROM     [order]\n"
                    + "WHERE  (LOWER(status) <> LOWER('Deleted'))\n"
                    + "ORDER BY order_date desc, order_time desc\n"
                    + "OFFSET ? ROWS \n"
                    + "FETCH NEXT ? ROWS ONLY;";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{(page - 1) * maxElement, maxElement});

            while (rs.next()) {
                int orderId = rs.getInt(1);
                int reservationId = rs.getInt(2);
                int empId = rs.getInt(3);
                int voucherId = rs.getInt(4);
                Date orderDate = rs.getDate(5);
                Time orderTime = rs.getTime(6);
                String paymentMethod = rs.getString(7);
                String status = rs.getString(8);

                Order order = new Order(orderId, reservationDAO.getElementByID(reservationId),
                        employeeDAO.getElementByID(empId), voucherDAO.getById(voucherId),
                        orderDate, orderTime, paymentMethod, status);

                list.add(order);
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load list");
        }

        return list;
    }

    public Order getElementByID(int id) {

        try {
            String query = "SELECT order_id, reservation_id, emp_id, voucher_id, order_date, order_time, payment_method, status\n"
                    + "FROM     [order]\n"
                    + "WHERE  (LOWER(status) <> LOWER('Deleted') and order_id = ?)\n"
                    + "ORDER BY order_id\n";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});

            while (rs.next()) {
                int orderId = rs.getInt(1);
                int reservationId = rs.getInt(2);
                int empId = rs.getInt(3);
                int voucherId = rs.getInt(4);
                Date orderDate = rs.getDate(5);
                Time orderTime = rs.getTime(6);
                String paymentMethod = rs.getString(7);
                String status = rs.getString(8);

                Order order = new Order(orderId, reservationDAO.getElementByID(reservationId),
                        employeeDAO.getElementByID(empId), voucherDAO.getById(voucherId),
                        orderDate, orderTime, paymentMethod, status);

                return order;
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load object");
        }

        return null;
    }

    public int add(int reservationId, int empId, Integer voucherId, String paymentMethod) {
        try {
            String query = "INSERT INTO [order]\n"
                    + "(reservation_id, emp_id, voucher_id, order_date, order_time, payment_method, status)\n"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            return this.executeQuery(query, new Object[]{reservationId, empId, voucherId,
                new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()), paymentMethod, "Pending"});

        } catch (SQLException ex) {
            System.out.println("Can't not add object");
        }
        return -1;
    }

    public int edit(int orderId, int reservationId, int empId, Integer voucherId, String paymentMethod) {
        try {

            String query = "UPDATE [order]\n"
                    + "SET reservation_id = ?, emp_id = ?, voucher_id = ?, payment_method = ?\n"
                    + "WHERE  (order_id = ?)";

            return this.executeQuery(query, new Object[]{reservationId, empId, voucherId, paymentMethod, orderId});

        } catch (SQLException ex) {
            System.out.println("Can't not edit object");
        }
        return -1;
    }

    public int delete(int id) {
        try {
            String query = "UPDATE [order]\n"
                    + "SET status = 'Deleted'\n"
                    + "WHERE  (order_id = ?)";

            return this.executeQuery(query, new Object[]{id});

        } catch (SQLException ex) {

        }
        return -1;
    }

    public int countItem() {
        try {
            String query = "SELECT COUNT(order_id) AS numrow\n"
                    + "FROM     [order]\n"
                    + "WHERE  (LOWER(status) <> LOWER(N'Deleted'))";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }

    public int approve(int id) {
        try {
            String query = "UPDATE [order]\n"
                    + "SET status = 'Approved'\n"
                    + "WHERE  (order_id = ?)";
            return this.executeQuery(query, new Object[]{id});
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }

    public int reject(int id) {
        try {
            String query = "UPDATE [order]\n"
                    + "SET status = 'Rejected'\n"
                    + "WHERE  (order_id = ?)";
            return this.executeQuery(query, new Object[]{id});
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }

    public int complete(int id) {
        try {
            String query = "UPDATE [order]\n"
                    + "SET status = 'Completed'\n"
                    + "WHERE  (order_id = ?)";
            return this.executeQuery(query, new Object[]{id});
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }

    public long getTotalPricebyOrderId(int id) {
        int sum = 0;
        try {
            String query = "SELECT [unit_price]\n"
                    + "      ,[quantity]\n"
                    + "  FROM [RestaurantManagement].[dbo].[order_item]\n"
                    + "  where order_id = ?";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});
            while (rs.next()) {
                int unitPrice = rs.getInt(1);
                int quantity = rs.getInt(2);

                sum += unitPrice * quantity;
            }

            Order order = getElementByID(id);
            long discount = 0;

            if (order.getVoucher() != null) {
                if (order.getVoucher().getDiscountType().equalsIgnoreCase("percent")) {
                    discount = order.getVoucher().getDiscountValue() / 100 * sum;
                } else {
                    discount = order.getVoucher().getDiscountValue();
                }
                if (discount > sum) {
                    return 0;
                }
                return sum - discount;
            } else {
                return sum;
            }
        } catch (SQLException ex) {
            return -1;
        }
    }
}
