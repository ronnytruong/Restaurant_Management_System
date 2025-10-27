<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Restaurant Management System</title>
        
        <!-- Favicons -->
        <link href="<%=request.getContextPath()%>/assets/img/favicon.png" rel="icon">
        <link href="<%=request.getContextPath()%>/assets/img/apple-touch-icon.png" rel="apple-touch-icon">

        <!-- Fonts -->
        <link href="https://fonts.googleapis.com" rel="preconnect">
        <link href="https://fonts.gstatic.com" rel="preconnect" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&family=Inter:wght@100;200;300;400;500;600;700;800;900&family=Amatic+SC:wght@400;700&display=swap" rel="stylesheet">

        <!-- Vendor CSS Files -->
        <link href="<%=request.getContextPath()%>/assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/assets/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/assets/vendor/aos/aos.css" rel="stylesheet">

        <!-- Main Site CSS -->
        <link href="<%=request.getContextPath()%>/assets/css/main.css" rel="stylesheet">

        <!-- Admin Listing CSS -->
        <link href="<%=request.getContextPath()%>/assets/css/dashboard.css" rel="stylesheet">
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
                                <li><a href="table"><i class="bi bi-grid-3x3"></i> Table List</a></li>
                                <li><a href="#"><i class="bi bi-tags"></i> Category List</a></li>
                                <li><a href="#"><i class="bi bi-list-ul"></i> Menu Item List</a></li>
                                <li><a href="#"><i class="bi bi-diagram-2"></i> Type List</a></li>
                                <li><a href="#"><i class="bi bi-basket"></i> Ingredient List</a></li>
                                <li><a href="recipe"><i class="bi bi-book"></i> Recipe List</a></li>
                                <li><a href="#"><i class="bi bi-download"></i> Import List</a></li>
                                <li><a href="supplier"><i class="bi bi-truck"></i> Supplier List</a></li>
                                <li><a href="#"><i class="bi bi-person-badge"></i> Account List</a></li>
                                <li><a href="role"><i class="bi bi-shield-lock"></i> Role List</a></li>
                                <li><a href="voucher"><i class="bi bi-ticket-perforated"></i> Voucher List</a></li>
                            </ul>
                        </nav>
                    </aside>
