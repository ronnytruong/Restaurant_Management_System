<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Form section">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row justify-content-between align-items-center">
            <h1 class="section-title mb-1">Add Import Detail</h1>
        </div>

        <div class="container">
            <form method="post" action="<c:url value='import'/>">
                <table class="table">
                    <tr><td></td><td></td></tr>

                    <tr>
                        <th>
                            <label for="importId">Import ID</label>
                        </th>
                        <td>
                            <input type="number" name="importId" id="importId" value="${currentImport.importId}" class="form-control" readonly>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            <label for="empName">Manager</label>
                        </th>
                        <td>
                            <input type="text" name="empName" id="empName" value="${currentImport.empName}" class="form-control" readonly>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            <label for="supplierName">Supplier</label>
                        </th>
                        <td>
                            <input type="text" name="supplierName" id="supplierName" value="${currentImport.supplierName}" class="form-control" readonly>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            <label for="contactPerson">Contact Person</label>
                        </th>
                        <td>
                            <input type="text" name="contactPerson" id="contactPerson" value="${currentImport.contactPerson}" class="form-control" readonly>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            <label for="importDate">Import Date</label>
                        </th>
                        <td>
                            <input type="date" name="importDate" id="importDate" value="${currentImport.importDate}" class="form-control" readonly>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            <label for="ingredientId">Ingredient Name</label>
                        </th>
                        <td>
                            <select name="ingredientId" class="form-select">    
                                <c:forEach var="ing" items="${ingredientList}">
                                    <option value="${ing.ingredientId}" required class="form-control">${ing.ingredientName}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            <label for="quantity">Quantity</label>
                        </th>
                        <td>
                            <input type="number" name="quantity" id="quantity" class="form-control" required>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            <label for="unit">Unit</label>
                        </th>
                        <td>
                            <div style="display: flex; gap: 10px; align-items: center;">
                                <select id="unit" name="unit" onchange="toggleBoxInput()" class="form-select">
                                    <option value="">-- Select Unit --</option>
                                    <option value="Bottle">Bottle</option>
                                    <option value="Gram">Gram</option>
                                    <option value="Liter">Liter</option>
                                    <option value="Piece">Piece</option>
                                    <option value="Crate">Crate</option>
                                </select>
                            </div>
                        </td>
                    </tr>

                    <tr>
                        <th>
                            <label for="unitPrice">Unit Price</label>
                        </th>
                        <td>
                            <input type="number" name="unitPrice" id="unitPrice" class="form-control" required>
                        </td>
                    </tr>

                    <tr>
                        <td>
                        </td>
                        <td>
                            <button class="btn btn-success" type="submit" name="action" value="addDetail">Save</button>
                            <a class="btn btn-outline-dark" href="<c:url value='import?view=detail&id=${currentImport.importId}'/>">Cancel</a>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</section>


<%@include file="/WEB-INF/include/footerDashboard.jsp" %>