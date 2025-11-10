<%-- 
    Document   : view-emp
    Created on : Oct 28, 2025, 9:38:16 PM
    Author     : PHAT
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <title>My Profile - Yummy Restaurant</title>
        <meta name="description" content="View and edit your personal account information.">
        <meta name="keywords" content="employee profile, account settings">

        <link href="assets/img/favicon.png" rel="icon">
        <link href="assets/img/apple-touch-icon.png" rel="apple-touch-icon">

        <link href="https://fonts.googleapis.com" rel="preconnect">
        <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;1,300;1,400;1,500;1,700&family=Inter:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">

        <link href="assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="assets/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
        <link href="assets/vendor/aos/aos.css" rel="stylesheet">
        <link href="assets/vendor/glightbox/css/glightbox.min.css" rel="stylesheet">
        <link href="assets/vendor/swiper/swiper-bundle.min.css" rel="stylesheet">

        <link href="assets/css/main.css" rel="stylesheet">

        <style>
            .profile-section .card {
                box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
                border: none;
            }
            .info-label {
                font-weight: 600;
                color: #7a7a7a;
            }
            .info-value.empty {
                color: #ccc;
                font-style: italic;
            }
            .btn-yummy-primary {
                background-color: #ce1212;
                border-color: #ce1212;
                color: white;
                transition: all 0.3s;
            }
            .btn-yummy-primary:hover {
                background-color: #a00f0f;
                border-color: #a00f0f;
                transform: translateY(-1px);
            }
            .btn-outline-yummy {
                color: #ce1212;
                border-color: #ce1212;
                transition: all 0.3s;
            }
            .btn-outline-yummy:hover {
                background-color: #ce1212;
                color: white;
            }
        </style>
    </head>

    <body class="inner-page">

        <%-- Include Employee Header (nav only Dashboard) --%>
        <%@ include file="/WEB-INF/include/employeeHeader.jsp" %>

<<<<<<< HEAD
        <main class="main">
            <section id="profile" class="profile-section pt-5 section">
                <div class="container" data-aos="fade-up" data-aos-delay="100">

                    <div class="row justify-content-center">
                        <div class="col-lg-8">
                            <div class="card p-4 p-md-5">

                                <%-- Messages --%>
                                <c:if test="${not empty successMessage}">
                                    <div class="alert alert-success d-flex align-items-center" role="alert">
                                        <i class="bi bi-check-circle-fill me-2"></i>
                                        <div>${successMessage}</div>
                                    </div>
                                </c:if>

                                <c:if test="${not empty errorMessage}">
                                    <div class="alert alert-danger d-flex align-items-center" role="alert">
                                        <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                        <div>${errorMessage}</div>
                                    </div>
                                </c:if>

                                <h3 class="mb-4 text-center">Profile Information</h3>

                                <div class="profile-info">
                                    <div class="row py-3 border-bottom">
                                        <div class="col-sm-4 info-label">Username</div>
                                        <div class="col-sm-8 info-value">
                                            <strong>${employee.empAccount}</strong>
                                        </div>
                                    </div>

                                    <div class="row py-3 border-bottom">
                                        <div class="col-sm-4 info-label">Role</div>
                                        <div class="col-sm-8 info-value">
                                            <c:choose>
                                                <c:when test="${not empty employee.roleName}">
                                                    ${employee.roleName}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="empty">Not provided</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="row py-3 border-bottom">
                                        <div class="col-sm-4 info-label">Full Name</div>
                                        <div class="col-sm-8 info-value">
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

                                    <div class="row py-3 border-bottom">
                                        <div class="col-sm-4 info-label">Gender</div>
                                        <div class="col-sm-8 info-value">
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

                                    <div class="row py-3 border-bottom">
                                        <div class="col-sm-4 info-label">Phone Number</div>
                                        <div class="col-sm-8 info-value">
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

                                    <div class="row py-3 border-bottom">
                                        <div class="col-sm-4 info-label">Email Address</div>
                                        <div class="col-sm-8 info-value">
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

                                    <div class="row py-3 border-bottom">
                                        <div class="col-sm-4 info-label">Address</div>
                                        <div class="col-sm-8 info-value">
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

                                    <div class="row py-3">
                                        <div class="col-sm-4 info-label">Date of Birth</div>
                                        <div class="col-sm-8 info-value">
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

                                <div class="text-center mt-4 pt-4 border-top">
                                    <a href="employee-profile?action=edit" class="btn btn-yummy-primary px-5 py-2 me-3">
                                        <i class="bi bi-pencil-square me-1"></i> Edit Profile
                                    </a>
                                    <a href="employee-profile?action=change-password" class="btn btn-outline-yummy px-5 py-2">
                                        <i class="bi bi-key me-1"></i> Change Password
                                    </a>
                                </div>

                            </div>

                </div>
            </section>
        </main>

        <%-- Include Footer (reuse customer footer) --%>
        <%@ include file="/WEB-INF/include/customerFooter.jsp" %>
    </body>
</html>