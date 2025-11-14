<%--
    Document  : edit
    Created on : Oct 22, 2025
    Author     : TruongBinhTrong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Includes the header, navigation, and opening tags for the admin layout --%>
<title>My Profile - Yummy</title>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Ingredient Edit Form">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex justify-content-between align-items-center">
            <h1 class="section-title mb-0">Edit Ingredient</h1>
        </div>

        <div class="container px-4 py-3">
            <c:choose>
                <c:when test="${not empty currentIngredient}">
                    <form method="post" action="<c:url value='ingredient'/>">
                        <%-- Hidden field for ID required for POST request --%>
                        <input type="hidden" name="id" value="${currentIngredient.ingredientId}">
                        <table class="table">

                            <%-- Ingredient Name --%>
                            <tr>
                                <th width="30%">
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

                            <%-- Ingredient Type (Dropdown with pre-selection) --%>
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

                            <%-- Unit (Dropdown with pre-selection) --%>
                            <tr>
                                <th>
                                    <label for="unit">Unit</label>
                                </th>
                                <td>
                                    <select id="unit" name="unit" class="form-select" required>
                                        <option value="">-- Select Unit --</option>
                                        <option value="Kilogram" <c:if test="${currentIngredient.unit == 'Kilogram'}">selected</c:if>>Kilogram</option>
                                        <option value="Gram" <c:if test="${currentIngredient.unit == 'Gram'}">selected</c:if>>Gram</option>
                                        <option value="Liter" <c:if test="${currentIngredient.unit == 'Liter'}">selected</c:if>>Liter</option>
                                        <option value="Milliliter" <c:if test="${currentIngredient.unit == 'Milliliter'}">selected</c:if>>Milliliter</option>
                                        <option value="Bottle" <c:if test="${currentIngredient.unit == 'Bottle'}">selected</c:if>>Bottle</option>
                                        <option value="Can" <c:if test="${currentIngredient.unit == 'Can'}">selected</c:if>>Can</option>
                                        <option value="Box" <c:if test="${currentIngredient.unit == 'Box'}">selected</c:if>>Box</option>
                                        <option value="Piece" <c:if test="${currentIngredient.unit == 'Piece'}">selected</c:if>>Piece</option>
                                    </select>
                                </td>
                            </tr>

                            <%-- Action Buttons --%>
                            <tr>
                                <td></td>
                                <td>
                                    <button class="btn btn-success" type="submit" name="action" value="edit">Save</button>
                                    <a class="btn btn-outline-dark" href="<c:url value='ingredient'/>">Cancel</a>
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