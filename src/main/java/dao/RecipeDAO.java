/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import static constant.CommonFunction.*;
import static constant.Constants.*;
import db.DBContext;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Recipe;
import model.RecipeItem;

/**
 *
 * @author PHAT
 */
public class RecipeDAO extends DBContext {

    public List<Recipe> getAll() {
        List<Recipe> list = new ArrayList<>();
        try {
            String query = "SELECT recipe_id, menu_item_id, status FROM recipe WHERE (LOWER(status) <> LOWER(N'Deleted')) ORDER BY recipe_id";
            ResultSet rs = this.executeSelectionQuery(query, null);
            while (rs.next()) {
                int id = rs.getInt("recipe_id");
                int menuItemId = rs.getInt("menu_item_id");
                String status = rs.getString("status");
                Recipe r = new Recipe(id, menuItemId, status);
                list.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<Recipe> getAll(int page) {
        List<Recipe> list = new ArrayList<>();
        try {
            String query = "SELECT recipe_id, menu_item_id, status FROM recipe WHERE (LOWER(status) <> LOWER(N'Deleted')) ORDER BY recipe_id "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{(page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});
            while (rs.next()) {
                int id = rs.getInt("recipe_id");
                int menuItemId = rs.getInt("menu_item_id");
                String status = rs.getString("status");
                Recipe r = new Recipe(id, menuItemId, status);
                list.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Search by keyword (áp dụng lên recipe_id và menu_item_id dưới dạng
     * string)
     */
    public List<Recipe> getAll(int page, String keyword) {
        List<Recipe> list = new ArrayList<>();
        try {
            String query = "SELECT recipe_id, menu_item_id, status FROM recipe "
                    + "WHERE (LOWER(status) <> LOWER(N'Deleted')) "
                    + "AND (CAST(recipe_id AS VARCHAR) LIKE ? OR CAST(menu_item_id AS VARCHAR) LIKE ?) "
                    + "ORDER BY recipe_id "
                    + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";
            keyword = "%" + keyword + "%";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{keyword, keyword, (page - 1) * MAX_ELEMENTS_PER_PAGE, MAX_ELEMENTS_PER_PAGE});
            while (rs.next()) {
                int id = rs.getInt("recipe_id");
                int menuItemId = rs.getInt("menu_item_id");
                String status = rs.getString("status");
                Recipe r = new Recipe(id, menuItemId, status);
                list.add(r);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public Recipe getElementByID(int id) {
        try {
            String query = "SELECT recipe_id, menu_item_id, status FROM recipe WHERE (recipe_id = ? and LOWER(status) <> LOWER(N'Deleted'))";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{id});
            if (rs.next()) {
                int menuItemId = rs.getInt("menu_item_id");
                String status = rs.getString("status");
                Recipe r = new Recipe(id, menuItemId, status);
                // lấy recipe items
                r.setItems(getItemsByRecipeId(id));
                return r;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int add(int menuItemId) {
        try {
            String query = "INSERT INTO recipe (menu_item_id, status) VALUES (?, ?)";
            return this.executeQuery(query, new Object[]{menuItemId, "Active"});
        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int edit(int id, int menuItemId, String status) {
        try {
            String query = "UPDATE recipe SET menu_item_id = ?, status = ? WHERE (recipe_id = ?)";
            return this.executeQuery(query, new Object[]{menuItemId, status, id});
        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int delete(int id) {
        try {
            String query = "UPDATE recipe SET status = 'Deleted' WHERE (recipe_id = ?)";
            return this.executeQuery(query, new Object[]{id});
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int countItem() {
        try {
            String query = "SELECT COUNT(recipe_id) AS numrow FROM recipe WHERE (LOWER(status) <> LOWER(N'Deleted'))";
            ResultSet rs = this.executeSelectionQuery(query, null);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error");
        }
        return 0;
    }

    // --- RECIPE_ITEM CRUD ---
    public List<RecipeItem> getItemsByRecipeId(int recipeId) {
        List<RecipeItem> list = new ArrayList<>();
        try {
            // join ingredient để có tên nguyên liệu hiển thị (chỉ SELECT)
            String query = "SELECT ri.recipe_item_id, ri.recipe_id, ri.ingredient_id, ri.quantity, ri.unit, ri.note, ri.status, ig.ingredient_name "
                    + "FROM recipe_item ri LEFT JOIN ingredient ig ON ri.ingredient_id = ig.ingredient_id "
                    + "WHERE ri.recipe_id = ? AND (LOWER(ri.status) <> LOWER(N'Deleted')) ORDER BY ri.recipe_item_id";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{recipeId});
            while (rs.next()) {
                int id = rs.getInt("recipe_item_id");
                int rId = rs.getInt("recipe_id");
                int ingId = rs.getInt("ingredient_id");
                double qty = rs.getDouble("quantity");
                String unit = rs.getString("unit");
                String note = rs.getString("note");
                String status = rs.getString("status");
                String ingredientName = null;
                try {
                    ingredientName = rs.getString("ingredient_name");
                } catch (SQLException ex) {
                }
                RecipeItem item = new RecipeItem(id, rId, ingId, qty, unit, note, status);
                item.setIngredientName(ingredientName);
                list.add(item);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    /**
     * Thêm item vào recipe. Trước khi insert: kiểm tra ingredient tồn tại và
     * chưa bị Deleted (bảng ingredient thuộc team khác => chỉ kiểm tra). Nếu
     * ingredient không hợp lệ trả về -2.
     */
    public int addItem(int recipeId, int ingredientId, double quantity, String unit, String note) {
        try {
            // validate ingredient exists and not Deleted (read-only)
            if (!ingredientValidForUse(ingredientId)) {
                return -2; // custom error: ingredient invalid / không tồn tại hoặc đã bị Deleted
            }
            String query = "INSERT INTO recipe_item (recipe_id, ingredient_id, quantity, unit, note, status) VALUES (?, ?, ?, ?, ?, ?)";
            return this.executeQuery(query, new Object[]{recipeId, ingredientId, quantity, unit, note, "Active"});
        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /**
     * Sửa recipe_item, trước đó kiểm tra ingredient hợp lệ (read-only check).
     * Nếu ingredient không hợp lệ trả về -2.
     */
    public int editItem(int recipeItemId, int ingredientId, double quantity, String unit, String note, String status) {
        try {
            if (!ingredientValidForUse(ingredientId)) {
                return -2;
            }
            String query = "UPDATE recipe_item SET ingredient_id = ?, quantity = ?, unit = ?, note = ?, status = ? WHERE recipe_item_id = ?";
            return this.executeQuery(query, new Object[]{ingredientId, quantity, unit, note, status, recipeItemId});
        } catch (SQLException ex) {
            int sqlError = checkErrorSQL(ex);
            if (sqlError != 0) {
                return sqlError;
            }
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int deleteItem(int recipeItemId) {
        try {
            String query = "UPDATE recipe_item SET status = 'Deleted' WHERE (recipe_item_id = ?)";
            return this.executeQuery(query, new Object[]{recipeItemId});
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    // ---- Helpers liên quan ingredient (READ-ONLY vì ingredient thuộc team khác) ----
    /**
     * Kiểm tra ingredient tồn tại và không bị Deleted. Trả về true nếu tồn tại
     * và status <> 'Deleted'
     */
    public boolean ingredientValidForUse(int ingredientId) {
        try {
            String query = "SELECT status FROM ingredient WHERE ingredient_id = ?";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{ingredientId});
            if (rs.next()) {
                String status = rs.getString("status");
                return status == null || !"Deleted".equalsIgnoreCase(status);
            } else {
                return false; // không tìm thấy ingredient
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Lấy tên ingredient (null nếu không tìm thấy)
     */
    public String getIngredientNameById(int ingredientId) {
        try {
            String query = "SELECT ingredient_name FROM ingredient WHERE ingredient_id = ?";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{ingredientId});
            if (rs.next()) {
                return rs.getString("ingredient_name");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RecipeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // helper: check if recipe exists / is deleted
    public boolean isRecipeDeleted(int recipeId) {
        try {
            String query = "SELECT status FROM recipe WHERE recipe_id = ?";
            ResultSet rs = this.executeSelectionQuery(query, new Object[]{recipeId});
            if (rs.next()) {
                String status = rs.getString("status");
                return "Deleted".equalsIgnoreCase(status);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
