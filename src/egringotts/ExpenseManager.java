/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package egringotts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.ApplicationFrame;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author USER
 */
public class ExpenseManager {
    
    private List<Transaction> transactions;

    public ExpenseManager() {
        transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public Map<String, Double> calculateCategoryPercentages() {
        Map<String, Double> categoryTotals = new HashMap<>();
        double total = transactions.stream().mapToDouble(Transaction::getAmount).sum();

        for (Transaction transaction : transactions) {
            categoryTotals.merge(transaction.getType(), (double) transaction.getAmount(), Double::sum);
        }

        Map<String, Double> categoryPercentages = new HashMap<>();
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            categoryPercentages.put(entry.getKey(), (entry.getValue() / total) * 100);
        }

        return categoryPercentages;
    }

    public List<Transaction> filterTransactions(String category, LocalDate startDate, LocalDate endDate, String paymentMethod) {
    return transactions.stream().filter(t -> 
        (category == null || t.getType().equals(category)) &&
        (startDate == null || !t.getDate().isBefore(startDate)) &&
        (endDate == null || !t.getDate().isAfter(endDate)) &&
        (paymentMethod == null || t.getDescription().equals(paymentMethod))).collect(Collectors.toList());
    }


    public void displayPieChart() {
    Map<String, Double> categoryPercentages = calculateCategoryPercentages();
    
    DefaultPieDataset dataset = new DefaultPieDataset();
    for (Map.Entry<String, Double> entry : categoryPercentages.entrySet()) {
    dataset.setValue(entry.getKey(), entry.getValue());
    }
    
    JFreeChart pieChart = ChartFactory.createPieChart("Expenditure by Category", dataset, true, true, false);
    PiePlot plot = (PiePlot) pieChart.getPlot();
    plot.setCircular(true);
    
    ApplicationFrame chartFrame = new ApplicationFrame("Expenditure Chart");
    ChartPanel chartPanel = new ChartPanel(pieChart);
    chartPanel.setPreferredSize(new java.awt.Dimension(560, 370));
    chartFrame.setContentPane(chartPanel);
    chartFrame.pack();
    chartFrame.setVisible(true);
    }
    
    public static void main(String[] args) throws Exception {
        ExpenseManager manager = new ExpenseManager();

        // Assuming the transactions are fetched from the database and added to the manager
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        manager.addTransaction(new Transaction("658210164008", "540857571006", 50, "Food", "bjkgjyfj"));
        manager.addTransaction(new Transaction("124512331312", "receipent2", 100, "Entertainment", "vbjgjhf"));
        manager.addTransaction(new Transaction("658210164008", "receipent3", 200, "Grocery", "uyduyfk"));
        manager.addTransaction(new Transaction("734223131232", "540857571006", 150, "Food", "vjhdthu"));

        //manager.displayPieChart();

        Map<String, Double> percentages = manager.calculateCategoryPercentages();
        for (Map.Entry<String, Double> entry : percentages.entrySet()) {
            System.out.printf("%s: %.2f%%\n", entry.getKey(), entry.getValue());
        }

        List<Transaction> filteredTransactions = manager.filterTransactions("Food", LocalDate.parse("2024-05-01", formatter), LocalDate.parse("2024-05-05", formatter), "Credit Card");
        for (Transaction t : filteredTransactions) {
            System.out.printf("Filtered - Category: %s, Amount: %d, Date: %s, Payment Method: %s\n", t.getType(), t.getAmount(), t.getDate(), t.getDescription());
        }
    }
    
}
