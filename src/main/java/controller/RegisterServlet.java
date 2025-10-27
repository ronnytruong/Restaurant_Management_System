/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import static constant.CommonFunction.getSqlErrorCode;
import static constant.CommonFunction.setPopup; // Used for setting success/error messages, if needed
import static constant.CommonFunction.validateString; // Assuming this function is for basic string null/empty check
import constant.HashUtil;
import dao.CustomerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Huynh Thai Duy Phuong - CE190603
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    CustomerDAO customerDAO = new CustomerDAO();

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
            /* Basic default output */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegisterServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method. Loads the registration form
     * page.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to the registration JSP page
        request.getRequestDispatcher("/WEB-INF/authentication/register.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method. Processes the registration
     * form submission.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String customerAccount = request.getParameter("account");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");
        String customerName = request.getParameter("name");

        //optional
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phone");

        boolean registerSuccess = true;
        String errorMessage = "";

        // Validation
        if (!validateString(customerAccount, -1)
                || !validateString(password, -1)
                || !validateString(customerName, -1)) {
            registerSuccess = false;
            errorMessage = "Please enter valid Username and Password.";
        } else if (!password.equals(confirmPassword)) {
            // B. Check password match
            registerSuccess = false;
            errorMessage = "Password and Confirm Password don't match.";
        } else if (customerDAO.checkAccountExist(customerAccount)) {
            // C. Check account existence
            registerSuccess = false;
            errorMessage = "Username already exists. Try another.";
        }

        // Process registration
        if (registerSuccess) {

            String hashedPassword = HashUtil.toMD5(password);

            int checkError = customerDAO.add(customerAccount, hashedPassword, customerName);

            if (checkError >= 1) {

                setPopup(request, true, "Register successfully! Sign in to connect.");
                response.sendRedirect("login");
                return;
            } else {

                registerSuccess = false;
                errorMessage = "Register failed. Error:" + getSqlErrorCode(checkError);
            }
        }

        if (!registerSuccess) {

            request.setAttribute("error", errorMessage);
            request.setAttribute("account", customerAccount);
            request.setAttribute("name", customerName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phoneNumber);

            request.getRequestDispatcher("/WEB-INF/authentication/register.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Handles user registration for customers";
    }// </editor-fold>

}
