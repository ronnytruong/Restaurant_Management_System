<%--
    Document   : edit
    Created on : Oct 22, 2025
    Author     : TruongBinhTrong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Ingredient Edit Form">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex justify-content-between align-items-center">
            <h1 class="section-title mb-0">Edit Ingredient</h1>
            <a class="btn btn-outline-secondary" href="<c:url value='ingredient'/>">
                <i class="bi bi-arrow-left"></i> Back to List
            </a>
        </div>

        <div class="container px-4 py-3">
            <c:choose>
                <c:when test="${not empty currentIngredient}">
                    <form method="post" action="<c:url value='ingredient'/>">
                        <%-- Hidden field for ID required for POST request --%>
                        <input type="hidden" name="id" value="${currentIngredient.ingredientId}">
                        <table class="table">
                            
                            <%-- ID (Read-only display) --%>
                            <tr>
                                <th width="30%">ID</th>
                                <td>${currentIngredient.ingredientId}</td>
                            </tr>
                            
                            <%-- Name --%>
                            <tr>
                                <th>
                                    <label for="ingredientNameInput">Name</label>
                                </th>
                                <td>
                                    <input type="text" 
                                           name="ingredientName" 
                                           id="ingredientNameInput"
                                           value="${currentIngredient.ingredientName}" 
                                           class="form-control" 
                                           required>
                                </td>
                            </tr>
                            
                            <%-- Type (Dropdown with pre-selection) --%>
                            <tr>
                                <th>
                                    <label for="typeSelect">Type</label>
                                </th>
                                <td>
                                    <select name="typeId" id="typeSelect" class="form-select" required>
                                        <c:forEach var="t" items="${typesList}">
                                            <option value="${t.typeId}" 
                                                    <c:if test="${t.typeId == currentIngredient.typeId}">selected</c:if> >
                                                ${t.typeName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            
                            <%-- Price --%>
                            <tr>
                                <th>
                                    <label for="priceInput">Price</label>
                                </th>
                                <td>
                                    <input type="number" 
                                           name="price" 
                                           id="priceInput" 
                                           value="${currentIngredient.price}" 
                                           class="form-control" 
                                           step="0.01"
                                           min="0.00"
                                           required>
                                </td>
                            </tr>
                            
                            <%-- Action Buttons (Styled to match the provided template) --%>
                            <tr>
                                <td></td>
                                <td>
                                    <button class="btn btn-success" type="submit" name="action" value="edit">
                                        Save
                                    </button>
                                    <a class="btn btn-outline-dark" href="<c:url value='ingredient'/>">
                                        Cancel
                                    </a>
                                </td>
                            </tr>
                        </table>
                    </form>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-danger" role="alert">
                        Ingredient not found or invalid ID provided.
                    </div>
                   
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
