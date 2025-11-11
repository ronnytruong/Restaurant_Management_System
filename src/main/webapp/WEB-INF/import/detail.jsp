<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Import detail">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Import Detail</h1>
            </div>
            <div class="actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                    <form action="<c:url value="import">
                              <c:param name="page" value="1"/>
                          </c:url>" method="get" class="search-box input-group">
                        <div class="search-box input-group">
                            <span class="input-group-text"><i class="bi bi-search"></i></span>
                            <input type="search" name="keyword" class="form-control" placeholder="Search by name or description">
                        </div>
                    </form>
                    <c:if test="${not empty currentImport}">
                        <a class="btn btn-primary add-btn" href="<c:url value='import'>
                               <c:param name='view' value='addDetail'/>
                               <c:param name='id' value='${currentImport.importId}'/>
                           </c:url>"><i class="bi bi-plus-circle"></i> Add Detail</a>
                    </c:if>

                </div>  
            </div>
        </div>

        <c:choose>
            <c:when test="${not empty currentImport}">
                <div class="card-body px-4 pb-4">
                    <div class="row g-3 g-md-4 mb-4">
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Import ID</small>
                                <p class="fs-5 fw-semibold mb-0">#<c:out value='${currentImport.importId}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Manager</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentImport.empName}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Supplier</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentImport.supplierName}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-sm-6 col-xl-3">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Contact</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentImport.contactPerson}'/></p>
                            </div>
                        </div>
                        <div class="col-12 col-lg-4">
                            <div class="border rounded-3 p-3 bg-light">
                                <small class="text-uppercase text-muted fw-semibold">Import date</small>
                                <p class="mb-0 fw-semibold"><c:out value='${currentImport.importDate}'/></p>
                            </div>
                        </div>
                    </div>

                    <div class="table-responsive">
                        <table class="table align-middle admin-table">
                            <thead>
                                <tr>
                                    <th scope="col">No.</th>
                                    <th scope="col">Ingredient</th>
                                    <th scope="col">Quantity</th>
                                    <th scope="col">Unit</th>
                                    <th scope="col">Unit price</th>
                                    <th scope="col">Total price</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${importDetails == null || empty importDetails}">
                                        <tr>
                                            <td colspan="6" class="text-center text-muted">No line items found for this import.</td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="detail" items="${importDetails}" varStatus="loop">
                                            <tr>
                                                <td><c:out value="${loop.index + 1}"/></td>
                                                <td><c:out value='${detail.ingredientName}'/></td>
                                                <td><c:out value='${detail.quantity}'/></td>
                                                <td><c:out value='${detail.unit}'/></td>
                                                <td><c:out value='${detail.unitPrice}'/></td>
                                                <td><c:out value='${detail.totalPrice}'/></td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                        <nav aria-label="Page navigation example">
                            <ul class="pagination">
                                <li class="page-item ${((empty param.page) || param.page <= 1) ? 'disabled' : ''}">
                                    <a class="page-link" href="<c:url value='/import'>
                                           <c:param name='view' value='detail'/>
                                           <c:param name='page' value='${(empty param.page) ? 1 : param.page - 1}'/>
                                           <c:if test='${not empty param.keyword || not empty requestScope.keyword}'><c:param name='keyword' value='${param.keyword != null ? param.keyword : requestScope.keyword }'/></c:if>
                                       </c:url>" aria-label="Previous">
                                        <span aria-hidden="true">&laquo;</span>
                                    </a>
                                </li>

                                <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                                    <li class="page-item ${((empty param.page && i == 1) || param.page == i) ? 'active' : ''}">
                                        <a class="page-link" href="<c:url value='/import'>
                                               <c:param name='view' value='detail'/>
                                               <c:param name='page' value='${i}'/>
                                               <c:if test='${not empty param.keyword || not empty requestScope.keyword}'><c:param name='keyword' value='${param.keyword != null ? param.keyword : requestScope.keyword }'/></c:if>
                                           </c:url>">${i}</a>
                                    </li>
                                </c:forEach>

                                <li class="page-item ${(requestScope.totalPages <= (empty param.page ? 1 : param.page)) ? 'disabled' : ''}">
                                    <a class="page-link" href="<c:url value='/detail '>
                                           <c:param name='view' value='list'/>
                                           <c:param name='page' value='${(empty param.page) ? 2 : param.page + 1}'/>
                                           <c:if test='${not empty param.keyword || not empty requestScope.keyword}'><c:param name='keyword' value='${param.keyword != null ? param.keyword : requestScope.keyword }'/></c:if>
                                       </c:url>" aria-label="Next">
                                        <span aria-hidden="true">&raquo;</span>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card-body px-4 pb-4">
                    <div class="alert alert-warning mb-3" role="alert">
                        Unable to find the requested import. Please verify the URL or choose another entry from the list.
                    </div>
                    <a class="btn btn-outline-dark" href="<c:url value='import'>
                           <c:param name='view' value='list'/>
                       </c:url>">Back to list</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
