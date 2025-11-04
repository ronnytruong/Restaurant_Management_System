package controller;
import static constant.Constants.MAX_ELEMENTS_PER_PAGE;
import dao.CategoryDAO;
import dao.MenuItemDAO;
import dao.RecipeDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Category;
import model.MenuItem;
import model.Recipe;
import java.util.logging.Level;
import java.util.logging.Logger;

// File upload dependencies
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;

// Added MultipartConfig for file upload 
@WebServlet(name="MenuItemServlet", urlPatterns={"/menuitem"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 10,      
    maxRequestSize = 1024 * 1024 * 50   
)
public class MenuItemServlet extends HttpServlet {
    
    private final MenuItemDAO menuItemDAO = new MenuItemDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final RecipeDAO recipeDAO = new RecipeDAO();
    
  
    private static final String UPLOAD_DIRECTORY = "assets" + File.separator + "img" + File.separator + "menu";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String view = request.getParameter("view");
        
             if ("add".equals(view) || "edit".equals(view)) {
            request.setAttribute("categories", categoryDAO.getAll());
            request.setAttribute("recipes", recipeDAO.getAll());
        }

       
        if ("add".equals(view)) {
            request.getRequestDispatcher("/WEB-INF/menu/add.jsp").forward(request, response);
            
        } else if ("edit".equals(view)) {
            String idStr = request.getParameter("id");
            try {
                int id = Integer.parseInt(idStr);
                MenuItem item = menuItemDAO.getElementByID(id);
                
                if (item != null) {
                    request.setAttribute("menuItem", item);
                    request.getRequestDispatcher("/WEB-INF/menu/edit.jsp").forward(request, response);
                } else {
                    response.sendRedirect("menuitem?view=list");
                }
            } catch (NumberFormatException | NullPointerException e) {
                response.sendRedirect("menuitem?view=list");
            }
            
        } else {
            String pageStr = request.getParameter("page");
            String keyword = request.getParameter("keyword");
            
            int page = (pageStr != null && pageStr.matches("\\d+")) ? Integer.parseInt(pageStr) : 1;
            
            int totalItems = menuItemDAO.countItem(keyword);
            int totalPages = (int) Math.ceil((double) totalItems / MAX_ELEMENTS_PER_PAGE);

            List<MenuItem> menuItemsList = menuItemDAO.searchAll(keyword, page, MAX_ELEMENTS_PER_PAGE);
            
            request.setAttribute("menuItemsList", menuItemsList);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("currentPage", page);
            request.setAttribute("keyword", keyword);
            
            request.getRequestDispatcher("/WEB-INF/menu/emp-list.jsp").forward(request, response);
        }
    }

   @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        

        String action = request.getParameter("action");
        String idStr = request.getParameter("id");
        int id = -1;
        
        try {
            if (idStr != null) id = Integer.parseInt(idStr);
        } catch (NumberFormatException ignored) {}
        
        int result = -1;
        String message = "";
        
        //Handle POST Actions 
        switch (action) {
            case "delete":
                if (id > 0) {
                    result = menuItemDAO.delete(id); 
                    message = (result > 0) ? "Menu item deleted successfully." : "Menu item deletion failed.";
                }
                break;
                
            case "add":
            case "update":
                try {
                    
                    String itemName = request.getParameter("itemName");
                    int price = Integer.parseInt(request.getParameter("price"));
                    int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                    int recipeId = Integer.parseInt(request.getParameter("recipeId"));
                    String description = request.getParameter("description");
                    String status = request.getParameter("status"); 
                    String existingImageUrl = request.getParameter("existingImageUrl"); 
                    
                    
                    Part filePart = request.getPart("imageFile"); 
                    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
                    String newImageUrl = existingImageUrl; 

                    if (fileName != null && !fileName.isEmpty()) {
                        String applicationPath = request.getServletContext().getRealPath("");
                        String uploadPath = applicationPath + File.separator + UPLOAD_DIRECTORY;
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) uploadDir.mkdirs();

                        // --- START MODIFICATION ---
                        // Replaced the unique file naming with the original file name
                        String finalFileName = fileName;
                        filePart.write(uploadPath + File.separator + finalFileName);
                        
                        // Set the new image URL using the final file name
                        newImageUrl = UPLOAD_DIRECTORY.replace(File.separator, "/") + "/" + finalFileName;
                        // --- END MODIFICATION ---
                    } 
                    
                    
                    
                    Category category = categoryDAO.getElementByID(categoryId);
                    Recipe recipe = recipeDAO.getElementByID(recipeId);

                    if (category != null && recipe != null && newImageUrl != null) {
                        String finalStatus = (id > 0 && status != null) ? status : "Active";
                        
                        MenuItem item = new MenuItem(
                            id, category, recipe, itemName, newImageUrl, price, description, finalStatus
                        );
                        
                        if ("add".equals(action)) {
                            result = menuItemDAO.add(item);
                            message = (result > 0) ? "Menu item added successfully." : "Menu item addition failed. (DB Error: " + result + ")";
                        } else { 
                            if (id > 0) {
                                result = menuItemDAO.edit(item);
                                message = (result > 0) ? "Menu item updated successfully." : "Menu item update failed. (DB Error: " + result + ")";
                            } else {
                                message = "Update failed: Invalid ID.";
                            }
                        }
                    } else {
                         message = "Operation failed: Category, Recipe, or Image URL invalid.";
                    }
                } catch (NumberFormatException e) {
                    message = "Operation failed: Invalid number format (e.g., price).";
                    Logger.getLogger(MenuItemServlet.class.getName()).log(Level.WARNING, "Invalid number input", e);
                } catch (Exception e) {
                    message = "Operation failed: Unexpected error during file processing or database operation.";
                    Logger.getLogger(MenuItemServlet.class.getName()).log(Level.SEVERE, "Unexpected error in POST", e);
                }
                break;
                
            default:
                message = "Unknown action specified.";
                break;
        }
        
        request.getSession().setAttribute("alertMessage", message);
        response.sendRedirect("menuitem?view=list");
    }
}


