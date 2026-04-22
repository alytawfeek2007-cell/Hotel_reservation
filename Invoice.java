import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Invoice implements Payable {

    private final String invoiceId;
    private static int idCounter = 1;

    
    private final double totalAmount;     // fixed at creation, never changes
    private double paidAmount;

    
    private final List<PaymentMethod> paymentMethods;
    private LocalDate paymentDate;        // date of the most recent payment



    public Invoice(double totalAmount) {
        if (totalAmount < 0)
            throw new IllegalArgumentException("Invoice total amount cannot be negative.");

        this.invoiceId      = "INV-" + String.format("%04d", idCounter++);
        this.totalAmount    = totalAmount;
        this.paidAmount     = 0;
        this.paymentMethods = new ArrayList<>();
    }


    
    public String      getInvoiceId()       { return invoiceId; }
    public double      getTotalAmount()     { return totalAmount; }
    public double      getPaidAmount()      { return paidAmount; }
    public LocalDate   getPaymentDate()     { return paymentDate; }


    public double getRemainingAmount() {
        return totalAmount - paidAmount;
    }

    public boolean isFullyPaid() {
        return paidAmount >= totalAmount;
    }



    public void pay(double amount, PaymentMethod method) {
            if (method == null)
                throw new IllegalArgumentException("Payment method cannot be null.");
            if (amount <= 0)
                throw new IllegalArgumentException("Payment amount must be greater than 0.");
            if (isFullyPaid())
                throw new IllegalStateException("Invoice " + invoiceId + " is already fully paid.");
            if (paidAmount + amount > totalAmount)
                throw new IllegalArgumentException(
                    "Payment of $" + amount + " exceeds remaining balance of $" + getRemainingAmount() + ".");

            paidAmount += amount;
            paymentMethods.add(method);
            paymentDate = LocalDate.now();

            System.out.printf("Payment of $%.2f via %s recorded. Remaining: $%.2f%n",
                amount, method, getRemainingAmount());
        }






    public void setTotalAmount(double totalAmount) {
        if (totalAmount < 0) {
            throw new IllegalArgumentException("Invoice total amount cannot be negative.");
        }
        this.totalAmount = totalAmount;
        this.balance = totalAmount; // reset balance if amount changes
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invoice)) return false;
        return invoiceId.equals(((Invoice) o).invoiceId);
    }

    @Override
public String toString() {
    return "Invoice{" +
        "id='" + invoiceId + '\'' +
        " | total=" + totalAmount +
        " | paid=" + paidAmount +
        " | remaining=" + getRemainingAmount() +
        " | methods=" + paymentMethods +
        " | paymentDate=" + (paymentDate != null ? paymentDate : "not yet paid") +
        " | fullyPaid=" + isFullyPaid() +
        '}';
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
