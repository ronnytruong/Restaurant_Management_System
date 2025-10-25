/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import constant.HashUtil;
import dao.EmployeeDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import model.Employee;

/**
 *
 * @author PHAT
 */
@WebServlet(name = "Profile_EmployeeServlet", urlPatterns = {"/profileEmployee"})
public class Profile_EmployeeServlet extends HttpServlet {

    private EmployeeDAO employeeDAO = new EmployeeDAO();

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
            out.println("<title>Servlet Profile_EmployeeServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Profile_EmployeeServlet at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("employeeSession") == null) {
            response.sendRedirect("login");
            return;
        }

        Employee employee = (Employee) session.getAttribute("employeeSession");
        String action = request.getParameter("action");

        if (action == null || action.equalsIgnoreCase("view")) {
            request.setAttribute("employee", employee);
            request.getRequestDispatcher("/WEB-INF/emp_profile/view.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("edit")) {
            request.setAttribute("employee", employee);
            request.getRequestDispatcher("/WEB-INF/emp_profile/edit.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("change-password")) {
            request.getRequestDispatcher("/WEB-INF/emp_profile/changepassword.jsp").forward(request, response);
        }
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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("employeeSession") == null) {
            response.sendRedirect("login");
            return;
        }

        Employee employee = (Employee) session.getAttribute("employeeSession");
        String action = request.getParameter("action");

        if ("edit".equalsIgnoreCase(action)) {
            updateProfile(request, response, session, employee);
        } else if ("change-password".equalsIgnoreCase(action)) {
            changePassword(request, response, session, employee);
        }
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, Employee employee)
            throws ServletException, IOException {

        String empAccount = request.getParameter("empAccount");
        String empName = request.getParameter("empName");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String dobStr = request.getParameter("dob");

        Date dob = null;
        if (dobStr != null && !dobStr.isEmpty()) {
            dob = Date.valueOf(dobStr);
        }

        int result = employeeDAO.edit(
                employee.getEmpId(),
                empAccount,
                empName,
                gender,
                dob,
                phone,
                email,
                address
        );

        if (result > 0) {
            // Lấy lại dữ liệu mới từ DB
            Employee updated = employeeDAO.getElementByID(employee.getEmpId());
            // Cập nhật session
            session.setAttribute("employeeSession", updated);
            session.setAttribute("successMessage", "Profile updated successfully.");

            // Redirect về trang view
            response.sendRedirect(request.getContextPath() + "/emp_profileEmployee?action=view");
        } else {
            request.setAttribute("errorMessage", "Failed to update emp_profile.");
            request.setAttribute("employee", employee);
            request.getRequestDispatcher("/WEB-INF/emp_profile/edit.jsp").forward(request, response);
        }
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, Employee employee)
            throws ServletException, IOException {

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        String errorMessage = null;

        // --- Validation ---
        if (oldPassword == null || newPassword == null || confirmPassword == null
                || oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            errorMessage = "Please fill all fields.";
        }

        if (errorMessage == null) {
            String hashedOld = HashUtil.toMD5(oldPassword);
            if (!hashedOld.equals(employee.getPassword())) {
                errorMessage = "Old password is incorrect.";
            }
        }

        if (errorMessage == null) {
            if (!newPassword.equals(confirmPassword)) {
                errorMessage = "New password and confirmation do not match.";
            }
        }

        if (errorMessage == null) {
            // Cập nhật mật khẩu
            String hashedNew = HashUtil.toMD5(newPassword);
            int result = employeeDAO.edit(employee.getEmpId(), hashedNew);

            if (result > 0) {
                employee.setPassword(hashedNew);
                session.setAttribute("employeeSession", employee);
                session.setAttribute("successMessage", "Password changed successfully.");

                response.sendRedirect(request.getContextPath() + "/profileEmployee?action=view");
                return;
            } else {
                errorMessage = "Failed to change password. Database error.";
            }
        }

        // Nếu có lỗi thì quay lại trang đổi mật khẩu
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/emp_profile/changepassword.jsp").forward(request, response);
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
