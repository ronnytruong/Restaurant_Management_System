/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import static constant.CommonFunction.*;
import dao.VoucherDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author PHAT
 */
@WebServlet(name = "VoucherServlet", urlPatterns = {"/voucher"})
public class VoucherServlet extends HttpServlet {

    VoucherDAO dao = new VoucherDAO();
    int PAGE_SIZE = 10;

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
            out.println("<title>Servlet VoucherServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet VoucherServlet at " + request.getContextPath() + "</h1>");
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

        // Xác định view
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
            request.setAttribute("currentVoucher", dao.getElementById(id));
        } else if (view.equalsIgnoreCase("delete")) {
            namepage = "delete";
        }

        // Pagination
        int page;
        int totalPages = getTotalPages(dao.countItem());
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        request.setAttribute("totalPages", totalPages);
        request.setAttribute("voucherList", dao.getAll(page, keyword));
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("/WEB-INF/voucher/" + namepage + ".jsp").forward(request, response);
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
                String code = request.getParameter("voucher_code");
                String name = request.getParameter("voucher_name");
                String discountType = request.getParameter("discount_type");
                String rawDiscount = request.getParameter("discount_value");
                String status = request.getParameter("status");

                int quantity;
                Date startDate, endDate;
                BigDecimal discountValue;

                try {
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                    startDate = Date.valueOf(request.getParameter("start_date"));
                    endDate = Date.valueOf(request.getParameter("end_date"));
                    discountValue = new BigDecimal(rawDiscount);
                } catch (Exception e) {
                    quantity = -1;
                    startDate = null;
                    endDate = null;
                    discountValue = BigDecimal.ZERO;
                }

                // Validate dữ liệu
                if (!validateString(code, -1)
                        || !validateString(name, -1)
                        || !validateString(discountType, -1)
                        || !validateString(status, -1)
                        || !validateInteger(quantity, false, true, true)
                        || startDate == null || endDate == null
                        || startDate.after(endDate)) {
                    popupStatus = false;
                    popupMessage = "The add action is NOT successful. Please check your input (invalid or start date after end date).";
                } else {
                    int result = dao.add(code, name, discountType, discountValue, quantity, startDate, endDate, status);
                    if (result >= 1) {
                        popupMessage = "Voucher \"" + name + "\" added successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "The add action failed. SQL error: " + getSqlErrorCode(result);
                    }
                }

            } else if (action.equalsIgnoreCase("edit")) {
                int id, quantity;
                String code = request.getParameter("voucher_code");
                String name = request.getParameter("voucher_name");
                String discountType = request.getParameter("discount_type");
                String rawDiscount = request.getParameter("discount_value");
                String status = request.getParameter("status");

                Date startDate, endDate;
                BigDecimal discountValue;

                try {
                    id = Integer.parseInt(request.getParameter("voucher_id"));
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                    startDate = Date.valueOf(request.getParameter("start_date"));
                    endDate = Date.valueOf(request.getParameter("end_date"));
                    discountValue = new BigDecimal(rawDiscount);
                } catch (Exception e) {
                    id = -1;
                    quantity = -1;
                    startDate = null;
                    endDate = null;
                    discountValue = BigDecimal.ZERO;
                }

                if (!validateInteger(id, false, false, true)
                        || !validateString(code, -1)
                        || !validateString(name, -1)
                        || !validateString(status, -1)
                        || startDate == null || endDate == null
                        || startDate.after(endDate)) {
                    popupStatus = false;
                    popupMessage = "The edit action is NOT successful. Invalid input or start date after end date.";
                } else {
                    int result = dao.edit(id, code, name, discountType, discountValue, quantity, startDate, endDate, status);
                    if (result >= 1) {
                        popupMessage = "Voucher ID=" + id + " edited successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "The edit action failed. SQL error: " + getSqlErrorCode(result);
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
                    popupMessage = "The delete action is NOT successful. Invalid ID.";
                } else {
                    int result = dao.delete(id);
                    if (result >= 1) {
                        popupMessage = "Voucher ID=" + id + " deleted successfully.";
                    } else {
                        popupStatus = false;
                        popupMessage = "The delete action failed. SQL error: " + getSqlErrorCode(result);
                    }
                }
            }
        }
        setPopup(request, popupStatus, popupMessage);
        response.sendRedirect(request.getContextPath() + "/voucher");
        
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
