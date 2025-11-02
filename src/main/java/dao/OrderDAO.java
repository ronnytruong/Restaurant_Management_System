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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Order;
import model.Reservation;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
public class OrderDAO extends DBContext {

    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private VoucherDAO voucherDAO = new VoucherDAO();
    private ReservationDAO reservationDAO = new ReservationDAO();

    public int updateStatus() {
        try {
            String query = "UPDATE o\n"
                    + "SET o.status = 'Deleted'\n"
                    + "FROM [order] AS o INNER JOIN\n"
                    + "reservation AS r ON o.reservation_id = r.reservation_id INNER JOIN\n"
                    + "employee AS e ON o.emp_id = e.emp_id\n"
                    + "WHERE  (r.status = 'Deleted') AND (e.status = 'Deleted')";

            return this.executeQuery(query, new Object[]{});

        } catch (SQLException e) {
            System.out.println("Can't update status");
        }

        return -1;
    }

    public List<Order> getAll() {
        updateStatus();

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
        updateStatus();

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

//    public List<order> getAllSeated(int page, String keyword) {
//        List<Role> list = new ArrayList<>();
//
//        try {
//            String query = "SELECT role_id, role_name, description, status\n"
//                    + "FROM     role\n"
//                    + "WHERE  (LOWER(status) <> LOWER(N'Deleted'))\n"
//                    + "AND (LOWER(role_name) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?))\n"
//                    + "ORDER BY role_id\n"
//                    + "OFFSET ? ROWS \n"
//                    + "FETCH NEXT ? ROWS ONLY;";
//
//            keyword = "%" + keyword + "%";
//
//            ResultSet rs = this.executeSelectionQuery(query, new Object[]{keyword, keyword, (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});
//
//            while (rs.next()) {
//                int id = rs.getInt(1);
//                String name = rs.getString(2);
//                String description = rs.getString(3);
//                String status = rs.getString(4);
//
//                Role role = new Role(id, name, description, status);
//
//                list.add(role);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return list;
//    }
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

    public Order getElementByCustomerID(int customerId) {
//
//        try {
//            String query = "SELECT order_id, reservation_id, emp_id, voucher_id, order_date, order_time, payment_method, status\n"
//                    + "FROM     [order]\n"
//                    + "WHERE  (LOWER(status) <> LOWER('Deleted') and order_id = ?)\n"
//                    + "ORDER BY order_id\n";
//
//            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});
//
//            while (rs.next()) {
//                int orderId = rs.getInt(1);
//                int reservationId = rs.getInt(2);
//                int empId = rs.getInt(3);
//                int voucherId = rs.getInt(4);
//                Date orderDate = rs.getDate(5);
//                Time orderTime = rs.getTime(6);
//                String paymentMethod = rs.getString(7);
//                String status = rs.getString(8);
//
//                Order order = new Order(orderId, reservationDAO.getElementByID(reservationId),
//                        employeeDAO.getElementByID(empId), voucherDAO.getById(voucherId),
//                        orderDate, orderTime, paymentMethod, status);
//
//                return order;
//            }
//        } catch (SQLException ex) {
//            System.out.println("Can't not load object");
//        }
//
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

    public int edit(int orderId, int reservationId, int empId, int voucherId, String paymentMethod) {
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
}
