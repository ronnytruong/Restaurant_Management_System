<%-- 
    Document   : edit
    Created on : Oct 25, 2025, 2:37:45 PM
    Author     : PHAT
--%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section">
    <div class="content-card shadow-sm px-4 py-3">
        <div class="d-flex justify-content-between align-items-center">
            <h2>Edit Recipe</h2>
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/recipe">Back</a>
        </div>

        <c:choose>
            <c:when test="${not empty currentRecipe}">
                <form method="post" action="${pageContext.request.contextPath}/recipe" class="row g-3 mt-3">
                    <input type="hidden" name="action" value="edit" />
                    <input type="hidden" name="id" value="${currentRecipe.recipeId}" />
                    <div class="col-md-3">
                        <label class="form-label">Recipe ID</label>
                        <input class="form-control" value="${currentRecipe.recipeId}" disabled/>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Menu Item</label>
                        <select name="menu_item_id" class="form-select" required>
                            <c:forEach var="mi" items="${menuItems}">
                                <option value="${mi[0]}" ${mi[0] == currentRecipe.menuItemId ? 'selected' : ''}>${mi[1]}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">Status</label>
                        <select name="status" class="form-select">
                            <option value="Active" ${currentRecipe.status == 'Active' ? 'selected' : ''}>Active</option>
                            <option value="Inactive" ${currentRecipe.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                           
                        </select>
                    </div>

                    <div class="col-12">
                        <button type="submit" class="btn btn-success">Save</button>
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/recipe">Cancel</a>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <div class="alert alert-warning mt-3">Recipe not found for id <c:out value="${param.id}"/></div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<%@ include file="/WEB-INF/include/footerDashboard.jsp" %>


