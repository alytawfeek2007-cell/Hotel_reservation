import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice implements Payable {

    private double totalAmount;
    private LocalDate paymentDate;
    private List<PaymentMethod> paymentMethods;
    private double balance; // track remaining balance

    public Invoice(double totalAmount) {
        this.totalAmount = totalAmount;
        this.paymentMethods = new ArrayList<>();
        this.balance = totalAmount; // initially unpaid
    }

    public void addPayment(PaymentMethod method) {
        paymentMethods.add(method);
        paymentDate = LocalDate.now();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
        this.balance = totalAmount; // reset balance if amount changes
    }

    // --- Payable interface methods ---
    @Override
    public void pay(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment must be positive.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Payment exceeds remaining balance.");
        }
        balance -= amount;
        paymentDate = LocalDate.now();
    }

    @Override
    public double getBalance() {
        return balance;
    }
}
