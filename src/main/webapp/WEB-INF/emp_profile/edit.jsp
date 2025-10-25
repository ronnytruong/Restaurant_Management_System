<%-- 
    Document   : edit
    Created on : Oct 25, 2025, 3:45:34 PM
    Author     : PHAT
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Edit Employee">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row justify-content-between align-items-md-center">
            <h1 class="section-title mb-1">Edit Profile</h1>
        </div>

        <div class="container">
            <form method="post" action="${pageContext.request.contextPath}/profileEmployee">
                <input type="hidden" name="id" value="${employee.empId}" />
                <table class="table">
                    <tr>
                        <th><label for="empAccount">Account</label></th>
                        <td>
                            <input type="text" name="empAccount" id="empAccount" value="${employee.empAccount}" class="form-control" readonly>
                        </td>
                    </tr>
                    <tr>
                        <th><label for="empName">Full Name</label></th>
                        <td>
                            <input type="text" name="empName" id="empName" value="${employee.empName}" class="form-control" required>
                        </td>
                    </tr>
                    <tr>
                        <th><label for="email">Email</label></th>
                        <td>
                            <input type="email" name="email" id="email" value="${employee.email}" class="form-control" required>
                        </td>
                    </tr>
                    <tr>
                        <th><label for="phone">Phone</label></th>
                        <td>
                            <input type="text" name="phone" id="phone" value="${employee.phoneNumber}" class="form-control">
                        </td>
                    </tr>  
                    <tr>
                        <th><label for="address">Address</label></th>
                        <td>
                            <input type="text" name="address" id="address" value="${employee.address}" class="form-control">
                        </td>
                    </tr>
                    <tr>
                        <th><label for="gender">Gender</label></th>
                        <td>
                            <select name="gender" id="gender" class="form-control">
                                <option value="" ${employee.gender == null ? "selected" : ""}>Select Gender</option>
                                <option value="Male" ${employee.gender == 'Male' ? "selected" : ""}>Male</option>
                                <option value="Female" ${employee.gender == 'Female' ? "selected" : ""}>Female</option>
                                <option value="Other" ${employee.gender == 'Other' ? "selected" : ""}>Other</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th><label for="dob">Date of Birth</label></th>
                        <td>
                            <input type="date" name="dob" id="dob" value="<c:out value='${employee.dob}'/>" class="form-control">
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <button class="btn btn-outline-success" type="submit" name="action" value="edit">Save</button>
                            <a class="btn btn-outline-dark" href="${pageContext.request.contextPath}/profileEmployee?action=view">Cancel</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/include/footerDashboard.jsp" %>