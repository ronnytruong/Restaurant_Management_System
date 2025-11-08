<%-- 
    Document   : view-emp
    Created on : Oct 28, 2025, 9:38:16 PM
    Author     : PHAT
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>My Profile - Yummy</title>
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
            .profile-info {
                margin-bottom: 30px;
            }
            .info-row {
                display: flex;
                padding: 16px 0;
                border-bottom: 1px solid #f0f0f0;
            }
            .info-row:last-child {
                border-bottom: none;
            }
            .info-label {
                flex: 0 0 180px;
                font-weight: 600;
                color: #555;
                font-size: 14px;
            }
            .info-value {
                flex: 1;
                color: #333;
                font-size: 14px;
            }
            .info-value.empty {
                color: #999;
                font-style: italic;
            }
            .button-group {
                display: flex;
                gap: 15px;
                margin-top: 30px;
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
                .header-content {
                    flex-direction: column;
                    gap: 15px;
                }
                .nav {
                    display: flex;
                    flex-wrap: wrap;
                    gap: 15px;
                }
                .nav a {
                    margin-left: 0;
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="profile-card">
                <h2>My Profile</h2>
                <p class="subtitle">View your account information</p>

                <c:if test="${not empty successMessage}">
                    <div class="message success">${successMessage}</div>
                </c:if>

                <c:if test="${not empty errorMessage}">
                    <div class="message error">${errorMessage}</div>
                </c:if>

                <div class="profile-info">
                    <div class="info-row">
                        <div class="info-label">Username</div>
                        <div class="info-value">${employee.empAccount}</div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">Role</div>
                        <div class="info-value">
                            <c:choose>  
                                <c:when test="${not empty employee.role.name}">
                                    ${employee.role.name}
                                </c:when>
                                <c:otherwise>
                                    <span class="empty">Not provided</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">Full Name</div>
                        <div class="info-value">
                            <c:choose>
                                <c:when test="${not empty employee.empName}">
                                    ${employee.empName}
                                </c:when>
                                <c:otherwise>
                                    <span class="empty">Not provided</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">Gender</div>
                        <div class="info-value">
                            <c:choose>
                                <c:when test="${not empty employee.gender}">
                                    ${employee.gender}
                                </c:when>
                                <c:otherwise>
                                    <span class="empty">Not provided</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">Phone Number</div>
                        <div class="info-value">
                            <c:choose>
                                <c:when test="${not empty employee.phoneNumber}">
                                    ${employee.phoneNumber}
                                </c:when>
                                <c:otherwise>
                                    <span class="empty">Not provided</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">Email Address</div>
                        <div class="info-value">
                            <c:choose>
                                <c:when test="${not empty employee.email}">
                                    ${employee.email}
                                </c:when>
                                <c:otherwise>
                                    <span class="empty">Not provided</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">Address</div>
                        <div class="info-value">
                            <c:choose>
                                <c:when test="${not empty employee.address}">
                                    ${employee.address}
                                </c:when>
                                <c:otherwise>
                                    <span class="empty">Not provided</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="info-row">
                        <div class="info-label">Date of Birth</div>
                        <div class="info-value">
                            <c:choose>
                                <c:when test="${not empty employee.dob}">
                                    ${employee.dob}
                                </c:when>
                                <c:otherwise>
                                    <span class="empty">Not provided</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <div class="button-group">
                    <a href="employee-profile?action=edit" class="btn btn-primary">Edit Profile</a>
                    <a href="employee-profile?action=change-password" class="btn btn-secondary">Change Password</a>
                </div>
            </div>
        </div>
    </body>
</html>
