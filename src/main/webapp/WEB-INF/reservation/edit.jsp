<%-- 
    Document   : edit
    Created on : Oct 28, 2025, 8:30:59 PM
    Author     : Administrator
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Edit reservation">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex justify-content-between align-items-center">
            <h1 class="section-title mb-0">
                Edit Reservation
                <small class="text-muted">#<c:out value="${currentReservation.reservationId}"/></small>
            </h1>
            <a class="btn btn-outline-secondary" href="<c:url value='/reservation'>
               <c:param name='view' value='list'/>
               <c:param name='page' value='${empty param.page ? 1 : param.page}'/>
               </c:url>">
                <i class="bi bi-arrow-left"></i> Back
            </a>
        </div>

        <c:choose>
            <c:when test="${currentReservation == null}">
                <div class="px-4 pb-4 text-danger">Reservation not found.</div>
            </c:when>
            <c:otherwise>
                <div class="px-4 pb-4">
                    <form class="row g-3" method="post" action="<c:url value='/reservation'/>">
                        <input type="hidden" name="action" value="edit"/>
                        <input type="hidden" name="reservationId" value="${currentReservation.reservationId}"/>

                        <div class="col-12 col-md-3">
                            <label class="form-label">Customer ID</label>
                            <input type="number" class="form-control" value="${currentReservation.customerId}" disabled>
                        </div>

                        <div class="col-12 col-md-3">
                            <label class="form-label">Table ID</label>
                            <input type="number" name="tableId" class="form-control" required
                                   value="${currentReservation.tableId}">
                        </div>

                        <div class="col-12 col-md-3">
                            <label class="form-label">Date</label>
                            <input type="date" name="reservationDate" class="form-control" required
                                   value="${currentReservation.reservationDate}">
                        </div>

                        <div class="col-12 col-md-3">
                            <label class="form-label">Time</label>
                            <input type="time" name="reservationTime" class="form-control" required
                                   value="${currentReservation.reservationTime}">
                        </div>

                        <div class="col-12 col-md-3">
                            <label class="form-label">Party Size</label>
                            <input type="number" name="partySize" class="form-control" min="1" required
                                   value="${currentReservation.partySize}">
                        </div>

                        <div class="col-12 col-md-3">
                            <label class="form-label">Status</label>
                            <select name="status" class="form-select" required>
                                <option value="Pending"   ${currentReservation.status == 'Pending'   ? 'selected' : ''}>Pending</option>
                                <option value="Approved"  ${currentReservation.status == 'Approved'  ? 'selected' : ''}>Approved</option>
                                <option value="Rejected"  ${currentReservation.status == 'Rejected'  ? 'selected' : ''}>Rejected</option>
                                <option value="Cancelled" ${currentReservation.status == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                            </select>
                        </div>

                        <div class="col-12 d-flex gap-2">
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-save2"></i> Save Changes
                            </button>
                            <a class="btn btn-outline-secondary" href="<c:url value='/reservation'>
                               <c:param name='view' value='list'/>
                               </c:url>">Cancel</a>
                        </div>
                    </form>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>

