/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import static constant.CommonFunction.*;
import dao.RecipeDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author PHAT
 */
@WebServlet(name = "RecipeServlet", urlPatterns = {"/recipe"})
public class RecipeServlet extends HttpServlet {
    RecipeDAO recipeDAO = new RecipeDAO();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RecipeServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RecipeServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String namepage = "";
        String view = request.getParameter("view");
        String keyword = request.getParameter("keyword");
        if (keyword == null) keyword = "";

        if (!validateString(view, -1) || view.equalsIgnoreCase("list")) {
            namepage = "list";
        } else if (view.equalsIgnoreCase("add")) {
            namepage = "add";
        } else if (view.equalsIgnoreCase("edit")) {
            namepage = "edit";
            int id;
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                id = -1;
            }
            request.setAttribute("currentRecipe", recipeDAO.getElementByID(id));
        } else if (view.equalsIgnoreCase("delete")) {
            namepage = "delete";
        } else if (view.equalsIgnoreCase("view")) {
            namepage = "view";
            int id;
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                id = -1;
            }
            request.setAttribute("currentRecipe", recipeDAO.getElementByID(id));
        }

        int page;
        int totalPages = getTotalPages(recipeDAO.countItem());
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("recipesList", recipeDAO.getAll(page, keyword));
        request.getRequestDispatcher("/WEB-INF/recipe/" + namepage + ".jsp").forward(request, response);
        removePopup(request);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        boolean popupStatus = true;
        String popupMessage = "";

        if (action != null && !action.isEmpty()) {
            // Recipe-level actions
            if (action.equalsIgnoreCase("add")) {
                int menuItemId;
                try {
                    menuItemId = Integer.parseInt(request.getParameter("menu_item_id"));
                } catch (NumberFormatException e) {
                    menuItemId = -1;
                }
                // validate
                if (!validateInteger(menuItemId, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The add action is NOT successful. The input has some error.";
                } else {
                    popupMessage = "Recipe added successfully (menu_item_id=" + menuItemId + ").";
                }
                if (popupStatus) {
                    int checkError = recipeDAO.add(menuItemId);
                    if (checkError >= 1) {
                        // success
                    } else {
                        popupStatus = false;
                        popupMessage = "The add action is NOT successful. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }
            } else if (action.equalsIgnoreCase("edit")) {
                int id;
                int menuItemId;
                String status;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }
                try {
                    menuItemId = Integer.parseInt(request.getParameter("menu_item_id"));
                } catch (NumberFormatException e) {
                    menuItemId = -1;
                }
                status = request.getParameter("status");
                if (!validateInteger(id, false, false, true) || !validateInteger(menuItemId, false, false, true) || !validateString(status, -1)) {
                    popupStatus = false;
                    popupMessage = "The edit action is NOT successful. The input has some error.";
                } else {
                    popupMessage = "Recipe with id=" + id + " edited successfully.";
                }
                if (popupStatus) {
                    int checkError = recipeDAO.edit(id, menuItemId, status);
                    if (checkError >= 1) {
                        // ok
                    } else {
                        popupStatus = false;
                        popupMessage = "The edit action is NOT successful. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }
            } else if (action.equalsIgnoreCase("delete")) {
                int id;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }
                if (!validateInteger(id, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The delete action is NOT successful.";
                } else {
                    popupMessage = "Recipe with id=" + id + " deleted successfully.";
                }
                if (popupStatus) {
                    int checkError = recipeDAO.delete(id);
                    if (checkError >= 1) {
                    } else {
                        popupStatus = false;
                        popupMessage = "The delete action is NOT successful. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }
            }

            // Item-level actions (prefix with item_)
            else if (action.equalsIgnoreCase("add_item")) {
                int recipeId, ingredientId;
                double quantity = 0;
                String unit = request.getParameter("unit");
                String note = request.getParameter("note");
                try {
                    recipeId = Integer.parseInt(request.getParameter("recipe_id"));
                } catch (NumberFormatException e) {
                    recipeId = -1;
                }
                try {
                    ingredientId = Integer.parseInt(request.getParameter("ingredient_id"));
                } catch (NumberFormatException e) {
                    ingredientId = -1;
                }
                try {
                    String q = request.getParameter("quantity");
                    if (q != null && !q.isEmpty()) quantity = Double.parseDouble(q);
                } catch (NumberFormatException e) {
                    quantity = 0;
                }

                if (!validateInteger(recipeId, false, false, true) || !validateInteger(ingredientId, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "Add item failed. Input invalid.";
                } else {
                    popupMessage = "Item added to recipe " + recipeId + " successfully.";
                }
                if (popupStatus) {
                    int checkError = recipeDAO.addItem(recipeId, ingredientId, quantity, unit, note);
                    if (checkError >= 1) {
                    } else {
                        popupStatus = false;
                        popupMessage = "Add item failed. SQL error " + getSqlErrorCode(checkError);
                    }
                }
            } else if (action.equalsIgnoreCase("edit_item")) {
                int recipeItemId, ingredientId;
                double quantity = 0;
                String unit = request.getParameter("unit");
                String note = request.getParameter("note");
                String status = request.getParameter("status");
                try {
                    recipeItemId = Integer.parseInt(request.getParameter("recipe_item_id"));
                } catch (NumberFormatException e) {
                    recipeItemId = -1;
                }
                try {
                    ingredientId = Integer.parseInt(request.getParameter("ingredient_id"));
                } catch (NumberFormatException e) {
                    ingredientId = -1;
                }
                try {
                    String q = request.getParameter("quantity");
                    if (q != null && !q.isEmpty()) quantity = Double.parseDouble(q);
                } catch (NumberFormatException e) {
                    quantity = 0;
                }

                if (!validateInteger(recipeItemId, false, false, true) || !validateInteger(ingredientId, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "Edit item failed. Input invalid.";
                } else {
                    popupMessage = "Item edited successfully.";
                }
                if (popupStatus) {
                    int checkError = recipeDAO.editItem(recipeItemId, ingredientId, quantity, unit, note, status);
                    if (checkError >= 1) {
                    } else {
                        popupStatus = false;
                        popupMessage = "Edit item failed. SQL error " + getSqlErrorCode(checkError);
                    }
                }
            } else if (action.equalsIgnoreCase("delete_item")) {
                int recipeItemId;
                try {
                    recipeItemId = Integer.parseInt(request.getParameter("recipe_item_id"));
                } catch (NumberFormatException e) {
                    recipeItemId = -1;
                }
                if (!validateInteger(recipeItemId, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "Delete item failed.";
                } else {
                    popupMessage = "Item deleted successfully.";
                }
                if (popupStatus) {
                    int checkError = recipeDAO.deleteItem(recipeItemId);
                    if (checkError >= 1) {
                    } else {
                        popupStatus = false;
                        popupMessage = "Delete item failed. SQL error " + getSqlErrorCode(checkError);
                    }
                }
            }
        }

        setPopup(request, popupStatus, popupMessage);
        response.sendRedirect(request.getContextPath() + "/recipe");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
