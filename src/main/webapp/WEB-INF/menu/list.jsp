<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">

   <head>
  <meta charset="utf-8">
  <meta content="width=device-width, initial-scale=1.0" name="viewport">
  <title>Our Menu - Yummy Restaurant</title>
  <meta name="description" content="Browse our delicious menu with a variety of starters, breakfast, lunch, and dinner options">
  <meta name="keywords" content="restaurant menu, food, dining, breakfast, lunch, dinner">

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
  <link href="assets/vendor/glightbox/css/glightbox.min.css" rel="stylesheet">
  <link href="assets/vendor/swiper/swiper-bundle.min.css" rel="stylesheet">

  <!-- Main CSS File -->
  <link href="assets/css/main.css" rel="stylesheet">
</head>

<body class="menu-page">

 <%@include file="/WEB-INF/include/customerHeader.jsp" %>

  <main class="main">

    <div class="page-title" data-aos="fade">
      <div class="container">
        <h1>Our Menu</h1>
      </div>
    </div>

    <section id="menu" class="menu section">
      <div class="container">

        <ul class="nav nav-tabs d-flex justify-content-center" data-aos="fade-up" data-aos-delay="100">
          <c:forEach var="categoryName" items="${categoryNames}" varStatus="status">
            <c:set var="tabId" value="menu-${fn:replace(fn:toLowerCase(categoryName), ' ', '')}" />
            <li class="nav-item">
              <a class="nav-link ${status.first ? 'active show' : ''}" data-bs-toggle="tab" data-bs-target="#${tabId}">
                <h4>${categoryName}</h4>
              </a>
            </li>
          </c:forEach>
        </ul>


        <div class="tab-content" data-aos="fade-up" data-aos-delay="200">
          <c:forEach var="categoryName" items="${categoryNames}" varStatus="status">
            <c:set var="tabId" value="menu-${fn:replace(fn:toLowerCase(categoryName), ' ', '')}" />
            <c:set var="listName" value="${fn:replace(fn:toLowerCase(categoryName), ' ', '')}List" />
            <c:set var="menuList" value="${requestScope[listName]}" />

            <div class="tab-pane fade ${status.first ? 'active show' : ''}" id="${tabId}">
              <div class="tab-header text-center">
                <p>Menu</p>
                <h3>${categoryName}</h3>
              </div>

              <div class="row gy-5">
                <c:if test="${empty menuList}">
                  <p class="text-center text-muted">No items available.</p>
                </c:if>

                <c:forEach var="item" items="${menuList}">
                  <div class="col-lg-4 menu-item">
                    <a href="${pageContext.request.contextPath}/${item.imageUrl}" class="glightbox">
                     <img 
            src="${pageContext.request.contextPath}/${item.imageUrl}" 
            class="menu-img img-fluid" 
            alt="${item.itemName}"
            onerror="this.onerror=null; 
                var fallbackPath = '${pageContext.request.contextPath}/assets/img/menu/NIA.png';
                this.src = fallbackPath;
                // Update the parent <a> tag's href
                this.closest('a').href = fallbackPath;"
        />
                    </a>
                    <h4>${item.itemName}</h4>
                    <p class="ingredients">${item.description}</p>
                    <p class="price">${item.price} VND</p>
                  </div>
                </c:forEach>
              </div>
            </div>
          </c:forEach>
        </div>
      </div>
    </section>
  </main>
   <%@include file="/WEB-INF/include/customerFooter.jsp" %>
