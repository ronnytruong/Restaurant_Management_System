<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Edit Profile - Yummy</title>
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

            /* Profile Info Form Styling */
            .form-group {
                margin-bottom: 10px;
            }

            .info-row {
                display: flex;
                padding: 16px 0;
                border-bottom: 1px solid #f0f0f0;
            }

            .info-row:last-of-type {
                border-bottom: none;
            }

            .info-label {
                flex: 0 0 180px;
                font-weight: 600;
                color: #555;
                font-size: 14px;
                align-self: center; /* Vertically align label and input */
            }

            .info-value {
                flex: 1;
                color: #333;
                font-size: 14px;
            }

            .info-value input[type="text"],
            .info-value input[type="email"],
            .info-value input[type="date"],
            .info-value select {
                width: 100%;
                padding: 10px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 14px;
            }

            .info-value input[readonly] {
                background-color: #eee;
                cursor: not-allowed;
            }

            .info-value.empty {
                color: #999;
                font-style: italic;
            }

            /* Buttons */
            .button-group {
                display: flex;
                gap: 15px;
                margin-top: 30px;
                padding-top: 20px; /* Space above buttons */
                border-top: 1px solid #f0f0f0;
            }

            .btn {
                display: inline-block;
                padding: 14px 40px;
                border-radius: 50px;
                font-size: 16px;
                font-weight: 500;
                text-decoration: none;
                text-align: center;
                transition: all 0.3s;
                cursor: pointer;
                border: none; /* Make buttons consistent */
            }

            .btn-primary {
                background-color: #d32f2f;
                color: white;
            }

            .btn-primary:hover {
                background-color: #b71c1c;
                transform: translateY(-2px);
                box-shadow: 0 4px 12px rgba(211, 47, 47, 0.3);
            }

            .btn-secondary {
                background-color: white;
                color: #d32f2f;
                border: 2px solid #d32f2f;
            }

            .btn-secondary:hover {
                background-color: #d32f2f;
                color: white;
            }

            /* Responsive */
            @media (max-width: 768px) {
                .profile-card {
                    padding: 30px 20px;
                }

                .info-row {
                    flex-direction: column;
                    gap: 8px;
                }

                .info-label {
                    flex: none;
                }

                .button-group {
                    flex-direction: column;
                }

                .btn {
                    width: 100%;
                }
            }
        </style>
    </head>
    <body>


        <div class="container">
            <div class="profile-card">
                <h2>Edit My Profile</h2>
                <p class="subtitle">Update your account information below</p>

                <c:if test="${not empty successMessage}">
                    <div class="message success">${successMessage}</div>
                </c:if>

                <c:if test="${not empty errorMessage}">
                    <div class="message error">${errorMessage}</div>
                </c:if>

                <form action="customer-profile" method="post">
                    <input type="hidden" name="action" value="edit"/>

                    <div class="info-row">
                        <div class="info-label">Username</div>
                        <div class="info-value">
                            <input type="text" value="${customer.customerAccount}" readonly />
                        </div>
                    </div>

                    <div class="info-row">
                        <div class="info-label">Full Name</div>
                        <div class="info-value">
                            <input type="text" name="customer_name" value="${customer.customerName}" required/>
                        </div>
                    </div>

                    <div class="info-row">
                        <div class="info-label">Gender</div>
                        <div class="info-value">
                            <select name="gender">
                                <option value="Male" <c:if test="${customer.gender eq 'Male'}">selected</c:if>>Male</option>
                                <option value="Female" <c:if test="${customer.gender eq 'Female'}">selected</c:if>>Female</option>
                                </select>
                            </div>
                        </div>

                        <div class="info-row">
                            <div class="info-label">Phone Number</div>
                            <div class="info-value">
                                <input type="text" name="phone_number" value="${customer.phoneNumber}"/>
                        </div>
                    </div>

                    <div class="info-row">
                        <div class="info-label">Email Address</div>
                        <div class="info-value">
                            <input type="email" name="email" value="${customer.email}"/>
                        </div>
                    </div>

                    <div class="info-row">
                        <div class="info-label">Address</div>
                        <div class="info-value">
                            <input type="text" name="address" value="${customer.address}"/>
                        </div>
                    </div>

                    <div class="info-row">
                        <div class="info-label">Date of Birth</div>
                        <div class="info-value">
                            <input type="date" name="dob" value="${customer.dob}"/>
                        </div>
                    </div>

                    <div class="button-group">
                        <button type="submit" class="btn btn-primary">Save Changes</button>
                        <a href="customer-profile?action=view" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>