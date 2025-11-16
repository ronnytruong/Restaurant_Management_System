<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Inventory usage history">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Usage History</h1>
                <p class="text-muted mb-0">Review previously recorded inventory deductions by processing date.</p>
            </div>
            <form method="get" action="<c:url value='inventory-report'/>" class="d-flex flex-column flex-sm-row gap-2 align-items-stretch align-items-sm-center">
                <input type="hidden" name="view" value="list" />
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-search"></i></span>
                    <input type="text" class="form-control" name="keyword" value="${keyword}" placeholder="Filter by date (yyyy-mm-dd) or staff">
                </div>
                <button type="submit" class="btn btn-outline-primary"><i class="bi bi-funnel"></i> Search</button>
            </form>
        </div>

        <div class="card-body px-4 pb-4">
            <c:choose>
                <c:when test="${not empty usageDays}">
                    <div class="table-responsive border rounded-3">
                        <table class="table align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th scope="col">Usage Date</th>
                                    <th scope="col" class="text-center">Ingredients</th>
                                    <th scope="col" class="text-end">Total Usage</th>
                                    <th scope="col">Processed By</th>
                                    <th scope="col">Processed At</th>
                                    <th scope="col" class="text-end">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${usageDays}">
                                    <tr>
                                        <td>${item.usageDate}</td>
                                        <td class="text-center">${item.ingredientCount}</td>
                                        <td class="text-end"><fmt:formatNumber value="${item.totalUsage}" maxFractionDigits="2" minFractionDigits="0"/></td>
                                        <td>${item.processedBy != null ? item.processedBy : 'N/A'}</td>
                                        <td>${item.processedAt}</td>
                                        <td class="text-end">
                                            <a class="btn btn-sm btn-outline-primary" href="<c:url value='inventory-report'>
                                                    <c:param name='view' value='detail'/>
                                                    <c:param name='date' value='${item.usageDate}'/>
                                                </c:url>"><i class="bi bi-eye"></i> Detail</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <c:if test="${totalPages > 1}">
                        <nav class="mt-3" aria-label="Usage pagination">
                            <ul class="pagination mb-0">
                                <c:forEach var="page" begin="1" end="${totalPages}">
                                    <li class="page-item ${page == currentPage ? 'active' : ''}">
                                        <a class="page-link" href="<c:url value='inventory-report'>
                                                <c:param name='view' value='list'/>
                                                <c:param name='page' value='${page}'/>
                                                <c:param name='keyword' value='${keyword}'/>
                                            </c:url>">${page}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-warning mb-0" role="alert">
                        No recorded usage entries match the current filter.
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
