public class Amenity {
   private String name;
   private String description;
   private double price;
   public String getName() {
    return name;
   }
   public void setName(String name) {
    this.name = name;
   }
   public String getDescription() {
    return description;
   }
   public void setDescription(String description) {
    this.description = description;
   }
   public double getPrice() {
    return price;
   }
  public void setPrice(double price) throws InvalidPaymentException {
    if (price < 0) {
        throw new InvalidPaymentException("Price cannot be negative. Provided value: " + price);
    }
    this.price = price;
}
}
