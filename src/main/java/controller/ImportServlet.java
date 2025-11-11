/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import static constant.CommonFunction.getSqlErrorCode;
import static constant.CommonFunction.getTotalPages;
import static constant.CommonFunction.removePopup;
import static constant.CommonFunction.setPopup;
import static constant.CommonFunction.validateInteger;
import static constant.CommonFunction.validateString;
import dao.EmployeeDAO;
import dao.ImportDAO;
import dao.IngredientDAO;
import dao.SupplierDAO;
import dao.TypeDAO;
import java.io.IOException;
import java.sql.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ImportServlet", urlPatterns = {"/import"})
public class ImportServlet extends HttpServlet {

    private final ImportDAO importDAO = new ImportDAO();
    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final IngredientDAO ingredientDAO = new IngredientDAO();
    private final TypeDAO typeDAO = new TypeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String view = request.getParameter("view");
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }

        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        String namepage;
        if (!validateString(view, -1) || "list".equalsIgnoreCase(view)) {
            namepage = "list";
            request.setAttribute("totalPages", getTotalPages(importDAO.countItem()));
            request.setAttribute("keyword", keyword);
            request.setAttribute("importList", importDAO.getAll(page, keyword));
        } else if ("add".equalsIgnoreCase(view)) {
            namepage = "add";
            request.setAttribute("supplierList", supplierDAO.getAll());
            request.setAttribute("employeeList", employeeDAO.getAll());
        } else if ("edit".equalsIgnoreCase(view)) {
            namepage = "edit";

            int id;
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                id = -1;
            }

            request.setAttribute("currentImport", importDAO.getElementByID(id));
            request.setAttribute("supplierList", supplierDAO.getAll());
            request.setAttribute("employeeList", employeeDAO.getAll());
        } else if ("detail".equalsIgnoreCase(view)) {
            namepage = "detail";

            int id;
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                id = -1;
            }

            if (id > 0) {
                request.setAttribute("currentImport", importDAO.getElementByID(id));
                request.setAttribute("importDetails", importDAO.getImportDetails(id, page, keyword));
            }
        } else if ("addDetail".equalsIgnoreCase(view)) {
            namepage = "addDetail";

            int id;
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                id = -1;
            }

            if (id > 0) {
                request.setAttribute("currentImport", importDAO.getElementByID(id));
                request.setAttribute("ingredientList", ingredientDAO.getAll());
            }
        } else {
            namepage = "list";
            request.setAttribute("totalPages", getTotalPages(importDAO.countItem()));
            request.setAttribute("keyword", keyword);
            request.setAttribute("importList", importDAO.getAll(page, keyword));
        }

        request.getRequestDispatcher("/WEB-INF/import/" + namepage + ".jsp").forward(request, response);
        removePopup(request);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        boolean popupStatus = true;
        String popupMessage = "";
        String redirectUrl = request.getContextPath() + "/import";

        boolean handled = action != null && !action.isEmpty();

        if (handled) {
            if ("add".equalsIgnoreCase(action)) {
                int supplierId;
                int empId;

                try {
                    supplierId = Integer.parseInt(request.getParameter("supplierId"));
                    empId = Integer.parseInt(request.getParameter("empId"));
                } catch (NumberFormatException e) {
                    supplierId = -1;
                    empId = -1;
                }

                if (!validateInteger(supplierId, false, false, true)
                        || !validateInteger(empId, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The add action is NOT successfull. The input has some error.";
                } else {
                    int checkError = importDAO.add(supplierId, empId);
                    if (checkError >= 1) {
                        popupMessage = "The import created successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "The add action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }

            } else if ("addDetail".equalsIgnoreCase(action)) {
                int importId;
                int ingredientId;
                int quantity;
                int unitPrice;
                int totalPrice;


                try {
                    importId = Integer.parseInt(request.getParameter("importId"));
                } catch (NumberFormatException e) {
                    importId = -1;
                }

                if (importId > 0) {
                    redirectUrl = request.getContextPath() + "/import?view=detail&id=" + importId;
                } else {
                    redirectUrl = request.getContextPath() + "/import";
                }

                try {
                    ingredientId = Integer.parseInt(request.getParameter("ingredientId"));
                } catch (NumberFormatException e) {
                    ingredientId = -1;
                }

                try {
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                } catch (NumberFormatException e) {
                    quantity = -1;
                }

                try {
                    unitPrice = Integer.parseInt(request.getParameter("unitPrice"));
                } catch (NumberFormatException e) {
                    unitPrice = -1;
                }

                totalPrice = unitPrice * quantity;

                if (!validateInteger(importId, false, false, true)
                        || !validateInteger(ingredientId, false, false, true)
                        || !validateInteger(quantity, false, false, true)
                        || !validateInteger(unitPrice, false, true, true)) {
                    popupStatus = false;
                    popupMessage = "The add detail action is NOT successfull. The input has some error.";
                } else {
                    int checkError = importDAO.addDetail(importId, ingredientId, quantity, unitPrice, totalPrice);
                    if (checkError >= 1) {
                        popupMessage = "The import detail added successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "The add detail action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }

            } else if ("edit".equalsIgnoreCase(action)) {
                int importId;
                int supplierId;
                int empId;
                Date importDate;

                try {
                    importId = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    importId = -1;
                }

                try {
                    supplierId = Integer.parseInt(request.getParameter("supplierId"));
                } catch (NumberFormatException e) {
                    supplierId = -1;
                }

                try {
                    empId = Integer.parseInt(request.getParameter("empId"));
                } catch (NumberFormatException e) {
                    empId = -1;
                }

                String importDateRaw = request.getParameter("importDate");
                try {
                    importDate = Date.valueOf(importDateRaw);
                } catch (IllegalArgumentException | NullPointerException e) {
                    importDate = null;
                }

                if (!validateInteger(importId, false, false, true)
                        || !validateInteger(supplierId, false, false, true)
                        || !validateInteger(empId, false, false, true)
                        || importDate == null) {
                    popupStatus = false;
                    popupMessage = "The edit action is NOT successfull. The input has some error.";
                } else {
                    int checkError = importDAO.edit(importId, supplierId, empId);
                    if (checkError >= 1) {
                        popupMessage = "The import with id=" + importId + " edited successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "The edit action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }

            } else if ("delete".equalsIgnoreCase(action)) {
                int id;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

                if (!validateInteger(id, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The delete action is NOT successfull.";
                } else {
                    int checkError = importDAO.delete(id);
                    if (checkError >= 1) {
                        popupMessage = "The import with id=" + id + " deleted successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "The delete action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }
            }
        }

        if (handled) {
            setPopup(request, popupStatus, popupMessage);
        }
        response.sendRedirect(redirectUrl);
    }

    @Override
    public String getServletInfo() {
        return "Import management servlet";
    }
}
