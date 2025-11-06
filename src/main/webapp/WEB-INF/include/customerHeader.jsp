 <header id="header" class="header d-flex align-items-center sticky-top">
    <div class="container position-relative d-flex align-items-center justify-content-between">

      <a href="homepage" class="logo d-flex align-items-center me-auto me-xl-0">
        <!-- Uncomment the line below if you also wish to use an image logo -->
        <!-- <img src="assets/img/logo.png" alt=""> -->
        <h1 class="sitename">Yummy</h1>
        <span>.</span>
      </a>

      <nav id="navmenu" class="navmenu">
        <ul>
          <li><a href="homepage">Home</a></li>
          <li><a href="menu">Menu</a></li>
          <li><a href="index.html#events">Events</a></li>
          <li><a href="index.html#chefs">Chefs</a></li>
          <li><a href="index.html#gallery">Gallery</a></li>
          <li class="dropdown"><a href="#"><span>Dropdown</span> <i class="bi bi-chevron-down toggle-dropdown"></i></a>
            <ul>
              <li><a href="#">Dropdown 1</a></li>
              <li class="dropdown"><a href="#"><span>Deep Dropdown</span> <i class="bi bi-chevron-down toggle-dropdown"></i></a>
                <ul>
                  <li><a href="#">Deep Dropdown 1</a></li>
                  <li><a href="#">Deep Dropdown 2</a></li>
                  <li><a href="#">Deep Dropdown 3</a></li>
                  <li><a href="#">Deep Dropdown 4</a></li>
                  <li><a href="#">Deep Dropdown 5</a></li>
                </ul>
              </li>
              <li><a href="#">Dropdown 2</a></li>
              <li><a href="#">Dropdown 3</a></li>
              <li><a href="#">Dropdown 4</a></li>
            </ul>
          </li>
          <li><a href="index.html#contact">Contact</a></li>
        </ul>
        <i class="mobile-nav-toggle d-xl-none bi bi-list"></i>
      </nav>

    <div class="d-flex align-items-center gap-2">
        
            
            <a class="btn-getstarted" href="booktable">Book a Table</a>
              
            <c:if test="${empty sessionScope.customerSession}">
                <a class="btn btn-outline-danger text-danger round" href="login">Login</a>
            </c:if>
            
            
            <c:if test="${not empty sessionScope.customerSession}">
                <div class="dropdown">
                    <a class="btn btn-link dropdown-toggle p-0" href="#" role="button" id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                        <img src="https://ui-avatars.com/api/?name=${sessionScope.customerSession.customerName}&background=f5f5f5&color=000000&size=40" 
                             alt="User Avatar" 
                             class="rounded-circle" 
                             style="width: 40px; height: 40px; object-fit: cover;">
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                        <li>
                            <h5 class="dropdown-header text-center text-dark">Hello, ${sessionScope.customerSession.customerName}!</h5>
                        </li>
                        <li><hr class="dropdown-divider"></li>
                        <li>
                            <a class="dropdown-item" href="customer-profile">
                                <i class="bi bi-person-circle me-2"></i>Manage My Profile
                            </a>
                        </li>
                        <li><hr class="dropdown-divider"></li>
                        <li>
                            <a class="dropdown-item text-danger" href="logout">
                                <i class="bi bi-box-arrow-right me-2"></i>Logout
                            </a>
                        </li>
                    </ul>
                </div>
            </c:if>
        </div>

    </div>
  </header>