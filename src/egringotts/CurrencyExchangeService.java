package egringotts;

/**
 *
 * @author wenhu
 */
public class CurrencyExchangeService {
    private CurrencyExchange exchange;

    public CurrencyExchangeService() {
        exchange = new CurrencyExchange();
    }

    //perform currency exchange
    public void performCurrencyExchange(GoldenGalleon sender, String recipientAccountNum, String fromCurrency, String toCurrency, double amount) throws Exception {
        double convertedAmount = exchange.exchange(fromCurrency, toCurrency, amount);
        double totalFee = exchange.getProcessingFee(fromCurrency, toCurrency) * amount;

        // Fetch sender's current balance
        double currentBalanceSender = sender.getBalance(fromCurrency);

        if (currentBalanceSender < (amount + totalFee)) {
            throw new Exception("Insufficient balance for the transaction.");
        }

        // Deduct the total amount from sender's balance
        double newBalanceSender = currentBalanceSender - (amount + totalFee);

        // Update sender's balance
        sender.updateBalanceSender((double) newBalanceSender, fromCurrency);

        // Fetch recipient's customer account
        GoldenGalleon recipient = Account.getCustomerByAccountNumber(recipientAccountNum);
        double currentBalanceRecipient = recipient.getBalance(toCurrency);

        // Update recipient's balance
        double newBalanceRecipient = currentBalanceRecipient + convertedAmount;
        recipient.updateBalanceRecipient((double) newBalanceRecipient, toCurrency, recipient.getAccountN());

        // Record transaction
        Transaction transaction = new Transaction();
        transaction.recordCurrencyExchange(sender.getAccountN(), recipientAccountNum, amount, convertedAmount, totalFee, newBalanceSender, newBalanceRecipient);

        System.out.println("Transaction successful. Converted " + amount + " " + fromCurrency + " to " + convertedAmount + " " + toCurrency + " with a processing fee of " + totalFee + ".");
        System.out.println("New sender balance: " + newBalanceSender);
        System.out.println("New recipient balance: " + newBalanceRecipient);
    }
}
