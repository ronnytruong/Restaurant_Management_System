<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Edit Profile - Yummy Restaurant"/>
<%@include file="/WEB-INF/include/headerCustomer.jsp" %>

<main class="main">
    <section id="edit-profile" class="profile-section pt-5 section">
        <div class="container" data-aos="fade-up" data-aos-delay="100">

            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="card p-4 p-md-5">

                        <h3 class="mb-4 text-center">Edit My Profile</h3>
                        <p class="subtitle text-center text-muted mb-4">Update your account information below</p>

                        <form action="customer-profile" method="post">
                            <input type="hidden" name="action" value="edit"/>

                            <div class="row py-3 border-bottom">
                                <div class="col-sm-4 info-label">Username</div>
                                <div class="col-sm-8 info-value">
                                    <input type="text" class="form-control" value="${customer.customerAccount}" readonly />
                                </div>
                            </div>
                            <div class="row py-3 border-bottom">
                                <div class="col-sm-4 info-label">Full Name</div>
                                <div class="col-sm-8 info-value">
                                    <input type="text" class="form-control" name="customer_name" value="${customer.customerName}" required/>
                                </div>
                            </div>
                            <div class="row py-3 border-bottom">
                                <div class="col-sm-4 info-label">Gender</div>
                                <div class="col-sm-8 info-value">
                                    <select name="gender" class="form-select">
                                        <option value="Male" <c:if test="${customer.gender eq 'Male'}">selected</c:if>>Male</option>
                                        <option value="Female" <c:if test="${customer.gender eq 'Female'}">selected</c:if>>Female</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="row py-3 border-bottom">
                                    <div class="col-sm-4 info-label">Phone Number</div>
                                    <div class="col-sm-8 info-value">
                                        <input type="text" class="form-control" name="phone_number" value="${customer.phoneNumber}"/>
                                </div>
                            </div>
                            <div class="row py-3 border-bottom">
                                <div class="col-sm-4 info-label">Email Address</div>
                                <div class="col-sm-8 info-value">
                                    <input type="email" class="form-control" name="email" value="${customer.email}"/>
                                </div>
                            </div>
                            <div class="row py-3 border-bottom">
                                <div class="col-sm-4 info-label">Address</div>
                                <div class="col-sm-8 info-value">
                                    <input type="text" class="form-control" name="address" value="${customer.address}"/>
                                </div>
                            </div>
                            <div class="row py-3">
                                <div class="col-sm-4 info-label">Date of Birth</div>
                                <div class="col-sm-8 info-value">
                                    <input type="date" class="form-control" name="dob" value="${customer.dob}"/>
                                </div>
                            </div>

                            <div class="text-center mt-4 pt-4 border-top">
                                <button type="submit" class="btn btn-danger px-5 py-2 me-3">
                                    <i class="bi bi-save me-1"></i> Save Changes
                                </button>
                                <a href="customer-profile?action=view" class="btn btn-outline-danger px-5 py-2">
                                    Cancel
                                </a>
                            </div>
                        </form>

                    </div>
                </div>
            </div>
        </div>
    </section>
</main>

<c:if test="${requestScope.popupStatus != null and requestScope.popupStatus == false}">
    <div class="modal" id="exampleModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Action Fail</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p style="color: red">Error: <c:out value="${requestScope.popupMessage}"/></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        // Use window.onload to ensure Bootstrap JS is fully loaded from the footer
        window.onload = function () {
            var myModal = new bootstrap.Modal(document.getElementById('exampleModal'));
            myModal.show();
        };
    </script>
</c:if>

<%@include file="/WEB-INF/include/footerCustomer.jsp" %>