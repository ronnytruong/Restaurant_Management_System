<%-- 
    Document   : list
    Created on : Oct 15, 2025, 5:09:24 PM
    Author     : TruongBinhTrong
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <title>Dashboard</title>
        <meta name="description" content="Administrative listing page for managing orders and employees at Yummy Restaurant.">
        <meta name="keywords" content="admin dashboard, restaurant orders, employee management">

        <!-- Favicons -->
        <link href="assets/img/favicon.png" rel="icon">
        <link href="assets/img/apple-touch-icon.png" rel="apple-touch-icon">

        <!-- Fonts -->
        <link href="https://fonts.googleapis.com" rel="preconnect">
        <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&family=Inter:wght@100;200;300;400;500;600;700;800;900&family=Amatic+SC:wght@400;700&display=swap" rel="stylesheet">

        <!-- Vendor CSS Files -->
        <link href="assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="assets/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
        <link href="assets/vendor/aos/aos.css" rel="stylesheet">

        <!-- Main Site CSS -->
        <link href="assets/css/main.css" rel="stylesheet">

        <!-- Admin Listing CSS -->
        <link href="assets/css/dashboard.css" rel="stylesheet">
    </head>

    <body class="admin-list-page">
        <header class="admin-header shadow-sm">
            <div class="container-fluid d-flex align-items-center justify-content-between">
                <div class="brand d-flex align-items-center gap-3">
                    <div class="brand-info">
                        <h2 class="brand-name mb-0">Dashboard</h2>
                        <p class="brand-subtitle mb-0">Staff &amp; Operations</p>
                    </div>
                </div>
                <div class="header-actions d-flex align-items-center gap-3">

                    <div class="profile-chip d-flex align-items-center gap-2">
                        <div class="avatar">A</div>
                        <div>
                            <p class="mb-0 fw-semibold">Alex Morgan</p>
                            <span class="role">Administrator</span>
                        </div>
                    </div>
                </div>
            </div>
        </header>

        <main class="admin-layout">
            <div class="container-fluid">
                <div class="row">
                    <aside class="col-12 col-lg-3 col-xxl-2 left-menu" aria-label="Admin navigation">
                        <nav class="menu-panel">
                            <h1 class="menu-title">Browse Lists</h1>
                            <ul class="menu-links list-unstyled mb-0">
                                <li><a href="order"><i class="bi bi-receipt"></i> Order List</a></li>
                                <li><a href="reservation"><i class="bi bi-calendar-check"></i> Reservation List</a></li>
                                <li><a href="table"><i class="bi bi-grid-3x3"></i> Table List</a></li>
                                <li><a href="category"><i class="bi bi-tags"></i> Category List</a></li>
                                <li><a href="menuitem"><i class="bi bi-list-ul"></i> Menu Item List</a></li>
                                <li><a href="type"><i class="bi bi-diagram-2"></i> Type List</a></li>
                                <li><a href="ingredient"><i class="bi bi-basket"></i> Ingredient List</a></li>
                                <li><a href="recipe"><i class="bi bi-book"></i> Recipe List</a></li>
                                <li><a href="import"><i class="bi bi-download"></i> Import List</a></li>
                                <li><a href="supplier"><i class="bi bi-truck"></i> Supplier List</a></li>
                                <li><a href="account"><i class="bi bi-person-badge"></i> Account List</a></li>
                                <li><a href="role"><i class="bi bi-shield-lock"></i> Role List</a></li>
                                <li><a href="voucher"><i class="bi bi-ticket-perforated"></i> Voucher List</a></li>
                            </ul>
                        </nav>
                    </aside>

                    <section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
                        <div class="content-card shadow-sm">
                            <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
                                <div>
                                    <h1 class="section-title mb-1">Type List</h1>
                                </div>
                                <div class="actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                                    <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                                        <div class="search-box input-group">
                                            <span class="input-group-text"><i class="bi bi-search"></i></span>
                                            <input type="search" class="form-control" placeholder="Search by customer or ID">
                                        </div>
                                        <a class="btn btn-primary add-btn" href="<c:url value="type">
                                               <c:param name="view" value="add"/>
                                           </c:url>"><i class="bi bi-plus-circle"></i> Add</a>

                                    </div>
                                </div>
                            </div>

                            <div class="table-responsive px-4 pb-2">
                                <table class="table align-middle admin-table">
                                    <thead>
                                        <tr>
                                            <th scope="col">No.</th>
                                            <th scope="col">Name</th>
                                            <th scope="col">Description</th>
                                            <th scope="col" class="text-end">Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${typesList == null || empty typesList}">
                                                <tr>
                                                    <td colspan="9" style="color:red;">No data to display</td>
                                                </tr>   
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="type" items="${typesList}" varStatus="loop">
                                                    <tr>
                                                        <td><c:out value="${loop.index + 1}"/></td>
                                                        <td><c:out value="${type.typeName}"/></td>
                                                        <td><c:out value="${type.description}"/></td>

                                                        <td class="text-end">
                                                            <div class="action-button-group d-flex justify-content-end gap-2">
                                                                <c:url var="edit" value="type">
                                                                    <c:param name="view" value="edit"/>
                                                                    <c:param name="id" value="${type.typeId}"/>
                                                                </c:url>
                                                                <a class="btn btn-outline-secondary btn-icon btn-edit"
                                                                   title="Edit"
                                                                   aria-label="Edit"
                                                                   href="${edit}">
                                                                    <i class="bi bi-pencil"></i>
                                                                </a>

                                                                <button type="button" class="btn btn-outline-secondary btn-icon btn-delete" title="Delete" aria-label="Delete" onclick="showDeletePopup('${type.typeId}')">
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
                    </section>
                </div>
            </div>
        </main>

        <div class="modal" id="deletePopup" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Confirm Deletion</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body text-danger">
                        <h6 id="idForDeletePopup">Are you sure you want to delete this type?</h6>
                        <small class="text-muted">This action cannot be undone.</small>
                    </div>
                    <form method="post" action="<c:url value="type"/>">
                        <input type="hidden" id="hiddenInputIdDelete" name="id" value="">
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" name="action" value="delete" class="btn btn-danger">Delete</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Vendor JS Files -->
        <script src="assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
        <script src="assets/vendor/aos/aos.js"></script>

        <!-- Main JS File -->
        <script src="assets/js/main.js"></script>
    </body>

    <c:if  test="${not empty param.status}">
        <div class="modal" id="exampleModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Action ${param.status == "success"?"Successful":"Fail"}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <c:choose>
                            <c:when test="${param.status eq 'success'}">
                                    <p style="color: green">The Type <c:out value="${param.lastAction}"/> successfully.</p>
                            </c:when>
                            <c:otherwise>
                                <p style="color: red">Failed to <c:out value="${param.lastAction}"/> the type. Please check the information.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <!--<button type="button" class="btn btn-primary">Save changes</button>-->
                    </div>
                </div>
            </div>
        </div>


        <script>
                                                                    var myModal = new bootstrap.Modal(document.getElementById('exampleModal'));
                                                                    myModal.show();
        </script>
    </c:if>

    <script>
        function showDeletePopup(id) {
            document.getElementById("hiddenInputIdDelete").value = id;
            document.getElementById("idForDeletePopup").textContent = "Are you sure you want to delete the type with id = " + id + "?";
            var myModal = new bootstrap.Modal(document.getElementById('deletePopup'));
            myModal.show();
        }
    </script>

</html>
