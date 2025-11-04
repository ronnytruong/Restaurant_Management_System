<%-- 
    Document   : edit
    Created on : Oct 25, 2025, 2:37:45 PM
    Author     : PHAT
--%>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10">
    <div class="content-card shadow-sm">
        <div class="card-header px-4 py-3">
            <h1 class="section-title mb-1">Edit Recipe</h1>
        </div>

        <div class="px-4 pb-4">
            <c:if test="${empty currentRecipe}">
                <div class="alert alert-danger">Recipe not found.</div>
            </c:if>

            <c:if test="${not empty currentRecipe}">
                <form method="post" action="<c:url value='recipe'/>" class="row g-3">
                    <input type="hidden" name="action" value="edit"/>
                    <input type="hidden" name="id" value="${currentRecipe.recipeId}"/>

                    <div class="col-md-6">
                        <label class="form-label">Recipe ID</label>
                        <input type="text" class="form-control" value="${currentRecipe.recipeId}" disabled>
                    </div>

                    <div class="col-md-6">
                        <label for="recipeName" class="form-label">Recipe Name</label>
                        <input id="recipeName" name="recipe_name" type="text" class="form-control" value="${currentRecipe.recipeName}" required />
                    </div>

<!--                    <div class="col-md-3">
                        <label for="statusSelect" class="form-label">Status</label>
                        <select id="statusSelect" name="status" class="form-select" required>
                            <option value="Active" ${currentRecipe.status == 'Active' ? 'selected="selected"' : ''}>Active</option>
                            <option value="Inactive" ${currentRecipe.status == 'Inactive' ? 'selected="selected"' : ''}>Inactive</option>
                        </select>
                    </div>-->

                    <div class="col-12 d-flex gap-2 justify-content-end">
                        <a class="btn btn-secondary" href="<c:url value='recipe'/>">Cancel</a>
                        <button type="submit" class="btn btn-primary">Save changes</button>
                    </div>
                </form>
            </c:if>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>