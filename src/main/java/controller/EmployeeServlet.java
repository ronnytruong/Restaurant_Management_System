/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import static constant.CommonFunction.*;
import dao.EmployeeDAO;
import dao.RoleDAO;
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
@WebServlet(name = "EmployeeServlet", urlPatterns = {"/employee"})
public class EmployeeServlet extends HttpServlet {

    EmployeeDAO employeeDAO = new EmployeeDAO();
    RoleDAO roleDAO = new RoleDAO();

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
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet EmployeeServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EmployeeServlet at " + request.getContextPath() + "</h1>");
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
        if (keyword == null) {
            keyword = "";
        }

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

            request.setAttribute("currentEmployee", employeeDAO.getElementByID(id));
        } else if (view.equalsIgnoreCase("delete")) {
            namepage = "delete";
        }

        int page;
        int totalPages = getTotalPages(employeeDAO.countItem());

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        request.setAttribute("rolesList", roleDAO.getAll());
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("employeeList", employeeDAO.getAll(page, keyword));

        request.getRequestDispatcher("/WEB-INF/employee/" + namepage + ".jsp").forward(request, response);
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

            if (action.equalsIgnoreCase("add")) {
                String empAccount = request.getParameter("empAccount");
                String password = request.getParameter("password");
                String empName = request.getParameter("empName");
                int roleId;

                try {
                    roleId = Integer.parseInt(request.getParameter("roleId"));
                    if (roleDAO.isRoleDeleted(roleId)) {
                        throw new Exception();
                    }
                } catch (Exception ex) {
                    roleId = -1;
                }

//validate
                if (!validateString(empAccount, -1)
                        || !validateString(password, -1)
                        || !validateString(empName, -1)
                        || !validateInteger(roleId, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The add action is NOT successfull. The input has some error.";
                } else {
                    popupMessage = "The object with name=" + empName + " added successfull"
                            + "(Account: " + empAccount + "; "
                            + "Password: " + password + ")";
                }
//end
                
                password = constant.HashUtil.toMD5(password);

                if (popupStatus == true) {
                    int checkError = employeeDAO.add(empAccount, password, empName, roleId);
                    if (checkError >= 1) {
                    } else {
                        popupStatus = false;
                        popupMessage = "The add action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }

            } else if (action.equalsIgnoreCase("edit")) {
                int empId;
                int roleId;

                try {
                    empId = Integer.parseInt(request.getParameter("id"));
                    roleId = Integer.parseInt(request.getParameter("roleId"));
                    
                    if (roleDAO.isRoleDeleted(roleId)) {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    empId = -1;
                    roleId = -1;
                }

//validate
                if (!validateInteger(empId, false, false, true)||
                        !validateInteger(roleId, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The edit action is NOT successfull. The input has some error.";
                } else {
                    popupMessage = "The object with id=" + empId + " edited role successfull.";
                }
//end

                if (popupStatus == true) {
                    int checkError = employeeDAO.edit(empId, roleId);

                    if (checkError >= 1) {
                    } else {
                        popupStatus = false;
                        popupMessage = "The edit action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }
            } else if (action.equalsIgnoreCase("delete")) {
                int id;

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

//validate
                if (!validateInteger(id, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The delete action is NOT successfull.";
                } else {
                    popupMessage = "The object with id=" + id + " deleted successfull.";
                }
//end
                if (popupStatus == true) {
                    int checkError = employeeDAO.delete(id);

                    if (checkError >= 1) {

                    } else {
                        popupStatus = false;
                        popupMessage = "The delete action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }
            } else if (action.equalsIgnoreCase("ban")) {
                int id;

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

//validate
                if (!validateInteger(id, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The ban action is NOT successfull.";
                } else {
                    popupMessage = "The object with id=" + id + " banned successfull.";
                }
//end
                if (popupStatus == true) {
                    int checkError = employeeDAO.ban(id);

                    if (checkError >= 1) {

                    } else {
                        popupStatus = false;
                        popupMessage = "The ban action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }
            } else if (action.equalsIgnoreCase("unban")) {
                int id;

                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (NumberFormatException e) {
                    id = -1;
                }

//validate
                if (!validateInteger(id, false, false, true)) {
                    popupStatus = false;
                    popupMessage = "The unban action is NOT successfull.";
                } else {
                    popupMessage = "The object with id=" + id + " unbanned successfull.";
                }
//end
                if (popupStatus == true) {
                    int checkError = employeeDAO.unban(id);

                    if (checkError >= 1) {

                    } else {
                        popupStatus = false;
                        popupMessage = "The unban action is NOT successfull. The object has " + getSqlErrorCode(checkError) + " error.";
                    }
                }
            }
            setPopup(request, popupStatus, popupMessage);
            response.sendRedirect(request.getContextPath() + "/employee");
        }
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
