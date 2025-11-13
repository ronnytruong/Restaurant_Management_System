<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Change Password - Yummy</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: #f5f5f5;
                color: #333;
            }


            /* Main Container */
            .container {
                max-width: 800px;
                margin: 60px auto;
                padding: 0 20px;
            }

            .profile-card {
                background: white;
                border-radius: 8px;
                padding: 40px;
                box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            }

            h2 {
                font-size: 32px;
                color: #333;
                margin-bottom: 10px;
                font-weight: 600;
            }

            .subtitle {
                color: #666;
                margin-bottom: 30px;
                font-size: 14px;
            }

            /* Messages */
            .message {
                padding: 12px 20px;
                border-radius: 4px;
                margin-bottom: 20px;
                font-size: 14px;
            }

            .message.success {
                background-color: #e8f5e9;
                color: #2e7d32;
                border-left: 4px solid #4caf50;
            }

            .message.error {
                background-color: #ffebee;
                color: #c62828;
                border-left: 4px solid #f44336;
            }


            .form-grid {

                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 20px;
                margin-bottom: 30px;
            }

            .form-group {
                margin-bottom: 20px;
            }

            .form-group.full-width {
                grid-column: 1 / -1;
            }

            label {
                display: block;
                font-weight: 500;
                color: #333;
                margin-bottom: 8px;
                font-size: 14px;
            }

            input, select {
                width: 100%;
                padding: 12px 15px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 14px;
                transition: border-color 0.3s;
                font-family: inherit;
            }

            input:focus, select:focus {
                outline: none;
                border-color: #d32f2f;
            }

            input[readonly] {
                background-color: #f5f5f5;
                cursor: not-allowed;
                color: #666;
            }

            /* Buttons */
            .button-group {
                display: flex;
                gap: 15px;
                align-items: center;
                margin-top: 30px;
            }

            button {
                background-color: #d32f2f;
                color: white;
                border: none;
                padding: 14px 40px;
                border-radius: 50px;
                font-size: 16px;
                font-weight: 500;
                cursor: pointer;
                transition: all 0.3s;
            }

            button:hover {
                background-color: #b71c1c;
                transform: translateY(-2px);
                box-shadow: 0 4px 12px rgba(211, 47, 47, 0.3);
            }

            .link-button {
                color: #d32f2f;
                text-decoration: none;
                font-weight: 500;
                padding: 14px 20px;
                transition: color 0.3s;

                border: none;
                background: none;
                cursor: pointer;
            }

            .link-button:hover {
                color: #b71c1c;
            }

            /* Responsive */
            @media (max-width: 768px) {
                .form-grid {
                    grid-template-columns: 1fr;
                }

                .profile-card {
                    padding: 30px 20px;
                }

                .button-group {
                    flex-direction: column;
                    width: 100%;
                }

                button, .link-button {
                    width: 100%;
                    text-align: center;
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="profile-card">
                <h2>Change Password</h2>
                <p class="subtitle">Update your customer account password securely</p>





                <c:if test="${not empty errorMessage}">
                    <div class="message error">${errorMessage}</div>
                </c:if>

                <form action="customer-profile" method="post">
                    <input type="hidden" name="action" value="change-password"/>

                    <div class="form-group">
                        <label>Enter old Password *</label>
                        <input type="password" name="oldPassword" required/>
                    </div>

                    <div class="form-group">
                        <label>Enter new Password *</label>
                        <input type="password" name="newPassword" required   
                               pattern="^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$"
                                 title="Password must be at least 8 characters long and include at least one uppercase letter, one number, and one special character."/>
                    </div>

                    <div class="form-group">
                        <label>Confirm Password *</label>
                        <input type="password" name="confirmPassword" required/>
                    </div>

                    <div class="button-group">
                        <button type="submit">Update Password</button>
                        <a href="customer-profile" class="link-button">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>