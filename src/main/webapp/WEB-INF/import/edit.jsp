<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Edit import">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row justify-content-between align-items-center">
            <h1 class="section-title mb-1">Edit Import</h1>
            <a class="btn btn-outline-dark" href="<c:url value='import'>
                   <c:param name='view' value='list'/>
               </c:url>">Cancel</a>
        </div>

        <div class="container pb-4">
            <c:choose>
                <c:when test="${not empty currentImport}">
                    <form method="post" action="<c:url value='import'/>" class="row g-3">
                        <input type="hidden" name="id" value="<c:out value='${currentImport.importId}'/>">

                        <div class="col-12 col-md-6">
                            <label for="supplierId" class="form-label">Supplier</label>
                            <select name="supplierId" id="supplierId" class="form-select">
                                <c:forEach var="supplier" items="${supplierList}">
                                    <option value="${supplier.supplierId}" ${supplier.supplierId == currentImport.supplierId ? 'selected' : ''}><c:out value='${supplier.supplierName}'/></option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-12 col-md-6">
                            <label for="empId" class="form-label">Manager</label>
                            <select name="empId" id="empId" class="form-select">
                                <c:forEach var="emp" items="${employeeList}">
                                    <option value="${emp.empId}" ${emp.empId == currentImport.empId ? 'selected' : ''}><c:out value='${emp.empName}'/></option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-12 col-md-6">
                            <label class="form-label">Contact person</label>
                            <input type="text" class="form-control" value="<c:out value='${currentImport.contactPerson}'/>" readonly>
                        </div>

                        <div class="col-12 col-md-6">
                            <label for="importDate" class="form-label">Import date</label>
                            <input type="date" name="importDate" id="importDate" class="form-control" value="<c:out value='${currentImport.importDate}'/>">
                        </div>

                        <div class="col-12 d-flex gap-2">
                            <button class="btn btn-success" type="submit" name="action" value="edit">Save</button>
                            <a class="btn btn-outline-secondary" href="<c:url value='import'>
                                   <c:param name='view' value='list'/>
                               </c:url>">Back to list</a>
                        </div>
                    </form>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-warning mt-3" role="alert">
                        Unable to find the import with id <c:out value='${param.id}'/>. Please check the information.
                    </div>
                    <a class="btn btn-outline-dark" href="<c:url value='import'>
                           <c:param name='view' value='list'/>
                       </c:url>">Back</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
