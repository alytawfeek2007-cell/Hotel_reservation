public class RoomType {
   private int capacity;
   private String typeName;
   private double pricePerNight;
    

    public RoomType(int capacity, String typeName, double pricePerNight) {
        this.capacity = capacity;
        this.typeName = typeName;
        this.pricePerNight = pricePerNight;
       
    }
 

   public int getCapacity() {
    return capacity;
   }

   public void setCapacity(int capacity) {
    this.capacity = capacity;
   }

    public String gettypeName() {
        return typeName;
    }

    public void settypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }


  public void setPricePerNight(double pricePerNight) throws InvalidPaymentException {
    if (pricePerNight < 0) {
        throw new InvalidPaymentException("Price per night cannot be negative. Attempted: " + pricePerNight);
    }
    this.pricePerNight = pricePerNight;
}


}
