package dao;

import db.DBContext;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.DailyUsageSummary;
import model.IngredientUsageReport;
import model.UsageDayItem;

/**
 * Provides data access helpers for daily kitchen usage reports.
 */
public class InventoryReportDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(InventoryReportDAO.class.getName());

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

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(reportDate));
            try (ResultSet rs = ps.executeQuery()) {
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
                + "WHERE CAST(o.order_date AS DATE) = ? "
                + "AND LOWER(o.status) = LOWER(N'Completed')";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(reportDate));
            try (ResultSet rs = ps.executeQuery()) {
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

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(reportDate));
            try (ResultSet rs = ps.executeQuery()) {
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

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(reportDate));
            try (ResultSet rs = ps.executeQuery()) {
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

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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

    public int countUsageDays(String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        keyword = keyword.trim();
        String keywordLower = keyword.toLowerCase();

        String sql = "SELECT COUNT(*) AS total\n"
                + "FROM (\n"
                + "    SELECT usage_date\n"
                + "    FROM ingredient_usage iu\n"
                + "    LEFT JOIN employee e ON iu.created_by = e.emp_id\n"
                + "    WHERE (CONVERT(VARCHAR(10), iu.usage_date, 120) LIKE ?\n"
                + "       OR LOWER(e.emp_name) LIKE ?)\n"
                + "    GROUP BY usage_date\n"
                + ") AS usage_days";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keywordLower + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unable to count usage days", ex);
        }

        return 0;
    }

    public List<UsageDayItem> getUsageHistory(int page, int maxElements, String keyword) {
        List<UsageDayItem> list = new ArrayList<>();

        if (page <= 0) {
            page = 1;
        }
        if (keyword == null) {
            keyword = "";
        }
        keyword = keyword.trim();
        String keywordLower = keyword.toLowerCase();

        String sql = "SELECT usage_date,\n"
                + "       COUNT(DISTINCT ingredient_id) AS ingredient_count,\n"
                + "       SUM(quantity_used) AS total_usage,\n"
                + "       MAX(e.emp_name) AS processed_by,\n"
                + "       COALESCE(CONVERT(VARCHAR(19), MIN(created_at), 120), '') AS processed_at\n"
                + "FROM ingredient_usage iu\n"
                + "LEFT JOIN employee e ON iu.created_by = e.emp_id\n"
                + "WHERE (CONVERT(VARCHAR(10), iu.usage_date, 120) LIKE ?\n"
                + "   OR LOWER(e.emp_name) LIKE ?)\n"
                + "GROUP BY usage_date\n"
                + "ORDER BY usage_date DESC\n"
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keywordLower + "%");
            ps.setInt(3, (page - 1) * maxElements);
            ps.setInt(4, maxElements);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate usageDate = rs.getDate("usage_date").toLocalDate();
                    int ingredientCount = rs.getInt("ingredient_count");
                    double totalUsage = rs.getDouble("total_usage");
                    String processedBy = rs.getString("processed_by");
                    String processedAt = rs.getString("processed_at");

                    list.add(new UsageDayItem(usageDate, ingredientCount, totalUsage, processedBy, processedAt));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unable to load usage history", ex);
        }

        return list;
    }

    public double getAvailableStock(int ingredientId) {
        String sql = "WITH imported AS (\n"
                + "    SELECT SUM(CASE WHEN imp.status IS NULL OR LOWER(imp.status) IN (LOWER(N'Completed'), LOWER(N'Active')) THEN CAST(id.quantity AS DECIMAL(18,2)) ELSE 0 END) AS total_imported\n"
                + "    FROM import_detail id\n"
                + "    JOIN import imp ON id.import_id = imp.import_id\n"
                + "    WHERE id.ingredient_id = ?\n"
                + "), usage_cte AS (\n"
                + "    SELECT SUM(quantity_used) AS total_used\n"
                + "    FROM ingredient_usage\n"
                + "    WHERE ingredient_id = ?\n"
                + ")\n"
                + "SELECT COALESCE(imported.total_imported, 0) - COALESCE(usage_cte.total_used, 0) AS available\n"
                + "FROM imported CROSS JOIN usage_cte";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, ingredientId);
            ps.setInt(2, ingredientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("available");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unable to calculate available stock", ex);
        }

        return 0;
    }
}
