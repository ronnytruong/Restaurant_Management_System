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

    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final RecipeDAO recipeDAO = new RecipeDAO();

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

    public MenuItem getElementByID(int id) {

        try {
            String query = "SELECT menu_item_id, category_id, recipe_id, item_name, image_url, price, description, status\n"
                    + "FROM     menu_item\n"
                    + "WHERE  (LOWER(status) <> LOWER('Deleted')) AND (menu_item_id = ?)";

            ResultSet rs = this.executeSelectionQuery(query, new Object[]{});

            while (rs.next()) {

                int menuItemId = rs.getInt(1);
                int categoryId = rs.getInt(2);
                int recipeId = rs.getInt(3);
                String itemName = rs.getString(4);
                String imageUrl = rs.getString(5);
                int price = rs.getInt(6);
                String description = rs.getString(7);
                String status = rs.getString(8);

                MenuItem item = new MenuItem(menuItemId, 
                        categoryDAO.getElementByID(categoryId), 
                        recipeDAO.getElementByID(recipeId), 
                        itemName, imageUrl, price, description, status);

                return item;
            }
        } catch (SQLException ex) {
            System.out.println("Can't not load object");
        }

        return null;
    }
}
