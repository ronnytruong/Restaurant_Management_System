<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Inventory usage report">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-lg-row gap-3 gap-lg-0 justify-content-between align-items-lg-center">
            <div>
                <h1 class="section-title mb-1">Inventory Usage Report</h1>
                <p class="text-muted mb-0">Review ingredients consumed by completed orders and deduct them from stock.</p>
            </div>
            <form method="get" action="<c:url value='inventory-report'/>" class="d-flex flex-column flex-sm-row gap-2 align-items-stretch align-items-sm-center">
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-calendar-event"></i></span>
                    <input type="date" class="form-control" name="date" value="${reportDate}" aria-label="Report date selector">
                </div>
                <button type="submit" class="btn btn-outline-primary"><i class="bi bi-arrow-repeat"></i> View</button>
            </form>
        </div>

        <div class="card-body px-4 pb-4">
            <c:if test="${alreadyProcessed}">
                <div class="alert alert-info d-flex align-items-center gap-2" role="alert">
                    <i class="bi bi-info-circle"></i>
                    <span>Report for <strong>${reportDate}</strong> has already been finalized<c:if test="${not empty processedByName}"> by <strong>${processedByName}</strong></c:if><c:if test="${not empty processedAt}"> at <strong>${processedAt}</strong></c:if>.</span>
                </div>
            </c:if>

            <div class="row g-3 g-md-4 mb-4">
                <div class="col-12 col-md-4">
                    <div class="border rounded-3 p-3 bg-light h-100">
                        <small class="text-uppercase text-muted fw-semibold">Completed orders</small>
                        <p class="fs-4 fw-semibold mb-0">${summary.completedOrders}</p>
                    </div>
                </div>
                <div class="col-12 col-md-4">
                    <div class="border rounded-3 p-3 bg-light h-100">
                        <small class="text-uppercase text-muted fw-semibold">Menu items prepared</small>
                        <p class="fs-4 fw-semibold mb-0">${summary.itemsPrepared}</p>
                    </div>
                </div>
                <div class="col-12 col-md-4">
                    <div class="border rounded-3 p-3 bg-light h-100">
                        <small class="text-uppercase text-muted fw-semibold">Total ingredient usage</small>
                        <p class="fs-4 fw-semibold mb-0">
                            <fmt:formatNumber value="${totalUsage}" maxFractionDigits="2" minFractionDigits="0"/>
                        </p>
                    </div>
                </div>
            </div>

            <c:choose>
                <c:when test="${hasData}">
                    <div class="table-responsive border rounded-3">
                        <table class="table align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th scope="col">#</th>
                                    <th scope="col">Ingredient</th>
                                    <th scope="col">Unit</th>
                                    <th scope="col" class="text-end">Stock before</th>
                                    <th scope="col" class="text-end">Used</th>
                                    <th scope="col" class="text-end">Remaining</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${usageList}" varStatus="loop">
                                    <tr>
                                        <td>${loop.index + 1}</td>
                                        <td>${item.ingredientName}</td>
                                        <td>${item.unit}</td>
                                        <td class="text-end"><fmt:formatNumber value="${item.stockBefore}" maxFractionDigits="2" minFractionDigits="0"/></td>
                                        <td class="text-end text-danger fw-semibold"><fmt:formatNumber value="${item.quantityUsed}" maxFractionDigits="2" minFractionDigits="0"/></td>
                                        <td class="text-end"><fmt:formatNumber value="${item.stockAfter}" maxFractionDigits="2" minFractionDigits="0"/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <c:if test="${not alreadyProcessed}">
                        <form method="post" action="<c:url value='inventory-report'/>" class="mt-4 d-flex justify-content-end">
                            <input type="hidden" name="action" value="finalize">
                            <input type="hidden" name="date" value="${reportDate}">
                            <button type="submit" class="btn btn-primary"><i class="bi bi-clipboard-check"></i> Finalize &amp; Deduct Stock</button>
                        </form>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-warning" role="alert">
                        No completed orders found for <strong>${reportDate}</strong>. Stock remains unchanged.
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>
