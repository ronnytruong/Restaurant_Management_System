<%-- 
    Document   : add
    Created on : Oct 25, 2025, 2:37:39 PM
    Author     : PHAT
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section">
    <div class="content-card shadow-sm px-4 py-3">
        <div class="d-flex justify-content-between align-items-center">
            <h2>Add Recipe</h2>
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/recipe">Back</a>
        </div>

        <div class="mt-3">
            <form method="post" action="${pageContext.request.contextPath}/recipe" class="row g-3">
                <input type="hidden" name="action" value="add" />
                <div class="col-md-4">
                    <label class="form-label">Menu Item</label>
                    <select name="menu_item_id" class="form-select" required>
                        <option value="" disabled selected>-- Select menu item --</option>
                        <c:forEach var="mi" items="${menuItems}">
                            <option value="${mi[0]}">${mi[1]}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4">
                    <label class="form-label">Status</label>
                    <select name="status" class="form-select">
                        <option value="Active" selected>Active</option>
                        <option value="Inactive">Inactive</option>
                    </select>
                </div>
                <div class="col-12">
                    <button type="submit" class="btn btn-success">Save</button>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/recipe">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/include/footerDashboard.jsp" %>

