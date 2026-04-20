import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

    private double totalAmount;
    private LocalDate paymentDate;

    private List<PaymentMethod> paymentMethods; 

    public Invoice(double totalAmount) {
        this.totalAmount = totalAmount;
        this.paymentMethods = new ArrayList<>();
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
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
}
