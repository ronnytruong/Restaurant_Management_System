<%-- 
    Document   : view
    Created on : Oct 25, 2025, 3:45:00 PM
    Author     : PHAT
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Employee Profile">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex justify-content-between align-items-center">
            <h1 class="section-title mb-0">My Profile</h1>
            <div>
                <a href="${pageContext.request.contextPath}/profileEmployee?action=edit" class="btn btn-outline-primary me-2">Edit Profile</a>
                <a href="${pageContext.request.contextPath}/profileEmployee?action=change-password" class="btn btn-outline-secondary">Change Password</a>
            </div>
        </div>

        <div class="card-body px-4 py-3">
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">${successMessage}</div>
            </c:if>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>

            <c:if test="${not empty employee}">
                <div class="row mb-3">
                    <div class="col-md-3 fw-semibold">Username:</div>
                    <div class="col-md-9"><c:out value="${employee.empAccount}"/></div>
                </div>

                <div class="row mb-3">
                    <div class="col-md-3 fw-semibold">Full Name:</div>
                    <div class="col-md-9"><c:out value="${employee.empName}"/></div>
                </div>

                <div class="row mb-3">
                    <div class="col-md-3 fw-semibold">Gender:</div>
                    <div class="col-md-9"><c:out value="${employee.gender}"/></div>
                </div>

                <div class="row mb-3">
                    <div class="col-md-3 fw-semibold">Date of Birth:</div>
                    <div class="col-md-9">
                        <c:choose>
                            <c:when test="${not empty employee.dob}">
                                <fmt:formatDate value="${employee.dob}" pattern="dd-MM-yyyy"/>
                            </c:when>
                            <c:otherwise>
                                <span class="text-muted">Not provided</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col-md-3 fw-semibold">Phone:</div>
                    <div class="col-md-9">
                        <c:choose>
                            <c:when test="${not empty employee.phoneNumber}">
                                <c:out value="${employee.phoneNumber}"/>
                            </c:when>
                            <c:otherwise>
                                <span class="text-muted">Not provided</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col-md-3 fw-semibold">Email:</div>
                    <div class="col-md-9">
                        <c:choose>
                            <c:when test="${not empty employee.email}">
                                <c:out value="${employee.email}"/>
                            </c:when>
                            <c:otherwise>
                                <span class="text-muted">Not provided</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col-md-3 fw-semibold">Address:</div>
                    <div class="col-md-9">
                        <c:choose>
                            <c:when test="${not empty employee.address}">
                                <c:out value="${employee.address}"/>
                            </c:when>
                            <c:otherwise>
                                <span class="text-muted">Not provided</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="row mb-3">
                    <div class="col-md-3 fw-semibold">Role:</div>
                    <div class="col-md-9"><c:out value="${employee.roleName}"/></div>
                </div>

                
            </c:if>
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/include/footerDashboard.jsp" %>