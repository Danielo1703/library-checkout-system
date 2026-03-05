package assignment2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.TreeMap;

public class OrderProcessor {
    private static class CustomerSummary {
        int totalItems = 0;
        double grossTotal = 0.0;
    }

    public void process(String inputFile) {
        Map<String, CustomerSummary> summaryByCustomer = new TreeMap<>();

        int totalOrdersProcessed = 0;
        int totalItemsPurchased = 0;
        double totalRevenueBeforeDiscount = 0.0;

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int lineNo = 0;

            while ((line = br.readLine()) != null) {
                lineNo++;

                if (lineNo == 1 && line.startsWith("OrderID|")) {
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length != 6) {
                    logError(lineNo, "Malformed record (expected 6 fields)");
                    continue;
                }

                String orderId = parts[0].trim();
                String customerName = parts[1].trim();
                String productName = parts[2].trim();
                String quantityStr = parts[3].trim();
                String unitPriceStr = parts[4].trim();
                String orderDateStr = parts[5].trim();

                if (orderId.isEmpty() || customerName.isEmpty() || productName.isEmpty()) {
                    logError(lineNo, "Missing required text field(s)");
                    continue;
                }

                int quantity;
                double unitPrice;

                try {
                    quantity = Integer.parseInt(quantityStr);
                    unitPrice = Double.parseDouble(unitPriceStr);
                } catch (NumberFormatException e) {
                    logError(lineNo, "Invalid numeric value(s)");
                    continue;
                }

                if (quantity <= 0) {
                    logError(lineNo, "Quantity must be > 0");
                    continue;
                }

                if (unitPrice < 0) {
                    logError(lineNo, "Unit price cannot be negative");
                    continue;
                }

                try {
                    LocalDate.parse(orderDateStr);
                } catch (DateTimeParseException e) {
                    logError(lineNo, "Invalid date format (use yyyy-MM-dd)");
                    continue;
                }

                double lineTotal = quantity * unitPrice;

                CustomerSummary cs = summaryByCustomer.computeIfAbsent(customerName, k -> new CustomerSummary());
                cs.totalItems += quantity;
                cs.grossTotal += lineTotal;

                totalOrdersProcessed++;
                totalItemsPurchased += quantity;
                totalRevenueBeforeDiscount += lineTotal;
            }
        } catch (IOException e) {
            System.out.println("Failed to read file: " + e.getMessage());
            return;
        }

        if (totalOrdersProcessed == 0) {
            System.out.println("No valid orders processed.");
            return;
        }

        printReport(summaryByCustomer, totalOrdersProcessed, totalItemsPurchased, totalRevenueBeforeDiscount);
    }

    private void printReport(Map<String, CustomerSummary> summaryByCustomer,
                             int totalOrdersProcessed,
                             int totalItemsPurchased,
                             double totalRevenueBeforeDiscount) {
        System.out.println("=== Invoice Summary Report ===");
        System.out.printf("%-15s %-12s %-12s %-12s %-12s%n",
                "Customer", "Items", "GrossTotal", "Discount", "NetTotal");

        double grandGross = 0.0;
        double grandDiscount = 0.0;
        double grandNet = 0.0;

        for (Map.Entry<String, CustomerSummary> entry : summaryByCustomer.entrySet()) {
            String customer = entry.getKey();
            CustomerSummary cs = entry.getValue();

            double discount = calculateDiscount(cs.grossTotal);
            double net = cs.grossTotal - discount;

            grandGross += cs.grossTotal;
            grandDiscount += discount;
            grandNet += net;

            System.out.printf("%-15s %-12d $%-11.2f $%-11.2f $%-11.2f%n",
                    customer, cs.totalItems, cs.grossTotal, discount, net);
        }

        System.out.println();
        System.out.printf("Total Orders Processed: %d%n", totalOrdersProcessed);
        System.out.printf("Total Items Purchased: %d%n", totalItemsPurchased);
        System.out.printf("Total Revenue Before Discount: $%.2f%n", totalRevenueBeforeDiscount);
        System.out.printf("Grand Gross Total: $%.2f%n", grandGross);
        System.out.printf("Grand Discount Amount: $%.2f%n", grandDiscount);
        System.out.printf("Grand Net Total: $%.2f%n", grandNet);
    }

    private double calculateDiscount(double gross) {
        return gross > 500.0 ? gross * 0.10 : 0.0;
    }

    private void logError(int lineNo, String message) {
        System.out.printf("Line %d error: %s%n", lineNo, message);
    }
}
