package egringotts;

/**
 *
 * @author wenhu
 */
import java.util.*;

public class currencyExchange {
    private Map<String, List<Edge>> graph;

    public currencyExchange() {
        graph = new HashMap<>();
        initializeRates();
    }

    //initialize currency conversion rates
    private void initializeRates() {
        addCurrencyConversion("Knut", "Sickle", 1.0 / 29, 0.01);
        addCurrencyConversion("Sickle", "Galleon", 1.0 / 17, 0.2);
        addCurrencyConversion("Knut", "Galleon", 1.0 / 493, 0.03);
    }

    //add currency conversion rates
    public void addCurrencyConversion(String from, String to, double rate, double fee) {
        graph.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, rate, fee));
        graph.computeIfAbsent(to, k -> new ArrayList<>()).add(new Edge(from, 1 / rate, fee));
    }

    //perform the currency exchange
    public ConversionResult convert(String from, String to, double amount) {
        PriorityQueue<Vertex> queue = new PriorityQueue<>();
        Map<String, java.lang.Double> distances = new HashMap<>();
        Map<String, java.lang.Double> fees = new HashMap<>();
        Map<String, String> previous = new HashMap<>();

        for (String currency : graph.keySet()) {
            distances.put(currency, java.lang.Double.POSITIVE_INFINITY);
            fees.put(currency, 0.0);
        }
        distances.put(from, 0.0);
        queue.add(new Vertex(from, 0.0, 0.0));

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            if (current.currency.equals(to)) {
                break;
            }

            for (Edge edge : graph.getOrDefault(current.currency, new ArrayList<>())) {
                double newDist = current.distance + Math.log(edge.rate);
                double newFee = current.fee + edge.fee * amount;

                if (newDist < distances.get(edge.to)) {
                    distances.put(edge.to, newDist);
                    fees.put(edge.to, newFee);
                    previous.put(edge.to, current.currency);
                    queue.add(new Vertex(edge.to, newDist, newFee));
                }
            }
        }

        double convertedAmount = amount * Math.exp(distances.get(to));
        double totalFee = fees.get(to);
        return new ConversionResult(convertedAmount, totalFee);
    }

    //conversion rates and fees
    private static class Edge {
        String to;
        double rate;
        double fee;

        Edge(String to, double rate, double fee) {
            this.to = to;
            this.rate = rate;
            this.fee = fee;
        }
    }

    //priority queue
    private static class Vertex implements Comparable<Vertex> {
        String currency;
        double distance;
        double fee;

        Vertex(String currency, double distance, double fee) {
            this.currency = currency;
            this.distance = distance;
            this.fee = fee;
        }

        @Override
        public int compareTo(Vertex other) {
            return java.lang.Double.compare(this.distance, other.distance);
        }
    }

    //hold conversion result details
    public static class ConversionResult {
        double convertedAmount;
        double totalFee;

        ConversionResult(double convertedAmount, double totalFee) {
            this.convertedAmount = convertedAmount;
            this.totalFee = totalFee;
        }

        public double getConvertedAmount() {
            return convertedAmount;
        }

        public double getTotalFee() {
            return totalFee;
        }
    }
}
