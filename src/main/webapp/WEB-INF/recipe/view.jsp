<%-- 
    Document   : view
    Created on : Oct 25, 2025, 2:37:56 PM
    Author     : PHAT
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/headerDashboard.jsp" %>
<style>
    /* Tăng kích thước và cân bằng các info card */
    .info-row .info-card {
        min-height: 110px; /* tăng chiều cao để ko bị khoảng trống */
        display: flex;
        flex-direction: column;
        justify-content: center;
        padding: 20px;
        border-radius: .5rem;
        background: #f8f9fa;
    }
    .info-row .info-card small {
        letter-spacing: .03em;
    }
    .info-row .info-card p {
        margin: 0;
        font-weight: 600;
    }
    /* Nếu muốn các card cùng chiều cao ở cùng 1 hàng */
    .info-row .col-md-4 {
        display: flex;
    }
    .info-row .col-md-4 > .info-card {
        flex: 1;
    }

    /* Button style (giữ consistent) */
    .btn-pill {
        border-radius: 999px;
        padding-left: 14px;
        padding-right: 14px;
    }
</style>
<section class="col-12 col-lg-9 col-xxl-10 table-section">
    <div class="content-card shadow-sm px-4 py-3">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2>Recipe Detail</h2>

        </div>

        <c:choose>
            <c:when test="${empty currentRecipe}">
                <div class="alert alert-warning mt-3">Recipe not found.</div>
            </c:when>
            <c:otherwise>

                <!-- Info cards: 3 cards rộng, đều, không khoảng trống -->
                <div class="row g-3 g-md-4 mb-3 info-row">
                    <div class="col-12 col-md-4">
                        <div class="info-card border rounded-3">
                            <small class="text-uppercase text-muted fw-semibold">Recipe ID</small>
                            <p class="fs-5 fw-semibold">#<c:out value='${currentRecipe.recipeId}'/></p>
                        </div>
                    </div>

                    <div class="col-12 col-md-4">
                        <div class="info-card border rounded-3">
                            <small class="text-uppercase text-muted fw-semibold">Recipe Name</small>
                            <p class="mb-0 fw-semibold"><c:out value='${currentRecipe.recipeName}'/></p>
                        </div>
                    </div>

                    <div class="col-12 col-md-4">
                        <div class="info-card border rounded-3">
                            <small class="text-uppercase text-muted fw-semibold">Items count</small>
                            <p class="mb-0 fw-semibold"><c:out value='${fn:length(currentRecipe.items)}'/> item(s)</p>
                        </div>
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col-12 text-end">
                        <a href="${pageContext.request.contextPath}/recipe?view=add-item&id=${currentRecipe.recipeId}"
                           class="btn btn-primary btn-pill">
                            <i class="bi bi-plus-circle"></i> Add Item
                        </a>
                    </div>
                </div>
                

                <!-- Items table -->
                <div class="table-responsive mt-3">
                    <table class="table align-middle admin-table">
                        <thead>
                            <tr>
                                <th scope="col">No.</th>
                                <th scope="col">Ingredient</th>
                                <th scope="col">Ingredient ID</th>
                                <th scope="col">Quantity</th>
                                <th scope="col">Unit</th>
                                <th scope="col">Note</th>
                                <th scope="col" class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty currentRecipe.items}">
                                    <tr>
                                        <td colspan="7" class="text-center text-muted">No items</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="it" items="${currentRecipe.items}" varStatus="loop">
                                        <tr>
                                            <td><c:out value="${loop.index + 1}"/></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty it.ingredientName}">
                                                        <c:out value="${it.ingredientName}" />
                                                    </c:when>
                                                    <c:otherwise>Unknown</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><c:out value="${it.ingredientId}" /></td>
                                            <td><c:out value="${it.quantity}" /></td>
                                            <td><c:out value="${it.unit}" /></td>
                                            <td><c:out value="${it.note}" /></td>
                                            <td class="text-end">
                                                <div class="action-button-group d-flex justify-content-end gap-2">
                                                    <!-- Edit: go to edit-item page -->
                                                    <a href="${pageContext.request.contextPath}/recipe?view=edit-item&recipe_item_id=${it.recipeItemId}"
                                                       class="btn btn-outline-secondary btn-icon" title="Edit Item" aria-label="Edit">
                                                        <i class="bi bi-pencil"></i>
                                                    </a>

                                                    <!-- Delete -->
                                                    <button type="button" class="btn btn-outline-danger btn-icon btn-delete"
                                                            title="Delete Item" aria-label="Delete"
                                                            onclick="showDeleteItemPopup(${it.recipeItemId})">
                                                        <i class="bi bi-x-circle"></i>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<!-- Delete item modal -->
<div class="modal fade" id="deleteItemPopup" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content">
            <form method="post" action="${pageContext.request.contextPath}/recipe">
                <div class="modal-header"><h5 class="modal-title">Confirm Delete</h5></div>
                <div class="modal-body text-danger">
                    <p>Are you sure to delete this item?</p>
                    <input type="hidden" id="hiddenDeleteRecipeItemId" name="recipe_item_id" value=""/>
                    <input type="hidden" name="recipe_id" value="${currentRecipe.recipeId}">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" name="action" value="delete_item" class="btn btn-danger">Delete</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    function showDeleteItemPopup(id) {
        document.getElementById('hiddenDeleteRecipeItemId').value = id;
        var modal = new bootstrap.Modal(document.getElementById('deleteItemPopup'));
        modal.show();
    }
</script>

<%@ include file="/WEB-INF/include/footerDashboard.jsp" %>