public class PaymentRecord {

    private double amount;
    private PaymentMethod method;

    public PaymentRecord(double amount, PaymentMethod method) {
        this.amount = amount;
        this.method = method;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }
}
