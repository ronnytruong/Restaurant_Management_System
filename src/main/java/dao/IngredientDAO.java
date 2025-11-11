/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package dao;

import static constant.CommonFunction.checkErrorSQL;
import static constant.Constants.MAX_ELEMENTS_PER_PAGE;
import db.DBContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Ingredient;

/**
 *
 * @author TruongBinhTrong
 */
public class IngredientDAO extends DBContext {

    public List<Ingredient> getAll() {
        List<Ingredient> list = new ArrayList<>();

        try {
            String query = "SELECT\n"
                    + "    i.ingredient_id,\n"
                    + "    i.ingredient_name,\n"
                    + "    t.type_name,\n"
                    + "	i.unit,\n"
                    + "    COALESCE(SUM(im.quantity), 0) AS TotalQuantity,\n"
                    + "	i.status\n"
                    + "FROM ingredient i\n"
                    + "JOIN type t \n"
                    + "    ON i.type_id = t.type_id\n"
                    + "LEFT JOIN import_detail im \n"
                    + "    ON im.ingredient_id = i.ingredient_id\n"
                    + "WHERE LOWER(i.status) != LOWER(N'Deleted')\n"
                    + "GROUP BY\n"
                    + "    i.ingredient_id,\n"
                    + "    i.ingredient_name,\n"
                    + "    t.type_name,\n"
                    + "	i.unit,\n"
                    + "    i.status\n"
                    + "ORDER BY \n"
                    + "    i.ingredient_id\n";

            ResultSet rs = this.executeSelectionQuery(query, null);

            while (rs.next()) {
                Ingredient ing = new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getString("type_name"),
                        rs.getString("unit"),
                        rs.getInt("TotalQuantity"),
                        rs.getString("status")
                );

                list.add(ing);
            }
        } catch (SQLException ex) {
            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public List<Ingredient> getAll(int page, String keyword) {
        List<Ingredient> list = new ArrayList<>();

        try {
            String query
                    = "SELECT i.ingredient_id, i.ingredient_name, t.type_name, "
                    + "i.unit, COALESCE(SUM(im.quantity), 0) AS TotalQuantity, i.status "
                    + "FROM ingredient i "
                    + "JOIN type t ON i.type_id = t.type_id "
                    + "LEFT JOIN import_detail im ON im.ingredient_id = i.ingredient_id "
                    + "WHERE LOWER(i.status) != LOWER('Deleted') "
                    + "  AND LOWER(i.ingredient_name) LIKE LOWER(?) "
                    + "GROUP BY i.ingredient_id, i.ingredient_name, t.type_name, i.unit, i.status "
                    + "ORDER BY i.ingredient_id "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

            keyword = "%" + keyword + "%";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{keyword, (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});

            while (rs.next()) {
                Ingredient ing = new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getString("type_name"),
                        rs.getString("unit"),
                        rs.getInt("TotalQuantity"),
                        rs.getString("status")
                );

                list.add(ing);
            }
        } catch (SQLException ex) {
            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public Ingredient getElementByID(int id) {

        try {
            String query = "SELECT i.ingredient_id, i.ingredient_name, i.unit, i.type_id, t.type_name, i.price, i.status\n"
                    + "FROM ingredient AS i\n"
                    + "LEFT JOIN type AS t ON i.type_id = t.type_id\n"
                    + "WHERE (i.ingredient_id = ? and LOWER(i.status) != LOWER(N'Deleted'))\n";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});

            while (rs.next()) {

                Ingredient ing = new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getString("unit"),
                        rs.getInt("type_id"),
                        rs.getString("type_name"),
                        rs.getString("status")
                );

                return ing;
            }
        } catch (SQLException ex) {
            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public int getLastId() {

        try {
            String query = "SELECT TOP (1) i.ingredient_id FROM ingredient i ORDER BY i.ingredient_id DESC";

            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt("ingredient_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return -1;
    }

    public int add(String ingredient_name, int type_id, String unit) {

        try {
            String query = "INSERT INTO ingredient (ingredient_name, type_id, unit, status)\n"
                    + "VALUES (?, ?, ?, ?)";

            return this.executeQuery(query, new Object[]{ingredient_name, type_id, unit, "Active"});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int edit(int ingredient_id, String ingredient_name, int type_id, String unit){
        try {

            String query = "UPDATE ingredient\n"
                    + "SET ingredient_name = ?, type_id = ?, unit = ?\n"
                    + "WHERE  (ingredient_id = ?)";

            return this.executeQuery(query, new Object[]{ingredient_name, type_id, unit, ingredient_id});

        } catch (SQLException ex) {

            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }

            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int delete(int id) {
        try {
            String query = "UPDATE ingredient\n"
                    + "SET status = 'Deleted'\n"
                    + "WHERE  (ingredient_id = ?)";

            return this.executeQuery(query, new Object[]{id});

        } catch (SQLException ex) {
            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int countItem() {
        try {
            String query = "select count(ingredient_id) as numrow from [dbo].[ingredient] where LOWER(status) != LOWER(N'Deleted')";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return 0;
    }

    public List<Ingredient> search(String keyword) {
        List<Ingredient> list = new ArrayList<>();
        try {
            String query = "SELECT i.ingredient_id, i.ingredient_name, i.unit, i.type_id, t.type_name, i.status "
                    + "FROM ingredient AS i "
                    + "LEFT JOIN type AS t ON i.type_id = t.type_id "
                    + "WHERE LOWER(i.status) != LOWER(N'Deleted') "
                    + "AND (CAST(i.ingredient_id AS VARCHAR) LIKE ? OR LOWER(i.ingredient_name) LIKE ?) "
                    + "ORDER BY i.ingredient_id";

            String searchKeyword = "%" + keyword.toLowerCase() + "%";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{searchKeyword, searchKeyword});

            while (rs.next()) {
                Ingredient ing = new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getString("unit"),
                        rs.getInt("type_id"),
                        rs.getString("type_name"),
                        rs.getString("status")
                );
                list.add(ing);
            }

        } catch (SQLException ex) {
            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

}
