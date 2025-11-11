package controller;

import dao.CustomerDAO;
import model.Customer;
import db.DBContext;

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
    private DBContext db = new DBContext();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customerSession") == null) {
            response.sendRedirect("login");
            return;
        }
        
        String successMessage = (String) session.getAttribute("successMessage");
    if (successMessage != null) {
        request.setAttribute("successMessage", successMessage);
        session.removeAttribute("successMessage"); // **Dòng quan trọng: Xóa ngay lập tức**
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
        String errorMessage = null;
        Date today = new Date(System.currentTimeMillis());
        long YEARDATE = 1000L * 60 * 60 * 24 * 365;
        // validation
        if (dob != null && dob.after(today) ) {
            errorMessage = "Invalid date of birth.";
        } else if (dob != null && ((today.getTime() - dob.getTime()) / (YEARDATE)) < 18) {
            errorMessage = "Invalid date of birth. You must be at least 18 years old.";
        } else if (dob != null && ((today.getTime() - dob.getTime()) / (YEARDATE)) > 100){
             errorMessage = "Invalid date of birth.";
    }else if (!email.equalsIgnoreCase(customer.getEmail()) && customerDAO.checkEmailExist(email)) {
            errorMessage = "Email already exists. This email is already taken by another account";
        } else if (!phone.equals(customer.getPhoneNumber()) && customerDAO.checkPhoneExist(phone)) {
            errorMessage = "Phone number already exists. This phone is already taken by another account";
        }

        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("/WEB-INF/profile/edit.jsp").forward(request, response);
            return;
        }

// If any validation failed
        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("/WEB-INF/profile/edit.jsp").forward(request, response);
            return;
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
            Customer updated = customerDAO.getElementByID(customer.getCustomerId());
            session.setAttribute("customerSession", updated);
            session.setAttribute("successMessage", "Profile updated successfully.");
            response.sendRedirect(request.getContextPath() + "/customer-profile?action=view");
            return;
        } else {
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

        if (oldPassword == null || newPassword == null || confirmPassword == null
                || oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            errorMessage = "Please fill all fields.";
        }

        if (errorMessage == null) {
            String hashedOld = db.hashToMD5(oldPassword);
            if (!hashedOld.equals(customer.getPassword())) {
                errorMessage = "Old password is incorrect.";
            }
        }

        if (errorMessage == null) {
            if (!newPassword.equals(confirmPassword)) {
                errorMessage = "New password and confirmation do not match.";
            }
        }

        if (errorMessage == null) {

            String hashedNew = db.hashToMD5(newPassword);
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
