<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<main class="main">
    
    <section id="menu" class="menu section">
        <div class="container">

            <ul class="nav nav-tabs d-flex justify-content-center" data-aos="fade-up" data-aos-delay="100">
                <c:forEach var="categoryName" items="${requestScope.categoryNames}" varStatus="loop">
                    <li class="nav-item">
                        <a class="nav-link <c:if test='${loop.first}'>active show</c:if>" 
                           data-bs-toggle="tab" 
                           data-bs-target="#menu-${categoryName.toLowerCase().replace(' ', '')}">
                            <h4>${categoryName}</h4>
                        </a>
                    </li></c:forEach>
            </ul>

            <div class="tab-content" data-aos="fade-up" data-aos-delay="200">
                
                <c:forEach var="categoryName" items="${requestScope.categoryNames}" varStatus="loop">
                    <div class="tab-pane fade <c:if test='${loop.first}'>active show</c:if>" 
                         id="menu-${categoryName.toLowerCase().replace(' ', '')}">

                        <div class="tab-header text-center">
                            <p>Menu</p>
                            <h3>${categoryName}</h3>
                        </div>

                        <div class="row gy-5">
                            
                            <c:set var="menuItems" value="${requestScope[categoryName.toLowerCase().replace(' ', '').concat('List')]}" />
                            
                            <c:choose>
                                <c:when test="${not empty menuItems}">
                                    <c:forEach var="item" items="${menuItems}">
                                        <div class="col-lg-4 menu-item">
                                            <a href="${item.imageUrl}" class="glightbox">
                                                <img src="${item.imageUrl}" class="menu-img img-fluid" alt="${item.itemName}">
                                            </a>
                                            <h4>${item.itemName}</h4>
                                            <p class="ingredients">
                                                ${item.ingredients != null ? item.ingredients : item.description} 
                                            </p>
                                            <p class="price">
                                                $${item.price}
                                            </p>
                                        </div></c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-12 text-center">
                                        <p>Không có món ?n nào trong danh m?c này.</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div></c:forEach>
                
            </div>
        </div>
    </section></main>
</html>