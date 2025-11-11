<%-- 
    Document   : booktable
    Created on : Oct 29, 2025, 3:47:47 PM
    Author     : TruongBinhTrong
--%>

<%@page import="model.Table"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <title>Book a Table - Yummy Restaurant</title>
        <meta name="description" content="Reserve your seat and explore available tables at Yummy Restaurant.">
        <meta name="keywords" content="restaurant reservation, book table, dining">

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

    <body class="reservation-page">

        <header id="header" class="header d-flex align-items-center sticky-top">
            <div class="container position-relative d-flex align-items-center justify-content-between">

                <a href="index.html" class="logo d-flex align-items-center me-auto me-xl-0">
                    <!-- Uncomment the line below if you also wish to use an image logo -->
                    <!-- <img src="assets/img/logo.png" alt=""> -->
                    <h1 class="sitename">Yummy</h1>
                    <span>.</span>
                </a>

                <nav id="navmenu" class="navmenu">
                    <ul>
                        <li><a href="index.html#hero">Home</a></li>
                        <li><a href="menu.html">Menu</a></li>
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

                <a class="btn-getstarted active" href="book-table.html">Book a Table</a>

            </div>
        </header>

        <main class="main">

            <div class="page-title" data-aos="fade">
                <div class="container">
                    <h1>Choose Your Table</h1>
                </div>
            </div>

            <section id="tables" class="tables section">

                <div class="container section-title" data-aos="fade-up">
                    <p><span>Select the perfect</span> <span class="description-title">Spot for Your Meal</span></p>
                </div>

                <div class="container" data-aos="fade-up" data-aos-delay="100">

                    <div class="row align-items-center justify-content-between g-4 table-legend">
                        <div class="col-md-4">
                            <div class="legend-item d-flex align-items-center">
                                <span class="legend-swatch available"></span>
                                <span>Available Table</span>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="legend-item d-flex align-items-center">
                                <span class="legend-swatch reserved"></span>
                                <span>Reserved</span>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="legend-item d-flex align-items-center">
                                <span class="legend-swatch occupied"></span>
                                <span>Occupied</span>
                            </div>
                        </div>
                    </div>

                    <div class="row g-4 mt-1" data-aos="fade-up" data-aos-delay="150">

                        <%
                            List<Table> tables = (List<Table>) request.getAttribute("tableList");
                            if (tables != null) {
                                for (Table t : tables) {
                        %>

                        <div class="col-6 col-md-4 col-lg-3">
                            <%
                                String status = t.getStatus().toLowerCase();
                                boolean clickable = status.equals("available");
                            %>
                            <div class="table-card <%= status%> h-100 text-center p-4"
                                 style="<%= clickable ? "cursor:pointer;" : "cursor:not-allowed; opacity:0.6;"%>"
                                 <%= clickable
                                         ? "onclick=\"window.location.href='booktable?view=add&tableId=" + t.getId() + "'\""
                                         : ""%>>

                                <h4>Table <%= t.getNumber()%></h4>
                                <p class="capacity"><%= t.getCapacity()%> Guests</p>
                                <span class="status"><%= t.getStatus()%></span>
                            </div>
                        </div>


                        <%
                            }
                        } else {
                        %>
                        <p>No tables available.</p>
                        <% }%>

                        <!-- Booking Modal (outside loop, only one instance) -->
                        <div class="modal fade" id="bookingModal" tabindex="-1" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <form action="<c:url value='/booktable'/>" method="post">
                                        <div class="modal-header">
                                            <h5 class="modal-title">Book Table</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                        </div>

                                        <div class="modal-body">
                                            <input type="hidden" name="tableId" id="tableId">

                                            <label class="form-label">Party Size</label>
                                            <input type="number" class="form-control" name="partySize" id="modalPartySize" min="1" required>
                                            <small class="text-muted">Number of guests</small>

                                            <label class="form-label">Date</label>
                                            <input type="date" class="form-control" name="reservationDate" id="modalReservationDate" required>

                                            <label class="form-label">Time</label>
                                            <input type="time" class="form-control" name="reservationTime" id="modalReservationTime" required>
                                        </div>

                                        <div class="modal-footer">
                                            <button type="submit" class="btn btn-danger">Confirm</button>
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>

            </section>



        </main>

        <footer id="footer" class="footer dark-background">

            <div class="container">
                <div class="row gy-3">
                    <div class="col-lg-3 col-md-6 d-flex">
                        <i class="bi bi-geo-alt icon"></i>
                        <div class="address">
                            <h4>Address</h4>
                            <p>A108 Adam Street</p>
                            <p>New York, NY 535022</p>
                            <p></p>
                        </div>

                    </div>

                    <div class="col-lg-3 col-md-6 d-flex">
                        <i class="bi bi-telephone icon"></i>
                        <div>
                            <h4>Contact</h4>
                            <p>
                                <strong>Phone:</strong> <span>+1 5589 55488 55</span><br>
                                <strong>Email:</strong> <span>info@example.com</span><br>
                            </p>
                        </div>
                    </div>

                    <div class="col-lg-3 col-md-6 d-flex">
                        <i class="bi bi-clock icon"></i>
                        <div>
                            <h4>Opening Hours</h4>
                            <p>
                                <strong>Mon-Sat:</strong> <span>11AM - 23PM</span><br>
                                <strong>Sunday</strong>: <span>Closed</span>
                            </p>
                        </div>
                    </div>

                    <div class="col-lg-3 col-md-6">
                        <h4>Follow Us</h4>
                        <div class="social-links d-flex">
                            <a href="#" class="twitter"><i class="bi bi-twitter-x"></i></a>
                            <a href="#" class="facebook"><i class="bi bi-facebook"></i></a>
                            <a href="#" class="instagram"><i class="bi bi-instagram"></i></a>
                            <a href="#" class="linkedin"><i class="bi bi-linkedin"></i></a>
                        </div>
                    </div>

                </div>
            </div>

            <div class="container copyright text-center mt-4">
                <p>© <span>Copyright</span> <strong class="px-1 sitename">Yummy</strong> <span>All Rights Reserved</span></p>
                <div class="credits">
                    <!-- All the links in the footer should remain intact. -->
                    <!-- You can delete the links only if you've purchased the pro version. -->
                    <!-- Licensing information: https://bootstrapmade.com/license/ -->
                    <!-- Purchase the pro version with working PHP/AJAX contact form: [buy-url] -->
                    Designed by <a href="https://bootstrapmade.com/">BootstrapMade</a> Distributed by <a href="https://themewagon.com">ThemeWagon</a>
                </div>
            </div>

        </footer>

        <!-- Scroll Top -->
        <a href="#" id="scroll-top" class="scroll-top d-flex align-items-center justify-content-center"><i class="bi bi-arrow-up-short"></i></a>

        <!-- Preloader -->
        <div id="preloader"></div>

        <!-- Vendor JS Files -->
        <script src="assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
        <script src="assets/vendor/php-email-form/validate.js"></script>
        <script src="assets/vendor/aos/aos.js"></script>
        <script src="assets/vendor/glightbox/js/glightbox.min.js"></script>
        <script src="assets/vendor/purecounter/purecounter_vanilla.js"></script>
        <script src="assets/vendor/swiper/swiper-bundle.min.js"></script>

        <!-- Main JS File -->
        <script src="assets/js/main.js"></script>

        <script>
            function openBookingModal(div) {
                const id = div.dataset.tableId;
                const number = div.dataset.tableNumber;
                const capacity = div.dataset.tableCapacity || '';
                document.getElementById('tableId').value = id;
                document.querySelector('#bookingModal .modal-title').innerText = "Book Table " + number;

                // Set minimum date to today
                const today = new Date().toISOString().split('T')[0];
                const dateInput = document.getElementById('modalReservationDate');
                if (dateInput) {
                    dateInput.setAttribute('min', today);
                    dateInput.value = '';
                }

                // Clear other fields
                document.getElementById('modalPartySize').value = '';
                document.getElementById('modalReservationTime').value = '';
            }
            // Set minimum date on page load
            document.addEventListener('DOMContentLoaded', function () {
                const today = new Date().toISOString().split('T')[0];
                const dateInput = document.getElementById('modalReservationDate');
                if (dateInput) {
                    dateInput.setAttribute('min', today);
                }
            });
        </script>

    </body>

</html>


