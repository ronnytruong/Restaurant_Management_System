package model;

import java.time.LocalDate;

/**
 * Represents a summarized usage report for a specific day.
 */
public class UsageDayItem {
    private LocalDate usageDate;
    private int ingredientCount;
    private double totalUsage;
    private String processedBy;
    private String processedAt;

    public UsageDayItem() {
    }

    public UsageDayItem(LocalDate usageDate, int ingredientCount, double totalUsage,
            String processedBy, String processedAt) {
        this.usageDate = usageDate;
        this.ingredientCount = ingredientCount;
        this.totalUsage = totalUsage;
        this.processedBy = processedBy;
        this.processedAt = processedAt;
    }

    public LocalDate getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(LocalDate usageDate) {
        this.usageDate = usageDate;
    }

    public int getIngredientCount() {
        return ingredientCount;
    }

    public void setIngredientCount(int ingredientCount) {
        this.ingredientCount = ingredientCount;
    }

    public double getTotalUsage() {
        return totalUsage;
    }

    public void setTotalUsage(double totalUsage) {
        this.totalUsage = totalUsage;
    }

    public String getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy;
    }

    public String getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(String processedAt) {
        this.processedAt = processedAt;
    }
}
