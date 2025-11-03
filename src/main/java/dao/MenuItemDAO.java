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
import model.Category;
import model.MenuItem;

public class MenuItemDAO extends DBContext {

    public static void main(String[] args) {
    }

    public List<String> getAllCategoryNames() {
        List<String> categoryNames = new ArrayList<>();
        try {
            String query = "SELECT category_name FROM category WHERE LOWER(status) = 'active' ORDER BY category_id";
            ResultSet rs = this.executeSelectionQuery(query, null);

            while (rs.next()) {
                categoryNames.add(rs.getString("category_name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return categoryNames;
    }

    public List<MenuItem> getMenuItemsByCategoryName(String categoryName) {
        List<MenuItem> list = new ArrayList<>();
        try {
            
            String query = "SELECT \n"
                    + "    mi.menu_item_id, mi.category_id, mi.item_name, \n"
                    + "    mi.image_url, mi.price, mi.description, mi.status, \n"
                    + "    c.category_name, \n"
                    + "    r.recipe_name, \n"
                    + "    (SELECT STRING_AGG(CONCAT(ri.quantity, ' ', ri.unit, ' ', i.ingredient_name), ', ') \n"
                    + "     FROM recipe_item ri \n"
                    + "     JOIN ingredient i ON ri.ingredient_id = i.ingredient_id \n"
                    + "     WHERE ri.recipe_id = mi.recipe_id AND LOWER(ri.status) = 'active') AS ingredients\n"
                    + "FROM menu_item mi \n"
                    + "JOIN category c ON mi.category_id = c.category_id \n"
                    + "JOIN recipe r ON mi.recipe_id = r.recipe_id\n"
                    + "WHERE LOWER(c.category_name) = LOWER(?) \n"
                    + "  AND LOWER(mi.status) = 'active'\n"
                    + "ORDER BY mi.menu_item_id";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{categoryName});

            while (rs.next()) {

                double price = rs.getInt("price");

            
                String ingredients = rs.getString("ingredients");

                MenuItem item = new MenuItem(
                        rs.getInt("menu_item_id"),
                        rs.getInt("category_id"),
                        rs.getString("item_name"),
                        ingredients, 
                        rs.getString("image_url"),
                        price,
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getString("category_name")
                );
                list.add(item);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
