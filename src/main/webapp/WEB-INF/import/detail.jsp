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
                                <li><a href="#"><i class="bi bi-receipt"></i> Order List</a></li>
                                <li><a href="#"><i class="bi bi-calendar-check"></i> Reservation List</a></li>
                                <li><a href="#"><i class="bi bi-grid-3x3"></i> Table List</a></li>
                                <li><a href="#"><i class="bi bi-tags"></i> Category List</a></li>
                                <li><a href="#"><i class="bi bi-list-ul"></i> Menu Item List</a></li>
                                <li><a href="#"><i class="bi bi-diagram-2"></i> Type List</a></li>
                                <li><a href="#"><i class="bi bi-basket"></i> Ingredient List</a></li>
                                <li><a href="#"><i class="bi bi-book"></i> Recipe List</a></li>
                                <li><a href="#"><i class="bi bi-download"></i> Import List</a></li>
                                <li><a href="supplier"><i class="bi bi-truck"></i> Supplier List</a></li>
                                <li><a href="#"><i class="bi bi-person-badge"></i> Account List</a></li>
                                <li><a href="#"><i class="bi bi-shield-lock"></i> Role List</a></li>
                                <li><a href="#"><i class="bi bi-ticket-perforated"></i> Voucher List</a></li>
                            </ul>
                        </nav>
                    </aside>

                    <section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
                        <div class="content-card shadow-sm">
                            <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
                                <div>
                                    <h1 class="section-title mb-1">Import Detail</h1>
                                </div>
                                <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                                    <form action="<c:url value='supplier'><c:param name='page' value='1'/></c:url>" method="get" class="search-box input-group">
                                            <div class="search-box input-group text-end">
                                                <span class="input-group-text"><i class="bi bi-search"></i></span>
                                                <input type="search" name="keyword" class="form-control"
                                                       placeholder="Search by name"
                                                       value="${param.keyword != null ? param.keyword : (requestScope.keyword != null ? requestScope.keyword : '')}">
                                        </div>
                                    </form>
                                    <a class="btn btn-primary add-btn" href="<c:url value="import">
                                           <c:param name="view" value="addDetail"/>
                                           <c:param name="id" value="${currentImport.importId}"/>
                                       </c:url>"><i class="bi bi-plus-circle"></i> Add Detail</a>

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
                                                                    <td>
                                                                        <c:out value="${loop.index + 1}"/>
                                                                    </td>
                                                                    <td>
                                                                        <c:out value='${detail.ingredientName}'/>
                                                                    </td>
                                                                    <td>
                                                                        <c:out value='${detail.quantity}'/>
                                                                    </td>
                                                                    <td>
                                                                        <c:out value='${detail.unit}'/>
                                                                    </td>
                                                                    <td>
                                                                        <c:out value='${detail.unitPrice}'/>
                                                                    </td>
                                                                    <td>
                                                                        <c:out value='${detail.totalPrice}'/>
                                                                </tr>
                                                            </c:forEach>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="text-end">
                                            <a class="btn btn-success add-btn" class="form-control" href="<c:url value="import">
                                                   <c:param name="view" value="addDetail"/>
                                                   <c:param name="id" value="${currentImport.importId}"/>
                                               </c:url>"><i class="bi bi-download"></i>Accept</a></div>

                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="card-body px-4 pb-4">
                                        <div class="alert alert-warning mb-3" role="alert">
                                            Unable to find the requested import. Please verify the URL or choose another entry from the list.
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </section>
                </div>
            </div>
        </main>

        <!-- Vendor JS Files -->
        <script src="assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
        <script src="assets/vendor/aos/aos.js"></script>

        <!-- Main JS File -->
        <script src="assets/js/main.js"></script>
    </body>
</html>
