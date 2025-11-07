/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.OrderDAO;
import dao.OrderItemDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Customer;
import model.Order;

/**
 *
 * @author Dai Minh Nhu - CE190213
 */
@WebServlet(name = "MyOrderServlet", urlPatterns = {"/myOrder"})
public class MyOrderServlet extends HttpServlet {

    private final int MAX_ELEMENTS_PER_PAGE = 15;
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();

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
            out.println("<title>Servlet MyOrderServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MyOrderServlet at " + request.getContextPath() + "</h1>");
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
        Customer customer = null;
        // check tam
        try {
            customer = (Customer) request.getSession(false).getAttribute("customerSession");
        } catch (Exception e){
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        // check tam

        int page;
        int totalPages = getTotalPages(orderDAO.countItemByCustomer(customer.getCustomerId()));

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        request.setAttribute("totalPages", totalPages);
        request.setAttribute("ordersList", orderDAO.getAllByCustomerId(customer.getCustomerId(), page, MAX_ELEMENTS_PER_PAGE));

        request.getRequestDispatcher("/WEB-INF/myOrder/list.jsp").forward(request, response);
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

        Customer customer = null;

        // check tam
        try {
            customer = (Customer) request.getSession(false).getAttribute("customerSession");
        } catch (Exception e){
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        // check tam
        
        if (action != null && !action.isEmpty()) {
            if (action.equalsIgnoreCase("cancel")) {

                int orderId;

                try {
                    orderId = Integer.parseInt(request.getParameter("orderId"));
                } catch (NumberFormatException e) {
                    orderId = -1;
                }

                Order order = orderDAO.getElementByID(orderId);

//validate
                if (orderId <= 0 || order == null
                        || order.getReservation().getCustomer().getCustomerId() != customer.getCustomerId()
                        || !order.getStatus().equalsIgnoreCase("Pending")) {
                    popupStatus = false;
                    popupMessage = "The cancel action is NOT successfull.";
                } else {
                    popupMessage = "The order cancelled successfull.";
                }
//end
                if (popupStatus == true) {
                    int checkError = orderDAO.cancel(orderId);

                    if (checkError >= 1) {

                    } else {
                        popupStatus = false;
                        popupMessage = "The cancle action is NOT successfull. Check the information again.";
                    }
                }
            } else if (action.equalsIgnoreCase("detail")) {

                int orderId;

                try {
                    orderId = Integer.parseInt(request.getParameter("orderId"));
                } catch (NumberFormatException e) {
                    orderId = -1;
                }

                Order order = orderDAO.getElementByID(orderId);

//validate
                if (orderId <= 0 || order == null
                        || order.getReservation().getCustomer().getCustomerId() != customer.getCustomerId()) {
                    popupStatus = false;
                    popupMessage = "The view detail action is NOT successfull.";
                } else {
                    popupMessage = orderItemDAO.exportBill(orderId);
                }
//end
            }
        }

        setPopup(request, popupStatus, popupMessage);
        response.sendRedirect(request.getContextPath() + "/myOrder");
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

    private int getTotalPages(int countItems) {
        return (int) Math.ceil((double) countItems / MAX_ELEMENTS_PER_PAGE);
    }

    private void setPopup(HttpServletRequest request, boolean status, String message) {
        HttpSession session = request.getSession(false);
        session.setAttribute("popupStatus", status);
        session.setAttribute("popupMessage", message);
    }

    private void removePopup(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.removeAttribute("popupStatus");
        session.removeAttribute("popupMessage");
    }
}
