package dao;

import db.DBContext;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.DailyUsageSummary;
import model.IngredientUsageReport;

/**
 * Provides data access helpers for daily kitchen usage reports.
 */
public class InventoryReportDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(InventoryReportDAO.class.getName());
    private static final DateTimeFormatter FILE_TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public List<IngredientUsageReport> calculateDailyUsage(LocalDate reportDate) {
        List<IngredientUsageReport> results = new ArrayList<>();

        if (reportDate == null) {
            return results;
        }

        String sql = "SELECT ri.ingredient_id, ing.ingredient_name, ing.unit, "
                + "SUM(CAST(oi.quantity AS DECIMAL(18, 2)) * ri.quantity) AS total_usage "
                + "FROM [order] AS o "
                + "JOIN order_item AS oi ON o.order_id = oi.order_id "
                + "JOIN menu_item AS mi ON oi.menu_item_id = mi.menu_item_id "
                + "JOIN recipe AS r ON mi.recipe_id = r.recipe_id "
                + "JOIN recipe_item AS ri ON r.recipe_id = ri.recipe_id "
                + "JOIN ingredient AS ing ON ri.ingredient_id = ing.ingredient_id "
                + "WHERE CAST(o.order_date AS DATE) = ? "
                + "AND LOWER(o.status) = LOWER(N'Completed') "
                + "AND (oi.status IS NULL OR LOWER(oi.status) <> LOWER(N'Deleted')) "
                + "AND (r.status IS NULL OR LOWER(r.status) <> LOWER(N'Deleted')) "
                + "AND (ri.status IS NULL OR LOWER(ri.status) <> LOWER(N'Deleted')) "
                + "GROUP BY ri.ingredient_id, ing.ingredient_name, ing.unit "
                + "ORDER BY ing.ingredient_name";

        try ( PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(reportDate));
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int ingredientId = rs.getInt("ingredient_id");
                    String ingredientName = rs.getNString("ingredient_name");
                    String unit = rs.getString("unit");
                    double quantityUsed = rs.getDouble("total_usage");

                    IngredientUsageReport report = new IngredientUsageReport(ingredientId, ingredientName, unit, quantityUsed);
                    results.add(report);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unable to aggregate daily usage", ex);
        }

        return results;
    }

    public DailyUsageSummary getDailySummary(LocalDate reportDate) {
        DailyUsageSummary summary = new DailyUsageSummary();

        if (reportDate == null) {
            return summary;
        }

        String sql = "SELECT COUNT(DISTINCT o.order_id) AS order_count, "
                + "COALESCE(SUM(CASE WHEN oi.quantity IS NULL THEN 0 ELSE oi.quantity END), 0) AS item_count "
                + "FROM [order] AS o "
                + "LEFT JOIN order_item AS oi ON o.order_id = oi.order_id "
                + "AND (oi.status IS NULL OR LOWER(oi.status) <> LOWER(N'Deleted')) "
                + "WHERE CAST(o.order_date AS DATE) = ? "
                + "AND LOWER(o.status) = LOWER(N'Completed')";

        try ( PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(reportDate));
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    summary.setReportDate(reportDate);
                    summary.setCompletedOrders(rs.getInt("order_count"));
                    summary.setItemsPrepared(rs.getInt("item_count"));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unable to load daily summary", ex);
        }

        return summary;
    }

    public boolean hasUsageReportForDate(LocalDate reportDate) {
        if (reportDate == null) {
            return false;
        }

        String sql = "SELECT COUNT(usage_id) AS total FROM ingredient_usage WHERE usage_date = ?";

        try ( PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(reportDate));
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unable to check usage report existence", ex);
        }

        return false;
    }

    public List<IngredientUsageReport> getRecordedUsage(LocalDate reportDate) {
        List<IngredientUsageReport> results = new ArrayList<>();

        if (reportDate == null) {
            return results;
        }

        String sql = "SELECT iu.ingredient_id, ing.ingredient_name, ing.unit, iu.quantity_used, "
                + "iu.stock_before, iu.stock_after, iu.created_by, iu.created_at, e.emp_name "
                + "FROM ingredient_usage AS iu "
                + "JOIN ingredient AS ing ON iu.ingredient_id = ing.ingredient_id "
                + "LEFT JOIN employee AS e ON iu.created_by = e.emp_id "
                + "WHERE iu.usage_date = ? "
                + "ORDER BY ing.ingredient_name";

        try ( PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(reportDate));
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    IngredientUsageReport report = new IngredientUsageReport();
                    report.setIngredientId(rs.getInt("ingredient_id"));
                    report.setIngredientName(rs.getNString("ingredient_name"));
                    report.setUnit(rs.getString("unit"));
                    report.setQuantityUsed(rs.getDouble("quantity_used"));
                    report.setStockBefore(rs.getDouble("stock_before"));
                    report.setStockAfter(rs.getDouble("stock_after"));

                    int createdBy = rs.getInt("created_by");
                    if (!rs.wasNull()) {
                        report.setProcessedBy(createdBy);
                    }

                    String createdByName = rs.getNString("emp_name");
                    if (createdByName != null) {
                        report.setProcessedByName(createdByName);
                    }

                    Timestamp createdAt = rs.getTimestamp("created_at");
                    if (createdAt != null) {
                        report.setProcessedAt(createdAt.toLocalDateTime());
                    }

                    results.add(report);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unable to load recorded usage report", ex);
        }

        return results;
    }

    public int saveUsageReport(LocalDate reportDate, Integer createdBy, List<IngredientUsageReport> items) throws SQLException {
        if (reportDate == null || items == null || items.isEmpty()) {
            return 0;
        }

        Connection conn = getConnection();
        boolean initialAutoCommit = conn.getAutoCommit();
        String sql = "INSERT INTO ingredient_usage (usage_date, ingredient_id, quantity_used, stock_before, stock_after, created_by) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            int inserted = 0;
            for (IngredientUsageReport item : items) {
                ps.setDate(1, Date.valueOf(reportDate));
                ps.setInt(2, item.getIngredientId());
                ps.setBigDecimal(3, BigDecimal.valueOf(item.getQuantityUsed()));
                ps.setBigDecimal(4, BigDecimal.valueOf(item.getStockBefore()));
                ps.setBigDecimal(5, BigDecimal.valueOf(item.getStockAfter()));

                if (createdBy != null && createdBy > 0) {
                    ps.setInt(6, createdBy);
                } else {
                    ps.setNull(6, Types.INTEGER);
                }

                inserted += ps.executeUpdate();
            }

            conn.commit();
            return inserted;
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                LOGGER.log(Level.SEVERE, "Unable to rollback usage report save", rollbackEx);
            }
            throw ex;
        } finally {
            try {
                conn.setAutoCommit(initialAutoCommit);
            } catch (SQLException ex) {
                LOGGER.log(Level.WARNING, "Unable to restore auto-commit flag", ex);
            }
        }
    }

    public String exportUsageReport(LocalDate reportDate, List<IngredientUsageReport> items, String processedByName) {
        if (reportDate == null || items == null || items.isEmpty()) {
            return "";
        }

        String directoryPath = "../../../export/stockUsage";
        File directory = new File(directoryPath);
        if (!directory.exists() && !directory.mkdirs()) {
            LOGGER.warning("Unable to create export directory for stock usage report");
            return "";
        }

        String fileName = "usage_" + reportDate + "_" + LocalDateTime.now().format(FILE_TIMESTAMP_FORMAT) + ".txt";
        File targetFile = new File(directory, fileName);

        DailyUsageSummary summary = getDailySummary(reportDate);
        double totalUsage = items.stream().mapToDouble(IngredientUsageReport::getQuantityUsed).sum();

        StringBuilder content = new StringBuilder();
        content.append("Inventory Usage Report\n");
        content.append("Report date: ").append(reportDate).append('\n');
        if (processedByName != null && !processedByName.isBlank()) {
            content.append("Processed by: ").append(processedByName).append('\n');
        }
        content.append("Completed orders: ").append(summary.getCompletedOrders()).append('\n');
        content.append("Menu items prepared: ").append(summary.getItemsPrepared()).append('\n');
        content.append(String.format("Total ingredient usage: %.2f\n", totalUsage));
        content.append("----------------------------------------\n");
        content.append(String.format("%-30s %-10s %-15s %-15s %-15s\n", "Ingredient", "Unit", "Before", "Used", "After"));
        content.append("----------------------------------------\n");

        for (IngredientUsageReport item : items) {
            content.append(String.format("%-30s %-10s %-15.2f %-15.2f %-15.2f\n",
                    item.getIngredientName(),
                    item.getUnit(),
                    item.getStockBefore(),
                    item.getQuantityUsed(),
                    item.getStockAfter()));
        }

        try ( FileWriter writer = new FileWriter(targetFile)) {
            writer.write(content.toString());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to export stock usage report", ex);
            return "";
        }

        return targetFile.getAbsolutePath();
    }
}
