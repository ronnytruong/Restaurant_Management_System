package controller;

import static constant.CommonFunction.getTotalPages;
import static constant.CommonFunction.removePopup;
import static constant.CommonFunction.setPopup;
import static constant.CommonFunction.validateInteger;
import static constant.CommonFunction.validateString;
import constant.Constants;
import dao.IngredientDAO;
import dao.InventoryReportDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.DailyUsageSummary;
import model.Employee;
import model.Ingredient;
import model.IngredientUsageReport;
import model.UsageDayItem;

/**
 * Handles the inventory usage report flow, allowing end-of-day deductions based on completed orders.
 */
@WebServlet(name = "InventoryReportServlet", urlPatterns = {"/inventory-report"})
public class InventoryReportServlet extends HttpServlet {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final InventoryReportDAO reportDAO = new InventoryReportDAO();
    private final IngredientDAO ingredientDAO = new IngredientDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ensureSession(request);

        String view = request.getParameter("view");
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        keyword = keyword.trim();

        String namepage;
        if (!validateString(view, -1) || "detail".equalsIgnoreCase(view)) {
            namepage = "usage";
            prepareDetailPage(request);
        } else if ("list".equalsIgnoreCase(view)) {
            namepage = "list";
            prepareListPage(request, keyword);
        } else {
            namepage = "usage";
            prepareDetailPage(request);
        }

        request.getRequestDispatcher("/WEB-INF/report/" + namepage + ".jsp").forward(request, response);
        removePopup(request);
    }

    private void prepareListPage(HttpServletRequest request, String keyword) {
        int page = parsePage(request.getParameter("page"));

        int totalItems = reportDAO.countUsageDays(keyword);
        int totalPages = getTotalPages(totalItems);
        if (totalPages > 0 && page > totalPages) {
            page = totalPages;
        }

        List<UsageDayItem> usageDays = reportDAO.getUsageHistory(page, Constants.MAX_ELEMENTS_PER_PAGE, keyword);

        request.setAttribute("keyword", keyword);
        request.setAttribute("usageDays", usageDays);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
    }

    private void prepareDetailPage(HttpServletRequest request) {
        LocalDate reportDate = parseDate(request.getParameter("date"));
        if (reportDate == null) {
            reportDate = LocalDate.now();
        }

        boolean alreadyProcessed = reportDAO.hasUsageReportForDate(reportDate);

        List<IngredientUsageReport> usageList;
        if (alreadyProcessed) {
            usageList = reportDAO.getRecordedUsage(reportDate);
        } else {
            usageList = reportDAO.calculateDailyUsage(reportDate);
            applyCurrentStockSnapshot(usageList);
        }

        DailyUsageSummary summary = reportDAO.getDailySummary(reportDate);
        double totalUsage = 0;
        LocalDateTime processedAt = null;
        String processedByName = null;

        for (IngredientUsageReport usageReport : usageList) {
            totalUsage += usageReport.getQuantityUsed();

            if (processedAt == null && usageReport.getProcessedAt() != null) {
                processedAt = usageReport.getProcessedAt();
            }

            if (processedByName == null && usageReport.getProcessedByName() != null) {
                processedByName = usageReport.getProcessedByName();
            }
        }

        String processedAtDisplay = null;
        if (processedAt != null) {
            processedAtDisplay = processedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        request.setAttribute("reportDate", reportDate);
        request.setAttribute("usageList", usageList);
        request.setAttribute("alreadyProcessed", alreadyProcessed);
        request.setAttribute("summary", summary);
        request.setAttribute("totalUsage", totalUsage);
        request.setAttribute("processedAt", processedAtDisplay);
        request.setAttribute("processedByName", processedByName);
        request.setAttribute("hasData", !usageList.isEmpty());
        request.setAttribute("ingredientList", ingredientDAO.getAll());

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ensureSession(request);
        String action = request.getParameter("action");
        LocalDate reportDate = parseDate(request.getParameter("date"));
        if (reportDate == null) {
            reportDate = LocalDate.now();
        }

        if ("record".equalsIgnoreCase(action)) {
            handleRecordReport(request, reportDate);
        } else if ("manual".equalsIgnoreCase(action)) {
            handleManualAdjustment(request, reportDate);
        }

        response.sendRedirect(request.getContextPath() + "/inventory-report?view=detail&date=" + reportDate);
    }

    private void handleRecordReport(HttpServletRequest request, LocalDate reportDate) {
        HttpSession session = request.getSession(false);
        Employee employee = session != null ? (Employee) session.getAttribute("employeeSession") : null;
        Integer employeeId = employee != null ? employee.getEmpId() : null;

        if (reportDAO.hasUsageReportForDate(reportDate)) {
            setPopup(request, false, "Báo cáo cho ngày " + reportDate + " đã được ghi nhận trước đó.");
            return;
        }

        List<IngredientUsageReport> usageList = reportDAO.calculateDailyUsage(reportDate);
        if (usageList.isEmpty()) {
            setPopup(request, false, "Không tìm thấy đơn hoàn tất trong ngày " + reportDate + ".");
            return;
        }

        applyCurrentStockSnapshot(usageList);

        try {
            reportDAO.saveUsageReport(reportDate, employeeId, usageList);
            setPopup(request, true, "Đã ghi nhận " + usageList.size() + " dòng tiêu hao cho ngày " + reportDate + ".");
        } catch (SQLException ex) {
            setPopup(request, false, "Không thể ghi nhận báo cáo: " + ex.getMessage());
        }
    }

    private void handleManualAdjustment(HttpServletRequest request, LocalDate reportDate) {
        if (!reportDAO.hasUsageReportForDate(reportDate)) {
            setPopup(request, false, "Hãy ghi nhận báo cáo của ngày trước khi thêm điều chỉnh thủ công.");
            return;
        }

        int ingredientId;
        try {
            ingredientId = Integer.parseInt(request.getParameter("ingredientId"));
        } catch (NumberFormatException ex) {
            ingredientId = -1;
        }

        double quantity;
        String quantityRaw = request.getParameter("quantity");
        try {
            quantity = Double.parseDouble(quantityRaw);
        } catch (NumberFormatException | NullPointerException ex) {
            quantity = -1;
        }

        if (!validateInteger(ingredientId, false, false, true) || quantity <= 0) {
            setPopup(request, false, "Thông tin điều chỉnh chưa hợp lệ.");
            return;
        }

        Ingredient ingredient = ingredientDAO.getElementByID(ingredientId);
        if (ingredient == null) {
            setPopup(request, false, "Không tìm thấy nguyên liệu để điều chỉnh.");
            return;
        }

        HttpSession session = request.getSession(false);
        Employee employee = session != null ? (Employee) session.getAttribute("employeeSession") : null;
        Integer employeeId = employee != null ? employee.getEmpId() : null;

        double stockBefore = reportDAO.getAvailableStock(ingredientId);
        IngredientUsageReport manualEntry = new IngredientUsageReport(
                ingredientId,
                ingredient.getIngredientName(),
                ingredient.getUnit(),
                quantity
        );
        manualEntry.setStockBefore(stockBefore);
        manualEntry.setStockAfter(stockBefore - quantity);

        List<IngredientUsageReport> items = new ArrayList<>();
        items.add(manualEntry);

        try {
            reportDAO.saveUsageReport(reportDate, employeeId, items);
            setPopup(request, true, "Đã thêm dòng điều chỉnh cho " + ingredient.getIngredientName() + ".");
        } catch (SQLException ex) {
            setPopup(request, false, "Không thể thêm điều chỉnh: " + ex.getMessage());
        }
    }

    private void applyCurrentStockSnapshot(List<IngredientUsageReport> usageList) {
        if (usageList == null || usageList.isEmpty()) {
            return;
        }

        List<Ingredient> ingredients = ingredientDAO.getAll();

        for (IngredientUsageReport report : usageList) {
            double stockBefore = 0;
            for (Ingredient ingredient : ingredients) {
                if (ingredient.getIngredientId() == report.getIngredientId()) {
                    stockBefore = ingredient.getTotalQuantity();
                    break;
                }
            }

            report.setStockBefore(stockBefore);
            report.setStockAfter(stockBefore - report.getQuantityUsed());
        }
    }

    private LocalDate parseDate(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(raw.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private int parsePage(String rawPage) {
        int page = 1;
        if (rawPage != null) {
            try {
                page = Integer.parseInt(rawPage);
            } catch (NumberFormatException ex) {
                page = 1;
            }
        }
        if (page <= 0) {
            page = 1;
        }
        return page;
    }

    private void ensureSession(HttpServletRequest request) {
        if (request.getSession(false) == null) {
            request.getSession(true);
        }
    }
}
