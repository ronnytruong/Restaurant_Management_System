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

import dao.ReservationDAO;
import dao.TableDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Time;
import model.Reservation;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "ReservationServlet", urlPatterns = {"/reservation"})
public class ReservationServlet extends HttpServlet {

    ReservationDAO reservationDAO = new ReservationDAO();
    TableDAO tableDAO = new TableDAO();

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
            out.println("<title>Servlet ReservationServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReservationServlet at " + request.getContextPath() + "</h1>");
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

        if (!validateString(view, -1) || view.equalsIgnoreCase("list")) {
            // ADMIN list
            int totalPages = getTotalPages(reservationDAO.countItem(keyword));
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("reservationList", reservationDAO.getAll(page, keyword));
            request.getRequestDispatcher("/WEB-INF/reservation/list.jsp").forward(request, response);

        } else if (view.equalsIgnoreCase("mylist")) {
            // CUSTOMER list
            int customerId;
            try {
                customerId = Integer.parseInt(request.getParameter("customerId"));
            } catch (NumberFormatException e) {
                customerId = -1;
            }

            int totalPages = getTotalPages(reservationDAO.countByCustomer(customerId, keyword));
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("customerId", customerId);
            request.setAttribute("reservationList", reservationDAO.getByCustomer(customerId, page, keyword));
            request.setAttribute("availableTables", tableDAO.getAll());
            request.getRequestDispatcher("/WEB-INF/reservation/mylist.jsp").forward(request, response);

        } else if (view.equalsIgnoreCase("edit")) {
            // load form edit
            int id;
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                id = -1;
            }
            request.setAttribute("currentReservation", reservationDAO.getElementByID(id));
            request.getRequestDispatcher("/WEB-INF/reservation/edit.jsp").forward(request, response);

        } else if (view.equalsIgnoreCase("add")) {
            // mở form add (admin tạo đặt chỗ mới)
            request.getRequestDispatcher("/WEB-INF/reservation/add.jsp").forward(request, response);

        } else {
            response.sendRedirect(request.getContextPath() + "/reservation");
        }
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

        if (!validateString(action, -1)) {
            response.sendRedirect(request.getContextPath() + "/reservation");
            return;
        }

        if (action.equalsIgnoreCase("add")) {
            // Add Reservation (customer self-create)
            int customerId, tableId, partySize;
            Date date;
            Time time;
            try {
                customerId = Integer.parseInt(request.getParameter("customerId"));
                tableId = Integer.parseInt(request.getParameter("tableId"));
                partySize = Integer.parseInt(request.getParameter("partySize"));
                date = Date.valueOf(request.getParameter("reservationDate"));
                time = Time.valueOf(request.getParameter("reservationTime") + ":00");
            } catch (Exception e) {
                popupStatus = false;
                popupMessage = "Invalid input for Add Reservation.";
                setPopup(request, popupStatus, popupMessage);
                response.sendRedirect(request.getContextPath() + "/reservation?view=mylist");
                return;
            }

            // ✅ Kiểm tra trạng thái bàn trước khi thêm
            model.Table selectedTable = tableDAO.getElementByID(tableId);
            if (selectedTable == null) {
                popupStatus = false;
                popupMessage = "Table not found.";
            } else if (selectedTable.getStatus().equalsIgnoreCase("Reserved")
                    || selectedTable.getStatus().equalsIgnoreCase("Occupied")) {
                popupStatus = false;
                popupMessage = "This table is currently not available for booking.";
            } else {
                int check = reservationDAO.add(customerId, tableId, date, time, partySize);
                if (check < 1) {
                    popupStatus = false;
                    popupMessage = "Add failed. SQL error: " + getSqlErrorCode(check);
                } else {
                    popupMessage = "Reservation created successfully. Status = Pending.";
                }
            }

            setPopup(request, popupStatus, popupMessage);
            response.sendRedirect(request.getContextPath()
                    + "/reservation?view=mylist&customerId=" + request.getParameter("customerId"));
            return;
        } else if (action.equalsIgnoreCase("edit")) {
            int id, tableId, party;
            Date date;
            Time time;

            try {
                id = Integer.parseInt(request.getParameter("reservationId"));
                tableId = Integer.parseInt(request.getParameter("tableId"));
                party = Integer.parseInt(request.getParameter("partySize"));
                date = Date.valueOf(request.getParameter("reservationDate"));
                time = Time.valueOf(request.getParameter("reservationTime") + ":00");
            } catch (Exception e) {
                id = -1;
                tableId = -1;
                party = -1;
                date = null;
                time = null;
            }

            if (!validateInteger(id, false, false, true) || tableId <= 0 || party <= 0
                    || date == null || time == null) {
                popupStatus = false;
                popupMessage = "The edit action failed. Invalid input data.";
            } else {
                int check = reservationDAO.edit(id, tableId, date, time, party);
                if (check < 1) {
                    popupStatus = false;
                    popupMessage = "Edit failed. SQL error: " + getSqlErrorCode(check);
                } else {
                    popupMessage = "Reservation (ID: " + id + ") updated successfully!";
                }
            }

            setPopup(request, popupStatus, popupMessage);
            String from = request.getParameter("from");
            String customerIdStr = request.getParameter("customerId");
            if ("mylist".equalsIgnoreCase(from) && customerIdStr != null) {
                response.sendRedirect(request.getContextPath()
                        + "/reservation?view=mylist&customerId=" + customerIdStr);
            } else {
                response.sendRedirect(request.getContextPath() + "/reservation");
            }
            return;
        } else if (action.equalsIgnoreCase("approve") || action.equalsIgnoreCase("reject")) {
            int id;
            try {
                id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                id = -1;
            }

            if (!validateInteger(id, false, false, true)) {
                popupStatus = false;
                popupMessage = "Invalid reservation ID.";
            } else {
                Reservation current = reservationDAO.getElementByID(id);
                if (current == null) {
                    popupStatus = false;
                    popupMessage = "Reservation not found.";
                } else if (current.getStatus().equalsIgnoreCase("Approved") && action.equalsIgnoreCase("reject")) {
                    popupStatus = false;
                    popupMessage = "Already approved. Cannot reject.";
                } else if (current.getStatus().equalsIgnoreCase("Rejected") && action.equalsIgnoreCase("approve")) {
                    popupStatus = false;
                    popupMessage = "Already rejected. Cannot approve.";
                } else {
                    String status = action.equalsIgnoreCase("approve") ? "Approved" : "Rejected";
                    int check = reservationDAO.updateStatus(id, status);
                    if (check < 1) {
                        popupStatus = false;
                        popupMessage = "Update status failed. SQL error: " + getSqlErrorCode(check);
                    } else {
                        popupMessage = "Reservation (ID: " + id + ") -> " + status;

                        int tableId = current.getTable().getId();
                        if (status.equalsIgnoreCase("Approved")) {
                            tableDAO.updateStatus(tableId, "Reserved");
                        } else if (status.equalsIgnoreCase("Rejected")) {
                            // Chỉ set lại Available nếu KHÔNG có reservation nào khác đang Approved/Seated cho cùng bàn
                            boolean hasActive = reservationDAO.hasActiveReservationForTable(tableId);
                            if (!hasActive) {
                                tableDAO.updateStatus(tableId, "Available");
                            }
                        }

                    }

                }
                setPopup(request, popupStatus, popupMessage);
                response.sendRedirect(request.getContextPath() + "/reservation");
                return;
            }
        } else if (action.equalsIgnoreCase("cancel")) {
            int id, customerId;
            try {
                id = Integer.parseInt(request.getParameter("id"));
                customerId = Integer.parseInt(request.getParameter("customerId"));
            } catch (NumberFormatException e) {
                id = -1;
                customerId = -1;
            }
            if (!validateInteger(id, false, false, true) || !validateInteger(customerId, false, false, true)) {
                popupStatus = false;
                popupMessage = "Invalid cancel request.";
            } else {
                int check = reservationDAO.cancelByCustomer(id, customerId);
                if (check < 1) {
                    popupStatus = false;
                    popupMessage = "Cancel failed. SQL error: " + getSqlErrorCode(check);
                } else {
                    popupMessage = "Reservation cancelled.";
                }
            }
            setPopup(request, popupStatus, popupMessage);
            response.sendRedirect(request.getContextPath()
                    + "/reservation?view=mylist&customerId=" + request.getParameter("customerId"));
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
