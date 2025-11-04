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
@WebServlet(name = "MyEmployeeProfileServlet", urlPatterns = {"/employee-profile"})
public class MyEmployeeProfileServlet extends HttpServlet {

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
            out.println("<title>Servlet MyEmployeeProfileServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MyEmployeeProfileServlet at " + request.getContextPath() + "</h1>");
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
            request.getRequestDispatcher("/WEB-INF/profile/view-emp.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("edit")) {
            request.setAttribute("employee", employee);
            request.getRequestDispatcher("/WEB-INF/profile/edit-emp.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("change-password")) {
            request.getRequestDispatcher("/WEB-INF/profile/changepassword-emp.jsp").forward(request, response);
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

        String name = request.getParameter("emp_name");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone_number");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String dobStr = request.getParameter("dob");

        Date dob = null;
        if (dobStr != null && !dobStr.isEmpty()) {
            dob = Date.valueOf(dobStr);
        }

        int result = employeeDAO.edit(
                employee.getEmpId(),
                employee.getEmpAccount(),
                name,
                gender,
                dob,
                phone,
                email,
                address
        );

        if (result > 0) {
            // Lấy dữ liệu mới từ DB
            Employee updated = employeeDAO.getElementByID(employee.getEmpId());

            // Cập nhật lại session
            session.setAttribute("employeeSession", updated);

            // Thông báo thành công (lưu tạm trong session)
            session.setAttribute("successMessage", "Profile updated successfully.");

            // Redirect về trang view
            response.sendRedirect(request.getContextPath() + "/employee-profile?action=view");
            return;
        } else {
            // Nếu lỗi thì giữ nguyên form edit và hiển thị lỗi
            request.setAttribute("errorMessage", "Failed to update profile.");
            request.setAttribute("employee", employee);
            request.getRequestDispatcher("/WEB-INF/employee/profile/edit.jsp").forward(request, response);
        }
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, Employee employee)
            throws ServletException, IOException {

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        String errorMessage = null;

        // --- Validation Checks ---
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

        // --- Execution or Final Forward ---
        if (errorMessage == null) {
            // SUCCESS PATH: No errors found, perform the update.
            String hashedNew = HashUtil.toMD5(newPassword);
            int result = employeeDAO.edit(employee.getEmpId(), hashedNew);

            if (result > 0) {
                employee.setPassword(hashedNew);
                session.setAttribute("employeeSession", employee);

                response.sendRedirect(request.getContextPath() + "/employee-profile");
                return;
            } else {
                errorMessage = "Failed to change password. Database error.";
            }
        }

        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/employee/profile/changepassword.jsp").forward(request, response);
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
