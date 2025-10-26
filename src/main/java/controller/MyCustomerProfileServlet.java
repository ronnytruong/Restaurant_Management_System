package controller;

import dao.CustomerDAO;
import model.Customer;
import constant.HashUtil;

import java.io.IOException;
import java.sql.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Huynh Thai Duy Phuong - CE190603
 */
@WebServlet(name = "CustomerProfileServlet", urlPatterns = {"/customer-profile"})
public class MyCustomerProfileServlet extends HttpServlet {

    private CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customerSession") == null) {
            response.sendRedirect("login");
            return;
        }

        Customer customer = (Customer) session.getAttribute("customerSession");
        String action = request.getParameter("action");

        if (action == null || action.equalsIgnoreCase("view")) {
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("/WEB-INF/profile/view.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("edit")) {
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("/WEB-INF/profile/edit.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("change-password")) {
            request.getRequestDispatcher("/WEB-INF/profile/changepassword.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customerSession") == null) {
            response.sendRedirect("login");
            return;
        }

        Customer customer = (Customer) session.getAttribute("customerSession");
        String action = request.getParameter("action");

        if ("edit".equalsIgnoreCase(action)) {
            updateProfile(request, response, session, customer);
        } else if ("change-password".equalsIgnoreCase(action)) {
            changePassword(request, response, session, customer);
        }
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, Customer customer)
            throws ServletException, IOException {

        String name = request.getParameter("customer_name");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone_number");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String dobStr = request.getParameter("dob");

        Date dob = null;
        if (dobStr != null && !dobStr.isEmpty()) {
            dob = Date.valueOf(dobStr);
        }

        int result = customerDAO.edit(
                customer.getCustomerId(),
                customer.getCustomerAccount(),
                name,
                gender,
                phone,
                email,
                address,
                dob
        );

        if (result > 0) {
            // Lấy dữ liệu mới từ DB
            Customer updated = customerDAO.getElementByID(customer.getCustomerId());

            // Cập nhật lại session
            session.setAttribute("customerSession", updated);

            // Thông báo thành công (lưu tạm trong session)
            session.setAttribute("successMessage", "Profile updated successfully.");

            // Redirect về trang view
            response.sendRedirect(request.getContextPath() + "/customer-profile?action=view");
            return;
        } else {
            // Nếu lỗi thì giữ nguyên form edit và hiển thị lỗi
            request.setAttribute("errorMessage", "Failed to update profile.");
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("/WEB-INF/profile/edit.jsp").forward(request, response);
        }
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, Customer customer)
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
            if (!hashedOld.equals(customer.getPassword())) {
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
            int result = customerDAO.edit(customer.getCustomerId(), hashedNew);

            if (result > 0) {

                customer.setPassword(hashedNew);
                session.setAttribute("customerSession", customer);

                response.sendRedirect(request.getContextPath() + "/customer-profile");
                return;
            } else {

                errorMessage = "Failed to change password. Database error.";
            }
        }

        request.setAttribute("errorMessage", errorMessage);

        request.getRequestDispatcher("/WEB-INF/profile/changepassword.jsp").forward(request, response);
    }
}
