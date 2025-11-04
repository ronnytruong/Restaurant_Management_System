<%-- 
    Document   : view
    Created on : Oct 25, 2025, 2:37:56 PM
    Author     : PHAT
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section">
    <div class="content-card shadow-sm px-4 py-3">
        <div class="d-flex justify-content-between align-items-center">
            <h2>Recipe Detail</h2>

        </div>

        <c:if test="${empty currentRecipe}">
            <div class="alert alert-warning mt-3">Recipe not found.</div>
        </c:if>

        <c:if test="${not empty currentRecipe}">
            <div class="mt-3">
                <dl class="row">
                    <dt class="col-sm-3">Recipe ID</dt>
                    <dd class="col-sm-9"><c:out value="${currentRecipe.recipeId}" /></dd>

                    <dt class="col-sm-3">Recipe Name</dt>
                    <dd class="col-sm-9"><c:out value="${currentRecipe.recipeName}" /></dd>

<!--                    <dt class="col-sm-3">Status</dt>
                    <dd class="col-sm-9"><c:out value="${currentRecipe.status}" /></dd>-->
                </dl>
            </div>

            <div class="mt-4">
                <h4>Items</h4>
                <c:if test="${not empty popupMessage}">
                    <div class="${popupStatus ? 'alert alert-success' : 'alert alert-danger'}">${popupMessage}</div>
                </c:if>

                <div class="table-responsive">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Ingredient</th>
                                <th>Ingredient ID</th>
                                <th>Quantity</th>
                                <th>Unit</th>
                                <th>Note</th>
<!--                                <th>Status</th>-->
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty currentRecipe.items}">
                                    <tr><td colspan="8" class="text-center text-muted">No items</td></tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="it" items="${currentRecipe.items}">
                                        <tr>
                                            <td><c:out value="${it.recipeItemId}" /></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty it.ingredientName}">
                                                        <c:out value="${it.ingredientName}" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        Unknown
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><c:out value="${it.ingredientId}" /></td>
                                            <td><c:out value="${it.quantity}" /></td>
                                            <td><c:out value="${it.unit}" /></td>
                                            <td><c:out value="${it.note}" /></td>
<%--                                            <td><c:out value="${it.status}" /></td>--%>
                                            <td class="text-end">
                                                <div class="action-button-group d-flex justify-content-end gap-2">
                                                    <!-- Edit icon -->
                                                    <button type="button" class="btn btn-outline-secondary btn-icon btn-edit"
                                                            title="Edit Item" aria-label="Edit"
                                                            onclick='openEditItemModal(${it.recipeItemId}, ${it.ingredientId}, "${it.quantity}", "${fn:escapeXml(it.unit)}", "${fn:escapeXml(it.note)}", "${it.status}")'>
                                                        <i class="bi bi-pencil"></i>
                                                    </button>

                                                    <!-- Delete icon -->
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
            </div>

            <div class="mt-4">
                <h4>Add Item</h4>
                <form method="post" action="${pageContext.request.contextPath}/recipe" class="row g-2">
                    <input type="hidden" name="action" value="add_item" />
                    <input type="hidden" name="recipe_id" value="${currentRecipe.recipeId}" />
                    <div class="col-md-3">
                        <label class="form-label">Ingredient</label>
                        <select name="ingredient_id" class="form-select" required>
                            <option value="">-- Select ingredient --</option>
                            <c:forEach var="ing" items="${ingredients}">
                                <option value="${ing.ingredientId}">${ing.ingredientName}</option>
                            </c:forEach>
                        </select>
                        <c:if test="${empty ingredients}">
                            <div class="form-text text-danger">No ingredients available. Please add ingredients first.</div>
                        </c:if>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label">Quantity</label>
                        <input name="quantity" type="number" step="0.01" class="form-control" required />
                    </div>
                    <div class="col-md-2">
                        <label class="form-label">Unit</label>
                        <input name="unit" type="text" class="form-control" />
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">Note</label>
                        <input name="note" type="text" class="form-control" />
                    </div>
                    <div class="col-md-2 d-flex align-items-end">
                        <button class="btn btn-success w-100" type="submit">Add Item</button>
                    </div>
                </form>
            </div>
        </c:if>
    </div>
</section>

<!-- Edit item modal -->
<div class="modal fade" id="editItemModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="editItemForm" method="post" action="${pageContext.request.contextPath}/recipe">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Item</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="action" value="edit_item"/>
                    <input type="hidden" id="edit_recipe_item_id" name="recipe_item_id" value=""/>
                    <div class="mb-2">
                        <label class="form-label">Ingredient</label>
                        <select id="edit_ingredient_id" name="ingredient_id" class="form-select" required>
                            <option value="">-- Select ingredient --</option>
                            <c:forEach var="ing" items="${ingredients}">
                                <option value="${ing.ingredientId}">${ing.ingredientName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Quantity</label>
                        <input id="edit_quantity" name="quantity" type="number" step="0.01" class="form-control" />
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Unit</label>
                        <input id="edit_unit" name="unit" type="text" class="form-control" />
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Note</label>
                        <input id="edit_note" name="note" type="text" class="form-control" />
                    </div>
<!--                    <div class="mb-2">
                        <label class="form-label">Status</label>
                        <select id="edit_status" name="status" class="form-select">
                            <option value="Active">Active</option>
                            <option value="Inactive">Inactive</option>
                            <option value="Deleted">Deleted</option>
                        </select>
                    </div>-->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save changes</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Delete item modal -->
<div class="modal fade" id="deleteItemPopup" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-sm modal-dialog-centered">
        <div class="modal-content">
            <form method="post" action="${pageContext.request.contextPath}/recipe">
                <div class="modal-header"><h5 class="modal-title">Confirm Delete</h5></div>
                <div class="modal-body text-danger">
                    <p>Are you sure to delete this item?</p>
                    <input type="hidden" id="hiddenDeleteRecipeItemId" name="recipe_item_id" value=""/>
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
    function openEditItemModal(recipeItemId, ingredientId, quantity, unit, note, status) {
        document.getElementById('edit_recipe_item_id').value = recipeItemId;
        document.getElementById('edit_ingredient_id').value = ingredientId;
        document.getElementById('edit_quantity').value = quantity;
        document.getElementById('edit_unit').value = unit || '';
        document.getElementById('edit_note').value = note || '';
//        document.getElementById('edit_status').value = status || 'Active';
        var modal = new bootstrap.Modal(document.getElementById('editItemModal'));
        modal.show();
    }

    function showDeleteItemPopup(id) {
        document.getElementById('hiddenDeleteRecipeItemId').value = id;
        var modal = new bootstrap.Modal(document.getElementById('deleteItemPopup'));
        modal.show();
    }
</script>

<%@ include file="/WEB-INF/include/footerDashboard.jsp" %>

