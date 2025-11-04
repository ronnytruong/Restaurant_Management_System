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
import model.Recipe;

public class MenuItemDAO extends DBContext {

    public static void main(String[] args) {
    }
/**
 * 
 * @return 
 */
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
/**
 * 
 * @param categoryName
 * @return 
 */
    public List<MenuItem> getMenuItemsByCategoryName(String categoryName) {
        List<MenuItem> list = new ArrayList<>();
        try {

            String query = "SELECT \n"
                    + "     mi.menu_item_id, mi.category_id, mi.recipe_id, mi.item_name, \n"
                    + "     mi.image_url, mi.price, mi.description, mi.status, \n"
                    + "     c.category_name, \n"
                    + "     r.recipe_name \n"
                    + "FROM menu_item mi \n"
                    + "JOIN category c ON mi.category_id = c.category_id \n"
                    + "JOIN recipe r ON mi.recipe_id = r.recipe_id\n"
                    + "WHERE LOWER(c.category_name) = LOWER(?) \n"
                    + "  AND LOWER(mi.status) = 'active'\n"
                    + "ORDER BY mi.menu_item_id";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{categoryName});

            while (rs.next()) {

              int menuItemId = rs.getInt("menu_item_id");
                String itemName = rs.getString("item_name");
                String imageUrl = rs.getString("image_url");
                int price = rs.getInt("price"); 
                String description = rs.getString("description");
                String status = rs.getString("status");
                int categoryId = rs.getInt("category_id");
                String catName = rs.getString("category_name");
                Category category = new Category(categoryId, catName, null, null);
                int recipeId = rs.getInt("recipe_id");
                String recipeName = rs.getString("recipe_name");
                Recipe recipe = new Recipe(recipeId, recipeName, null);
                

                MenuItem item = new MenuItem(
                     menuItemId,
                        category,
                        recipe,
                        itemName,
                        imageUrl,
                        price,
                        description,
                        status
                );
                list.add(item);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
