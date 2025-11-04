<%-- 
    Document   : mylist
    Created on : Oct 28, 2025, 8:30:59 PM
    Author     : Administrator
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="My reservations">
    <div class="content-card shadow-sm">

        <!-- Header -->
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">My Reservations</h1>
            </div>
            <div class="actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                    <!-- Search box -->
                    <form action="<c:url value='/reservation'/>" method="get" class="search-box input-group">
                        <input type="hidden" name="view" value="mylist"/>
                        <input type="hidden" name="customerId" value="${requestScope.customerId}"/>
                        <input type="hidden" name="page" value="1"/>
                        <span class="input-group-text"><i class="bi bi-search"></i></span>
                        <input type="search" name="keyword" value="${param.keyword}" class="form-control" placeholder="Search table / status">
                    </form>
                </div>
            </div>
        </div>
        <!-- Table -->
        <div class="table-responsive px-4 pb-2 mt-3">
            <table class="table align-middle admin-table">
                <thead>
                    <tr>
                        <th width="8%">ID</th>
                        <th width="10%">Table</th>
                        <th width="16%">Date</th>
                        <th width="14%">Time</th>
                        <th width="10%">Party</th>
                        <th width="14%">Status</th>
                        <th width="14%" class="text-end">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty reservationList}">
                            <tr><td colspan="7" style="color:red;">No data to display</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="r" items="${reservationList}">
                                <tr>
                                    <td><c:out value="${r.customer.customerName}"/></td>
                                    <td><c:out value="${r.table.number}"/></td>
                                    <td>${r.reservationDate}</td>
                                    <td>${r.reservationTime}</td>
                                    <td>${r.partySize}</td>
                                    <td>
                                        <span class="badge 
                                              ${r.status == 'Approved' ? 'bg-success' : 
                                                (r.status == 'Rejected' ? 'bg-danger' : 
                                                (r.status == 'Cancelled' ? 'bg-secondary' : 'bg-warning text-dark'))}">
                                                  ${r.status}
                                              </span>
                                        </td>
                                        <td class="text-end">
                                            <div class="action-button-group d-flex justify-content-end gap-2">
                                                <!-- Edit -->
                                                <a class="btn btn-outline-secondary btn-icon"
                                                   href="<c:url value='/reservation'>
                                                       <c:param name='view' value='edit'/>
                                                       <c:param name='id' value='${r.reservationId}'/>
                                                       <c:param name='from' value='mylist'/>
                                                       <c:param name='customerId' value='${requestScope.customerId}'/>
                                                   </c:url>"
                                                   title="Edit" aria-label="Edit"
                                                   ${r.status != 'Pending' ? 'disabled' : ''}>
                                                    <i class="bi bi-pencil-square"></i>
                                                </a>

                                                <!-- Cancel -->
                                                <button type="button" class="btn btn-outline-danger btn-icon"
                                                        title="Cancel" aria-label="Cancel"
                                                        onclick="showCancelPopup('${r.reservationId}', '${requestScope.customerId}')"
                                                        ${r.status != 'Pending' ? 'disabled' : ''}>
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

                <!-- Pagination -->
                <nav aria-label="Page navigation">
                    <ul class="pagination">
                        <li class="page-item ${((empty param.page) || param.page <= 1)?'disabled':''}">
                            <a class="page-link" href="<c:url value='/reservation'>
                                   <c:param name='view' value='mylist'/>
                                   <c:param name='customerId' value='${requestScope.customerId}'/>
                                   <c:param name='page' value='${param.page - 1}'/>
                                   <c:param name='keyword' value='${param.keyword}'/>
                               </c:url>" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>

                        <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                            <li class="page-item ${((empty param.page && i == 1) || param.page == i)?'active':''}">
                                <a class="page-link" href="<c:url value='/reservation'>
                                       <c:param name='view' value='mylist'/>
                                       <c:param name='customerId' value='${requestScope.customerId}'/>
                                       <c:param name='page' value='${i}'/>
                                       <c:param name='keyword' value='${param.keyword}'/>
                                   </c:url>">${i}</a>
                            </li>
                        </c:forEach>

                        <li class="page-item ${(requestScope.totalPages <= param.page || requestScope.totalPages eq 1)?'disabled':''}">
                            <a class="page-link" href="<c:url value='/reservation'>
                                   <c:param name='view' value='mylist'/>
                                   <c:param name='customerId' value='${requestScope.customerId}'/>
                                   <c:param name='page' value='${(empty param.page)?2:param.page + 1}'/>
                                   <c:param name='keyword' value='${param.keyword}'/>
                               </c:url>" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </section>

    <!-- Cancel confirmation modal -->
    <div class="modal" id="cancelPopup" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Confirm Cancellation</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body text-danger">
                    <h6 id="idForCancelPopup">Are you sure you want to cancel this reservation?</h6>
                    <small class="text-muted">This action cannot be undone.</small>
                </div>
                <form method="post" action="<c:url value='/reservation'/>">
                    <input type="hidden" name="action" value="cancel"/>
                    <input type="hidden" id="hiddenReservationId" name="id" value=""/>
                    <input type="hidden" id="hiddenCustomerId" name="customerId" value="${requestScope.customerId}"/>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-danger">Cancel Reservation</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        function showCancelPopup(resId, cusId) {
            document.getElementById('hiddenReservationId').value = resId;
            document.getElementById('hiddenCustomerId').value = cusId;
            document.getElementById('idForCancelPopup').innerText = 'Cancel reservation #' + resId + '?';
            var modal = new bootstrap.Modal(document.getElementById('cancelPopup'));
            modal.show();
        }
    </script>

    <%@include file="/WEB-INF/include/footerDashboard.jsp" %>
