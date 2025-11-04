<%-- 
    Document   : add
    Created on : Oct 28, 2025, 8:30:59 PM
    Author     : Administrator
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Add reservation">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex justify-content-between align-items-center">
            <h1 class="section-title mb-0">Add Reservation</h1>
            <a class="btn btn-outline-secondary" href="<c:url value='/reservation'>
                   <c:param name='view' value='list'/>
                   <c:param name='page' value='${empty param.page ? 1 : param.page}'/>
               </c:url>">
                <i class="bi bi-arrow-left"></i> Back
            </a>
        </div>

        <div class="px-4 pb-4">
            <form class="row g-3" method="post" action="<c:url value='/reservation'/>">
                <input type="hidden" name="action" value="add"/>

                <!-- ✅ Customer ID -->
                <div class="col-12 col-md-3">
                    <label class="form-label">Customer ID</label>
                    <input type="number" name="customerId" class="form-control" required placeholder="e.g. 1">
                </div>

                <!-- ✅ Table -->
                <div class="col-12 col-md-3">
                    <label class="form-label">Table ID</label>
                    <input type="number" name="tableId" class="form-control" required placeholder="e.g. 3">
                    <small class="text-muted">Enter Table ID (T1 = 1, T2 = 2...)</small>
                </div>

                <div class="col-12 col-md-3">
                    <label class="form-label">Date</label>
                    <input type="date" name="reservationDate" class="form-control" required>
                </div>

                <div class="col-12 col-md-3">
                    <label class="form-label">Time</label>
                    <input type="time" name="reservationTime" class="form-control" required>
                </div>

                <div class="col-12 col-md-3">
                    <label class="form-label">Party Size</label>
                    <input type="number" name="partySize" class="form-control" min="1" required placeholder="e.g. 4">
                </div>

                <div class="col-12 d-flex gap-2">
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-save2"></i> Save
                    </button>
                    <a class="btn btn-outline-secondary" href="<c:url value='/reservation'>
                           <c:param name='view' value='list'/>
                       </c:url>">Cancel</a>
                </div>
            </form>

            <small class="text-muted d-block mt-2">
                <strong>Pending</strong>.
            </small>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>

