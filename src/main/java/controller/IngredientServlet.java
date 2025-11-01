/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import static constant.CommonFunction.addEDtoEverything;
import static constant.CommonFunction.getTotalPages;
import static constant.CommonFunction.validateInteger;
import static constant.CommonFunction.validateString;
import constant.Constants;
import dao.ImportDAO;
import dao.IngredientDAO;
import dao.TypeDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Ingredient;

/**
 *
 * @author TruongBinhTrong
 */
@WebServlet(name = "IngredientServlet", urlPatterns = {"/ingredient"})
public class IngredientServlet extends HttpServlet {
    IngredientDAO ingredientDAO = new IngredientDAO();
    TypeDAO typeDAO = new TypeDAO();
    ImportDAO importDAO = new ImportDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* sample */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet IngredientServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet IngredientServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
   String namepage = "";
        String view = request.getParameter("view");
     
        String searchKeyword = request.getParameter("search");
        List<Ingredient> ingredients;
        int totalPages = 1; 
        int page = 1;
        
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
     
            ingredients = ingredientDAO.search(searchKeyword);
            request.setAttribute("searchKeyword", searchKeyword); 
        } else {
           
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
            totalPages = getTotalPages(ingredientDAO.countItem());
            ingredients = ingredientDAO.getAll(page);
        }
      

        if (!validateString(view, -1) || view.equalsIgnoreCase("list")) {
            int id = 0;
            namepage = "list";
            request.setAttribute("importList", importDAO.getImportDetails(id));
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

            request.setAttribute("currentIngredient", ingredientDAO.getElementByID(id));
        } else if (view.equalsIgnoreCase("delete")) {
            namepage = "delete";
        }

        // The old pagination logic is now integrated above
        // int page;
        // int totalPages = getTotalPages(ingredientDAO.countItem()); // Moved up

        // try {
        //     page = Integer.parseInt(request.getParameter("page"));
        // } catch (NumberFormatException e) {
        //     page = 1;
        // }

        // Set attributes based on search or list view
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("ingredientsList", ingredients); // Changed to 'ingredients' list
        request.setAttribute("currentPage", page); // Add current page to handle pagination CSS
        request.setAttribute("typesList", typeDAO.getAll());

        request.getRequestDispatcher("/WEB-INF/ingredient/" + namepage + ".jsp").forward(request, response);
    
    }

    // POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        boolean passValidation = true;
        if (action != null && !action.isEmpty()) {

            if (action.equalsIgnoreCase("add")) {
                String name = request.getParameter("ingredientName");
                String priceStr = request.getParameter("price");
                int typeId;
                double price;

                try {
                    typeId = Integer.parseInt(request.getParameter("typeId"));
                } catch (NumberFormatException e) {
                    typeId = -1;
                }

                try {
                    price = Double.parseDouble(priceStr);
                } catch (NumberFormatException e) {
                    price = 0.0;
                }

                //validate
                if (!validateString(name, -1) || !validateInteger(typeId, false, false, true)) {
                    passValidation = false;
                }
                //end
                if (passValidation == true) {
                    if (ingredientDAO.add(name, typeId, price) >= 1) {
                    } else {
                        passValidation = false;
                    }
                }

            } else if (action.equalsIgnoreCase("edit")) {
                int id;
                String name = request.getParameter("ingredientName");
                String priceStr = request.getParameter("price");
                int typeId;
                double price;

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                    passValidation = false;
                }

                try {
                    typeId = Integer.parseInt(request.getParameter("typeId"));
                } catch (NumberFormatException e) {
                    typeId = -1;
                }

                try {
                    price = Double.parseDouble(priceStr);
                } catch (NumberFormatException e) {
                    price = 0.0;
                }

                //validate
                if (!validateString(name, -1)
                        || !validateInteger(id, false, false, true)
                        || !validateInteger(typeId, false, false, true)) {
                    passValidation = false;
                }
                //end
                if (passValidation == true) {
                    int checkError = ingredientDAO.edit(id, name, typeId, price);

                    if (checkError >= 1) {

                    } else {
                        if (checkError - Constants.DUPLICATE_KEY == 0) {                
                            System.err.println("DUPLICATE_KEY");
                        } else if (checkError - Constants.FOREIGN_KEY_VIOLATION == 0) {
                            System.err.println("FOREIGN_KEY_VIOLATION");
                        } else if (checkError - Constants.NULL_INSERT_VIOLATION == 0) {
                            System.err.println("NULL_INSERT_VIOLATION");
                        }

                        passValidation = false;
                    }
                }
            } else if (action.equalsIgnoreCase("delete")) {
                int id;

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;

                    passValidation = false;
                }

                //validate
                if (!validateInteger(id, false, false, true)) {
                    passValidation = false;
                }
                //end
                if (passValidation == true) {
                    int checkError = ingredientDAO.delete(id);

                    if (checkError >= 1) {

                    } else {
                        if (checkError - Constants.DUPLICATE_KEY == 0) {                
                            System.err.println("DUPLICATE_KEY");
                        } else if (checkError - Constants.FOREIGN_KEY_VIOLATION == 0) {
                            System.err.println("FOREIGN_KEY_VIOLATION");
                        } else if (checkError - Constants.NULL_INSERT_VIOLATION == 0) {
                            System.err.println("NULL_INSERT_VIOLATION");
                        }

                        passValidation = false;
                    }
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/ingredient?" + "status=" + (passValidation ? "success" : "fail") + "&lastAction=" + addEDtoEverything(action));

    }

    @Override
    public String getServletInfo() {
        return "Ingredient management servlet";
    }// </editor-fold>

}
