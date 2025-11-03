/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import static constant.CommonFunction.checkErrorSQL;
import static constant.Constants.MAX_ELEMENTS_PER_PAGE;
import db.DBContext;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Customer;
import model.Reservation;
import model.Table;

/**
 * @author You
 */
public class ReservationDAO extends DBContext {

    public Reservation getElementByTableId(int id) {
        try {
            String sql = "SELECT r.reservation_id, r.customer_id, r.table_id, r.reservation_date, r.reservation_time, r.party_size, r.status\n"
                    + "FROM     reservation AS r INNER JOIN\n"
                    + "                  [table] AS t ON r.table_id = t.table_id\n"
                    + "WHERE  (t.table_id = ?) AND (LOWER(r.status) = LOWER('Seated'))";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{id});
            if (rs.next()) {
                return extract(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Reservation> getAllSeated() {
        List<Reservation> list = new ArrayList<>();

        try {
            String sql = "SELECT reservation_id, customer_id, table_id, reservation_date, reservation_time, party_size, status\n"
                    + "FROM     reservation\n"
                    + "WHERE  (LOWER(status) = LOWER('Seated'))\n"
                    + "ORDER BY reservation_id";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{});

            while (rs.next()) {
                list.add(extract(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /* ===================== LIST (ADMIN) ===================== */
    public List<Reservation> getAll(int page, String keyword) {
        List<Reservation> list = new ArrayList<>();
        if (keyword == null) {
            keyword = "";
        }
        String kw = "%" + keyword + "%";

        try {
            String sql = "SELECT r.reservation_id, r.customer_id, r.table_id, "
                    + "r.reservation_date, r.reservation_time, r.party_size, r.status "
                    + "FROM reservation AS r "
                    + "WHERE (CAST(r.reservation_id AS VARCHAR) LIKE ? OR "
                    + "       CAST(r.customer_id AS VARCHAR) LIKE ? OR "
                    + "       CAST(r.table_id    AS VARCHAR) LIKE ? OR "
                    + "       LOWER(r.status) LIKE LOWER(?)) "
                    + "ORDER BY r.reservation_id "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            ResultSet rs = this.executeSelectionQuery(sql,
                    new Object[]{kw, kw, kw, kw, (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});

            while (rs.next()) {
                list.add(extract(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int countItem(String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        String kw = "%" + keyword + "%";
        try {
            String sql = "SELECT COUNT(*) "
                    + "FROM reservation AS r "
                    + "WHERE (CAST(r.reservation_id AS VARCHAR) LIKE ? OR "
                    + "       CAST(r.customer_id AS VARCHAR) LIKE ? OR "
                    + "       CAST(r.table_id    AS VARCHAR) LIKE ? OR "
                    + "       LOWER(r.status) LIKE LOWER(?))";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{kw, kw, kw, kw});
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /* ===================== LIST (CUSTOMER) ===================== */
    public List<Reservation> getByCustomer(int customerId, int page, String keyword) {
        List<Reservation> list = new ArrayList<>();
        if (keyword == null) {
            keyword = "";
        }
        String kw = "%" + keyword + "%";
        try {
            String sql = "SELECT r.reservation_id, r.customer_id, r.table_id, "
                    + "r.reservation_date, r.reservation_time, r.party_size, r.status "
                    + "FROM reservation AS r "
                    + "WHERE r.customer_id = ? "
                    + "AND (CAST(r.table_id AS VARCHAR) LIKE ? OR LOWER(r.status) LIKE LOWER(?)) "
                    + "ORDER BY r.reservation_id "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
            ResultSet rs = this.executeSelectionQuery(sql,
                    new Object[]{customerId, kw, kw, (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});

            while (rs.next()) {
                list.add(extract(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int countByCustomer(int customerId, String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        String kw = "%" + keyword + "%";
        try {
            String sql = "SELECT COUNT(*) FROM reservation r "
                    + "WHERE r.customer_id = ? "
                    + "AND (CAST(r.table_id AS VARCHAR) LIKE ? OR LOWER(r.status) LIKE LOWER(?))";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{customerId, kw, kw});
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /* ===================== CRUD / STATUS ===================== */
    public Reservation getElementByID(int id) {
        try {
            String sql = "SELECT r.reservation_id, r.customer_id, r.table_id, "
                    + "r.reservation_date, r.reservation_time, r.party_size, r.status "
                    + "FROM reservation r WHERE r.reservation_id = ?";
            ResultSet rs = this.executeSelectionQuery(sql, new Object[]{id});
            if (rs.next()) {
                return extract(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int add(int customerId, int tableId, Date date, Time time, int partySize) {
        try {
            String sql = "INSERT INTO reservation "
                    + "(customer_id, table_id, reservation_date, reservation_time, party_size, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            return this.executeQuery(sql, new Object[]{customerId, tableId, date, time, partySize, "Pending"});
        } catch (SQLException ex) {
            int err = checkErrorSQL(ex);
            if (err != 0) {
                return err;
            }
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int edit(int reservationId, int tableId, Date date, Time time, int partySize, String status) {
        try {
            String sql = "UPDATE reservation "
                    + "SET table_id = ?, reservation_date = ?, reservation_time = ?, party_size = ?, status = ? "
                    + "WHERE reservation_id = ?";
            return this.executeQuery(sql, new Object[]{tableId, date, time, partySize, status, reservationId});
        } catch (SQLException ex) {
            int err = checkErrorSQL(ex);
            if (err != 0) {
                return err;
            }
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /**
     * Approve / Reject / Pending / Cancelled …
     */
    public int updateStatus(int id, String status) {
        try {
            String sql = "UPDATE reservation SET status = ? WHERE reservation_id = ?";
            return this.executeQuery(sql, new Object[]{status, id});
        } catch (SQLException ex) {
            int err = checkErrorSQL(ex);
            if (err != 0) {
                return err;
            }
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /**
     * Customer “Cancel My Reservation” -> đổi sang Cancelled thay vì xóa vật lý
     */
    public int cancelByCustomer(int id, int customerId) {
        try {
            String sql = "UPDATE reservation SET status = 'Cancelled' "
                    + "WHERE reservation_id = ? AND customer_id = ?";
            return this.executeQuery(sql, new Object[]{id, customerId});
        } catch (SQLException ex) {
            int err = checkErrorSQL(ex);
            if (err != 0) {
                return err;
            }
            Logger.getLogger(ReservationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /* ===================== helper ===================== */
    private Reservation extract(ResultSet rs) throws SQLException {
        int id = rs.getInt("reservation_id");
        int customerId = rs.getInt("customer_id");
        int tableId = rs.getInt("table_id");
        Date date = rs.getDate("reservation_date");
        Time time = rs.getTime("reservation_time");
        int party = rs.getInt("party_size");
        String status = rs.getString("status");

        // Gọi các DAO để lấy object
        CustomerDAO customerDAO = new CustomerDAO();
        TableDAO tableDAO = new TableDAO();

        Customer customer = customerDAO.getElementByID(customerId);
        Table table = tableDAO.getElementByID(tableId);

        return new Reservation(id, customer, table, date, time, party, status);
    }

}
