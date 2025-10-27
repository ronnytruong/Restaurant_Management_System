/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import static constant.CommonFunction.checkErrorSQL;
import db.DBContext;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Import;

/**
 *
 * @author TruongBinhTrong
 */
public class ImportDAO extends DBContext {

    public List<Import> getAll() {
        List<Import> list = new ArrayList<>();

        try {
            String query = "SELECT i.import_id, e.emp_name, s.contact_person, s.supplier_name, i.import_date "
                    + "FROM import i "
                    + "JOIN employee e ON e.emp_id = i.emp_id "
                    + "JOIN supplier s ON s.supplier_id = i.supplier_id	";

            ResultSet rs = this.executeSelectionQuery(query, null);

            while (rs.next()) {
                int importId = rs.getInt(1);
                String empName = rs.getString(2);
                String contactPerson = rs.getString(3);
                String supplierName = rs.getString(4);
                Date importDate = rs.getDate(5);

                Import imp = new Import(importId, empName, contactPerson, supplierName, importDate);

                list.add(imp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public List<Import> getImportDetails(int importId) {
        List<Import> list = new ArrayList<>();

        try {
            String query = "SELECT igd.ingredient_name, id.quantity, id.unit, id.unit_price, id.total_price "
                    + "FROM import i "
                    + "JOIN import_detail id ON i.import_id = id.import_id "
                    + "JOIN ingredient igd ON id.ingredient_id = igd.ingredient_id "
                    + "WHERE i.import_id = ?";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{importId});

            while (rs.next()) {
                String ingredientName = rs.getString(1);
                int quantity = rs.getInt(2);
                String unit = rs.getString(3);
                int unitPrice = rs.getInt(4);
                int totalPrice = rs.getInt(5);

                Import imp = new Import(ingredientName, quantity, unit, unitPrice, totalPrice);

                list.add(imp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public Import getElementByID(int id) {

        try {
            String query = "SELECT i.import_id, e.emp_name, s.contact_person, s.supplier_name, i.import_date "
                    + "FROM import i "
                    + "JOIN employee e ON e.emp_id = i.emp_id "
                    + "JOIN supplier s ON s.supplier_id = i.supplier_id  "
                    + "WHERE i.import_id = ?";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});

            if (rs.next()) {
                int importId = rs.getInt(1);
                String empName = rs.getString(2);
                String contactPerson = rs.getString(3);
                String supplierName = rs.getString(4);
                Date importDate = rs.getDate(5);

                return new Import(importId, empName, contactPerson, supplierName, importDate);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ImportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public int add(int supplierId, int empId) {
        try {
            String query = "INSERT INTO import (supplierId, empId, status) VALUES (? , ?, ?)";

            return this.executeQuery(query, new Object[]{supplierId, empId, "Active"});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(ImportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int addDetail(String ingredientName, int typeId, String status, int importId,
            int ingredientId, int quantity, String unit, int unitPrice, int totalPrice) {
        try {
            String query = "INSERT INTO ingredient (ingredient_name, type_id, status) "
                    + "VALUES (?, ?, ?) "
                    + "INSERT INTO import_detail (import_id, ingredient_id, quantity, unit, unit_price, total_price) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            return this.executeQuery(query, new Object[]{ingredientName, typeId, "Active", importId,
                ingredientId, quantity, unit, unitPrice, totalPrice});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(ImportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int edit(int categoryId, String categoryName, String description) {
        try {

            String query = "UPDATE category\n"
                    + "SET category_name = ?, description = ?\n"
                    + "WHERE  (category_id = ?)";

            return this.executeQuery(query, new Object[]{categoryName, description, categoryId});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(ImportDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int delete(int id) {
        try {
            String query = "UPDATE category\n"
                    + "SET status = 'Deleted'\n"
                    + "WHERE  (category_id = ?)";

            return this.executeQuery(query, new Object[]{id});

        } catch (SQLException ex) {

        }
        return -1;
    }

    public int countItem() {
        try {
            String query = "select count(import_id) as numrow from [dbo].[import]";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }
}
