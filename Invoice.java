import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

    private double totalAmount;
    private double paidAmount;
    private LocalDate paymentDate;

    private List<PaymentMethod> paymentMethods; 

    public Invoice(double totalAmount) {
        setTotalAmount(totalAmount);
        this.paidAmount = 0;
        this.paymentMethods = new ArrayList<>();
    }

    public void addPayment(PaymentMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("Payment method cannot be null.");
        }
        paymentMethods.add(method);
        paymentDate = LocalDate.now();
    }

    public void addPayment(PaymentMethod method, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0.");
        }
        if (paidAmount + amount > totalAmount) {
            throw new IllegalArgumentException("Payment exceeds invoice total amount.");
        }
        addPayment(method);
        paidAmount += amount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public double getRemainingAmount() {
        return totalAmount - paidAmount;
    }

    public boolean isFullyPaid() {
        return paidAmount >= totalAmount;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setTotalAmount(double totalAmount) {
        if (totalAmount < 0) {
            throw new IllegalArgumentException("Invoice total amount cannot be negative.");
        }
        this.totalAmount = totalAmount;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        if (paymentMethods == null) {
            throw new IllegalArgumentException("Payment methods list cannot be null.");
        }
        this.paymentMethods = paymentMethods;
    }
}
