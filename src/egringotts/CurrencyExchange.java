/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package egringotts;

/**
 * @note : please use this class for the exchange to make it more memory friendly
 * @author afiqz
 */
import java.util.*;

class CurrencyExchange {
    private Map<String, Map<String, Double>> graph = new HashMap<>();
    private Map<String, Double> processingFees = new HashMap<>();

    /**
     * Adds currency conversion information to the graph and stores processing fees.
     *
     * @param from          The source currency.
     * @param to            The target currency.
     * @param rate          The exchange rate from 'from' to 'to'.
     * @param processingFee The processing fee (in the source currency) for this conversion.
     */
    public void addCurrency(String from, String to, double rate, double processingFee) {
        // Add to the graph if the currency conversion is not already present
        graph.computeIfAbsent(from, k -> new HashMap<>()).put(to, rate);
        graph.computeIfAbsent(to, k -> new HashMap<>()).put(from, 1.0 / rate);

        // Add processing fee only if not already set
        String feeKey = from + "-" + to;
        if (!processingFees.containsKey(feeKey)) {
            processingFees.put(feeKey, processingFee);
            processingFees.put(to + "-" + from, processingFee); // Assuming symmetric fees
        }
    }

    /**
     * Calculates the exchanged amount from 'from' to 'to'.
     *
     * @param from   The source currency.
     * @param to     The target currency.
     * @param amount The initial amount to exchange.
     * @return The converted amount or -1.0 if no valid path exists.
     */
    public double exchange(String from, String to, double amount) {
        if (!graph.containsKey(from) || !graph.containsKey(to)) {
            return -1.0; // Invalid currencies
        }

        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingDouble(p -> p.amount));
        pq.offer(new Pair(from, amount));

        Map<String, Double> visited = new HashMap<>();
        visited.put(from, amount);

        while (!pq.isEmpty()) {
            Pair curr = pq.poll();
            if (curr.currency.equals(to)) {
                return curr.amount;
            }
            for (Map.Entry<String, Double> neighbor : graph.get(curr.currency).entrySet()) {
                String nextCurrency = neighbor.getKey();
                double nextAmount = curr.amount * neighbor.getValue();
                if (!visited.containsKey(nextCurrency) || nextAmount > visited.get(nextCurrency)) {
                    visited.put(nextCurrency, nextAmount);
                    pq.offer(new Pair(nextCurrency, nextAmount));
                }
            }
        }
        return -1.0; // No valid path
    }

    /**
     * Retrieves the processing fee for a given currency pair.
     *
     * @param from The source currency.
     * @param to   The target currency.
     * @return The processing fee or 0.0 if not found.
     */
    public double getProcessingFee(String from, String to) {
        return processingFees.getOrDefault(from + "-" + to, 0.0);
    }

    private static class Pair {
        String currency;
        double amount;

        Pair(String currency, double amount) {
            this.currency = currency;
            this.amount = amount;
        }
    }

    public static void main(String[] args) {
        // Example usage:
        CurrencyExchange exchange = new CurrencyExchange();
        // Add currencies, rates, and processing fees (e.g., exchange.addCurrency("Knut", "Sickle", 29, 0.01);)

        // Calculate converted amount and processing fee
        double valueExchange = 100.0;
        double convertedAmount = exchange.exchange("Knut", "Galleon", valueExchange);
        double processingFee = exchange.getProcessingFee("Knut", "Galleon");
        double updatedBalance = 1000.0 - processingFee;

        System.out.println("Converted amount: " + convertedAmount + " Galleons");
        System.out.println("Processing fee: " + processingFee + " Knuts");
        System.out.println("Updated balance: " + updatedBalance + " Knuts");
    }
}


