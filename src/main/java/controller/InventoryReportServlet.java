package controller;

import static constant.CommonFunction.removePopup;
import static constant.CommonFunction.setPopup;
import dao.IngredientDAO;
import dao.InventoryReportDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
        double totalUsage = usageList.stream().mapToDouble(IngredientUsageReport::getQuantityUsed).sum();

        LocalDateTime processedAt = usageList.stream()
                .map(IngredientUsageReport::getProcessedAt)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        String processedByName = usageList.stream()
                .map(IngredientUsageReport::getProcessedByName)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

    String processedAtDisplay = processedAt != null
        ? processedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        : null;

    request.setAttribute("reportDate", reportDate);
    request.setAttribute("usageList", usageList);
    request.setAttribute("alreadyProcessed", alreadyProcessed);
    request.setAttribute("summary", summary);
    request.setAttribute("totalUsage", totalUsage);
    request.setAttribute("processedAt", processedAtDisplay);
    request.setAttribute("processedByName", processedByName);
    request.setAttribute("hasData", !usageList.isEmpty());

        request.getRequestDispatcher("/WEB-INF/report/usage.jsp").forward(request, response);
        ensureSession(request);
        removePopup(request);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        LocalDate reportDate = parseDate(request.getParameter("date"));
        if (reportDate == null) {
            reportDate = LocalDate.now();
        }

        if ("finalize".equalsIgnoreCase(action)) {
            handleFinalizeReport(request, reportDate);
        }

        response.sendRedirect(request.getContextPath() + "/inventory-report?date=" + reportDate);
    }

    private void handleFinalizeReport(HttpServletRequest request, LocalDate reportDate) {
        ensureSession(request);
        HttpSession session = request.getSession(false);
        Employee employee = session != null ? (Employee) session.getAttribute("employeeSession") : null;
        Integer employeeId = employee != null ? employee.getEmpId() : null;
        String employeeName = employee != null ? employee.getEmpName() : null;

        if (reportDAO.hasUsageReportForDate(reportDate)) {
            setPopup(request, false, "Report for " + reportDate + " has already been finalized.");
            return;
        }

        List<IngredientUsageReport> usageList = reportDAO.calculateDailyUsage(reportDate);
        if (usageList.isEmpty()) {
            setPopup(request, false, "No completed orders found on " + reportDate + ". Nothing to deduct.");
            return;
        }

        applyCurrentStockSnapshot(usageList);

        try {
            reportDAO.saveUsageReport(reportDate, employeeId, usageList);
            String exportPath = reportDAO.exportUsageReport(reportDate, usageList, employeeName);

            StringBuilder message = new StringBuilder("Inventory usage for ")
                    .append(reportDate)
                    .append(" recorded successfully.");
            if (exportPath != null && !exportPath.isBlank()) {
                message.append(" Report saved at: ").append(exportPath);
            }
            setPopup(request, true, message.toString());
        } catch (SQLException ex) {
            setPopup(request, false, "Unable to finalize report: " + ex.getMessage());
        }
    }

    private void applyCurrentStockSnapshot(List<IngredientUsageReport> usageList) {
        if (usageList == null || usageList.isEmpty()) {
            return;
        }

        List<Ingredient> ingredients = ingredientDAO.getAll();
        Map<Integer, Double> stockByIngredient = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            stockByIngredient.put(ingredient.getIngredientId(), (double) ingredient.getTotalQuantity());
        }

        for (IngredientUsageReport report : usageList) {
            double before = Optional.ofNullable(stockByIngredient.get(report.getIngredientId())).orElse(0.0d);
            report.setStockBefore(before);
            report.setStockAfter(before - report.getQuantityUsed());
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

    private void ensureSession(HttpServletRequest request) {
        if (request.getSession(false) == null) {
            request.getSession(true);
        }
    }
}
