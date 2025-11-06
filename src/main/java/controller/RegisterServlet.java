/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;


import db.DBContext;
import dao.CustomerDAO;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private CustomerDAO customerDAO = new CustomerDAO();
    private DBContext db = new DBContext();
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    private void setPopup(HttpServletRequest request, boolean status, String message) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute("popupStatus", status);
            session.setAttribute("popupMessage", message);
        }
    }
    private String getSqlErrorCode(int temp_code) {
      
     
        final int DUPLICATE_KEY_CONST = -10; 
        final int FOREIGN_KEY_VIOLATION_CONST = -20; 
        final int NULL_INSERT_VIOLATION_CONST = -30;
        final int UNIQUE_INDEX_CONST = -40; 
        
        if (temp_code == DUPLICATE_KEY_CONST) {
            return "DUPLICATE_KEY (Account or other unique field already exists)";
        } else if (temp_code == FOREIGN_KEY_VIOLATION_CONST) {
            return "FOREIGN_KEY_VIOLATION";
        } else if (temp_code == NULL_INSERT_VIOLATION_CONST) {
            return "NULL_INSERT_VIOLATION";
        } else if (temp_code == UNIQUE_INDEX_CONST) {
            return "DUPLICATE_UNIQUE";
        }

        return "Unknown Error Code: " + temp_code;
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            
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
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phone");
        //optional, post authentication
String gender = null; 
        String address = null;
        java.sql.Date dob = null;

        boolean registerSuccess = true;
        String errorMessage = "";

        // Validation
     if (isNullOrEmpty(customerAccount)
                || isNullOrEmpty(password)
                || isNullOrEmpty(customerName)
                || isNullOrEmpty(email)
                || isNullOrEmpty(phoneNumber)) {
            
            registerSuccess = false;
            errorMessage = "All fields marked with (*) are required. Please fill them out.";
            
        } else if (!password.equals(confirmPassword)) {
           
            registerSuccess = false;
            errorMessage = "Password and Confirm Password don't match.";
            
        } else if (customerDAO.checkAccountExist(customerAccount)) {
          
            registerSuccess = false;
            errorMessage = "Username already exists. Try another.";
        }

        if (registerSuccess) {
            
            String hashedPassword = db.hashToMD5(password);

           
            int checkError = customerDAO.add(customerAccount, hashedPassword, customerName, 
                                            gender, phoneNumber, email, address, dob);

            if (checkError >= 1) {
                
                setPopup(request, true, "Register successfully! Sign in to connect.");
                response.sendRedirect("login");
                return;
                
            } else {
                
                registerSuccess = false;
                errorMessage = "Register failed. Database Error: " + getSqlErrorCode(checkError);
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
