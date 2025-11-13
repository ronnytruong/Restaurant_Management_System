<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="title" value="My Profile - Yummy Restaurant"/>
<%@include file="/WEB-INF/include/headerCustomer.jsp" %>

<main class="main">
    <section id="profile" class="profile-section pt-5 section">
        <div class="container" data-aos="fade-up" data-aos-delay="100">

            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="card p-4 p-md-5">

                        <h3 class="mb-4 text-center">Profile Information</h3>

                        <div class="profile-info">
                            <div class="row py-3 border-bottom">
                                <div class="col-sm-4 info-label">Username</div>
                                <div class="col-sm-8 info-value">
                                    <strong>${customer.customerAccount}</strong>
                                </div>
                            </div>
                            <div class="row py-3 border-bottom">
                                <div class="col-sm-4 info-label">Full Name</div>
                                <div class="col-sm-8 info-value">
                                    <c:choose>
                                        <c:when test="${not empty customer.customerName}">${customer.customerName}</c:when>
                                        <c:otherwise><span class="empty">Not provided</span></c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="row py-3 border-bottom">
                                <div class="col-sm-4 info-label">Gender</div>
                                <div class="col-sm-8 info-value">
                                    <c:choose>
                                        <c:when test="${not empty customer.gender}">${customer.gender}</c:when>
                                        <c:otherwise><span class="empty">Not provided</span></c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="row py-3 border-bottom">
                                <div class="col-sm-4 info-label">Phone Number</div>
                                <div class="col-sm-8 info-value">
                                    <c:choose>
                                        <c:when test="${not empty customer.phoneNumber}">${customer.phoneNumber}</c:when>
                                        <c:otherwise><span class="empty">Not provided</span></c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="row py-3 border-bottom">
                                <div class="col-sm-4 info-label">Email Address</div>
                                <div class="col-sm-8 info-value">
                                    <c:choose>
                                        <c:when test="${not empty customer.email}">${customer.email}</c:when>
                                        <c:otherwise><span class="empty">Not provided</span></c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="row py-3 border-bottom">
                                <div class="col-sm-4 info-label">Address</div>
                                <div class="col-sm-8 info-value">
                                    <c:choose>
                                        <c:when test="${not empty customer.address}">${customer.address}</c:when>
                                        <c:otherwise><span class="empty">Not provided</span></c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="row py-3">
                                <div class="col-sm-4 info-label">Date of Birth</div>
                                <div class="col-sm-8 info-value">
                                    <c:choose>
                                        <c:when test="${not empty customer.dob}">${customer.dob}</c:when>
                                        <c:otherwise><span class="empty">Not provided</span></c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>


                        <div class="text-center mt-4 pt-4 border-top">
                            <a href="customer-profile?action=edit" class="btn btn-danger px-5 py-2 me-3">
                                <i class="bi bi-pencil-square me-1"></i> Edit Profile
                            </a>
                            <a href="customer-profile?action=change-password" class="btn btn-outline-danger px-5 py-2">
                                <i class="bi bi-key me-1"></i> Change Password
                            </a>
                        </div>

                    </div>
                </div>
            </div>

        </div>
    </section>
</main>


<c:if test="${sessionScope.popupStatus != null}">
    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">
                        Action Successful
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p style="color: green;">
                        <c:out value="${sessionScope.popupMessage}"/>
                    </p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        var myModal = new bootstrap.Modal(document.getElementById('exampleModal'));
        myModal.show();
    </script>
</c:if>

<%@include file="/WEB-INF/include/footerCustomer.jsp" %>
</body>
</html>